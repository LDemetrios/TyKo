package org.ldemetrios.tyko.manual.launcher

import org.ldemetrios.tyko.model.TArray
import org.ldemetrios.tyko.model.TBytes
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.TInt
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.t
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertContentEquals

class TemporaryProjectLauncherTest {
    private val launcher = TemporaryProjectLauncher(
        tempRoot = Files.createTempDirectory("temporary-project-launcher-test")
    )

    private fun launch(
        name: String,
        files: Map<String, ByteArray>,
        commands: List<List<String>>,
    ): TDict<TValue> = launcher.launch(
        name = name.t,
        files = TDict(files.mapValues { (_, bytes) -> TBytes(bytes) }),
        commands = TArray(commands.map { command -> TArray(command.map { it.t }) }),
    )

    private fun executedCommands(result: TDict<TValue>): List<TDict<TValue>> =
        TArray.fromValue<TDict<TValue>>(result.getValue("executedCommands")).value

    private fun readFiles(result: TDict<TValue>): TDict<TBytes> =
        TDict.fromValue<TBytes>(result.getValue("readFiles"))

    @Test
    fun `launch writes files and executes commands from the project root`() {
        val result = launch(
            name = "alpha",
            files = mapOf("hello.txt" to "hello".encodeToByteArray()),
            commands = listOf(
                listOf("sh", "-c", "pwd"),
                listOf("sh", "-c", "cat hello.txt"),
            ),
        )

        val executedCommands = executedCommands(result)
        assertEquals(2, executedCommands.size)
        assertEquals(result.getValue("projectRoot"), executedCommands[0].getValue("workingDirectory"))
        assertEquals("hello", (executedCommands[1].getValue("stdout") as TStr).value.trim())
    }

    @Test
    fun `cd changes the working directory for subsequent commands`() {
        val result = launch(
            name = "beta",
            files = mapOf("nested/value.txt" to "inside".encodeToByteArray()),
            commands = listOf(
                listOf("cd", "nested"),
                listOf("sh", "-c", "pwd"),
                listOf("sh", "-c", "cat value.txt"),
            ),
        )

        val executedCommands = executedCommands(result)
        assertEquals(2, executedCommands.size)
        assertEquals(
            (result.getValue("projectRoot") as TStr).value + "/nested",
            (executedCommands[0].getValue("workingDirectory") as TStr).value,
        )
        assertEquals("inside", (executedCommands[1].getValue("stdout") as TStr).value.trim())
    }

    @Test
    fun `read captures file bytes into the result`() {
        val result = launch(
            name = "gamma",
            files = mapOf("nested/value.bin" to byteArrayOf(1, 2, 3)),
            commands = listOf(
                listOf("cd", "nested"),
                listOf("read", "value.bin"),
                listOf("read", "../nested/value.bin"),
            ),
        )

        assertEquals(0, executedCommands(result).size)
        assertContentEquals(byteArrayOf(1, 2, 3), readFiles(result).getValue("value.bin").value)
        assertContentEquals(byteArrayOf(1, 2, 3), readFiles(result).getValue("../nested/value.bin").value)
    }

    @Test
    fun `same project name resolves to the same path and resets stale contents`() {
        val first = launch(
            name = "stable-name",
            files = mapOf("tracked.txt" to "first".encodeToByteArray()),
            commands = listOf(
                listOf("sh", "-c", "touch extra.txt"),
            ),
        )

        val second = launch(
            name = "stable-name",
            files = mapOf("tracked.txt" to "second".encodeToByteArray()),
            commands = listOf(
                listOf("sh", "-c", "cat tracked.txt"),
            ),
        )

        assertEquals(first.getValue("projectRoot"), second.getValue("projectRoot"))
        assertEquals("second", (executedCommands(second).single().getValue("stdout") as TStr).value.trim())
        val projectRoot = (second.getValue("projectRoot") as TStr).value
        assertFalse(Files.exists(java.nio.file.Path.of(projectRoot, "extra.txt")))
        assertTrue(Files.exists(java.nio.file.Path.of(projectRoot, "tracked.txt")))
    }

    @Test
    fun `non-zero exit code is returned in the result instead of throwing`() {
        val result = launch(
            name = "non-zero-exit",
            files = emptyMap(),
            commands = listOf(
                listOf("sh", "-c", "echo boom >&2; exit 7"),
            ),
        )

        val command = executedCommands(result).single()
        assertEquals(7L, (command.getValue("exitCode") as TInt).value)
        assertEquals("boom", (command.getValue("stderr") as TStr).value.trim())
    }
}
