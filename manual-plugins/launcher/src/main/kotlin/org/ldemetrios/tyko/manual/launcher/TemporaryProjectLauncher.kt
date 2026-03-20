package org.ldemetrios.tyko.manual.launcher

import org.ldemetrios.tyko.model.TArray
import org.ldemetrios.tyko.model.TBytes
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.TInt
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.t
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.readBytes
import kotlin.io.path.writeBytes

private data class ExecutedCommand(
    val command: List<String>,
    val workingDirectory: Path,
    val exitCode: Int,
    val stdout: String,
    val stderr: String,
) {
    fun toTypst(): TDict<TValue> = TDict(
        mapOf(
            "command" to TArray(command.map { it.t }),
            "workingDirectory" to workingDirectory.toString().t,
            "exitCode" to TInt(exitCode.toLong()),
            "stdout" to stdout.t,
            "stderr" to stderr.t,
        )
    )
}

private data class ProjectLaunchResult(
    val projectRoot: Path,
    val executedCommands: List<ExecutedCommand>,
    val readFiles: Map<String, ByteArray>,
) {
    fun toTypst(): TDict<TValue> = TDict(
        mapOf(
            "projectRoot" to projectRoot.toString().t,
            "executedCommands" to TArray(executedCommands.map { it.toTypst() }),
            "readFiles" to TDict(readFiles.mapValues { (_, bytes) -> TBytes(bytes) }),
        )
    )
}

class TemporaryProjectLauncher(
    private val tempRoot: Path = Path.of(System.getProperty("java.io.tmpdir"), "tyko-manual-launcher"),
) {
    fun launch(
        name: TStr,
        files: TDict<TBytes>,
        commands: TArray<TArray<TStr>>,
    ): TDict<TValue> {
        val nameValue = name.value
        require(nameValue.isNotBlank()) { "Project name must not be blank." }

        val projectRoot = projectRootFor(nameValue)
        resetProjectRoot(projectRoot)
        writeFiles(projectRoot, files.value.mapValues { (_, content) -> content.value })

        var currentDirectory = projectRoot
        val executedCommands = mutableListOf<ExecutedCommand>()
        val readFiles = linkedMapOf<String, ByteArray>()

        for (command in commands.value.map { row -> row.value.map { it.value } }) {
            require(command.isNotEmpty()) { "Commands must not be empty." }
            if (command.first() == "cd") {
                require(command.size == 2) { "`cd` expects exactly one target directory." }
                currentDirectory = resolveDirectory(projectRoot, currentDirectory, command[1])
                continue
            }
            if (command.first() == "read") {
                require(command.size == 2) { "`read` expects exactly one file path." }
                val target = resolveCommandFile(projectRoot, currentDirectory, command[1])
                if (target.exists() && !target.isDirectory()) {
                    readFiles[command[1]] = target.readBytes()
                }
                continue
            }

            val result = execute(command, currentDirectory)
            executedCommands += result
        }

        return ProjectLaunchResult(
            projectRoot = projectRoot,
            executedCommands = executedCommands,
            readFiles = readFiles,
        ).toTypst()
    }

    private fun projectRootFor(name: String): Path {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(name.toByteArray())
            .joinToString("") { "%02x".format(it) }
            .take(12)
        val safeName = name.map { ch ->
            when {
                ch.isLetterOrDigit() -> ch
                ch == '-' || ch == '_' -> ch
                else -> '_'
            }
        }.joinToString("")
            .trim('_')
            .ifBlank { "project" }
            .take(64)
        return tempRoot.resolve("$safeName-$digest").normalize()
    }

    private fun resetProjectRoot(projectRoot: Path) {
        Files.createDirectories(tempRoot)
        if (projectRoot.exists()) {
            Files.walk(projectRoot).use { paths ->
                paths.sorted(Comparator.reverseOrder())
                    .forEach { path ->
                        if (path != projectRoot) {
                            path.deleteIfExists()
                        }
                    }
            }
        } else {
            Files.createDirectories(projectRoot)
        }
        Files.createDirectories(projectRoot)
    }

    private fun writeFiles(projectRoot: Path, files: Map<String, ByteArray>) {
        for ((relativePath, content) in files) {
            val path = resolveFile(projectRoot, relativePath)
            val parent = path.parent
            if (parent != null) {
                Files.createDirectories(parent)
            }
            path.writeBytes(content)
        }
    }

    private fun resolveFile(projectRoot: Path, relativePath: String): Path {
        require(relativePath.isNotBlank()) { "File path must not be blank." }
        require(!Path.of(relativePath).isAbsolute) { "File path must be relative: $relativePath" }
        val resolved = projectRoot.resolve(relativePath).normalize()
        require(resolved.startsWith(projectRoot)) {
            "File path escapes the project root: $relativePath"
        }
        return resolved
    }

    private fun resolveDirectory(projectRoot: Path, currentDirectory: Path, target: String): Path {
        require(target.isNotBlank()) { "`cd` target must not be blank." }
        val candidate = resolveCommandPath(projectRoot, currentDirectory, target)
        require(candidate.startsWith(projectRoot)) {
            "`cd` target escapes the project root: $target"
        }
        require(candidate.exists() && candidate.isDirectory()) {
            "`cd` target does not exist or is not a directory: $target"
        }
        return candidate
    }

    private fun resolveCommandFile(projectRoot: Path, currentDirectory: Path, target: String): Path {
        require(target.isNotBlank()) { "`read` target must not be blank." }
        return resolveCommandPath(projectRoot, currentDirectory, target)
    }

    private fun resolveCommandPath(projectRoot: Path, currentDirectory: Path, target: String): Path {
        val candidate = if (Path.of(target).isAbsolute) {
            Path.of(target)
        } else {
            currentDirectory.resolve(target)
        }.normalize()

        require(candidate.startsWith(projectRoot)) {
            "Command target escapes the project root: $target"
        }
        return candidate
    }

    private fun execute(command: List<String>, workingDirectory: Path): ExecutedCommand {
        val actualCommand = rewriteGradleCommand(command, workingDirectory)
        val process = try {
            ProcessBuilder(actualCommand)
                .directory(workingDirectory.toFile())
                .start()
        } catch (e: IOException) {
            if (!shouldRetryThroughShell(actualCommand, e)) throw e
            ProcessBuilder(loginShellCommand(actualCommand))
                .directory(workingDirectory.toFile())
                .start()
        }

        val stdout = ByteArrayOutputStream()
        val stderr = ByteArrayOutputStream()

        val stdoutThread = Thread {
            process.inputStream.use { input -> input.copyTo(stdout) }
        }
        val stderrThread = Thread {
            process.errorStream.use { input -> input.copyTo(stderr) }
        }
        stdoutThread.start()
        stderrThread.start()

        val exitCode = process.waitFor()
        stdoutThread.join()
        stderrThread.join()

        return ExecutedCommand(
            command = actualCommand,
            workingDirectory = workingDirectory,
            exitCode = exitCode,
            stdout = stdout.toString(Charsets.UTF_8),
            stderr = stderr.toString(Charsets.UTF_8),
        )
    }

    private fun rewriteGradleCommand(command: List<String>, workingDirectory: Path): List<String> {
        if (command.firstOrNull() != "gradle") return command
        val wrapper = workingDirectory.resolve("gradlew")
        return if (wrapper.exists() && !wrapper.isDirectory()) {
            listOf(wrapper.toAbsolutePath().toString()) + command.drop(1)
        } else {
            command
        }
    }

    private fun shouldRetryThroughShell(command: List<String>, error: IOException): Boolean {
        val executable = command.firstOrNull() ?: return false
        return '/' !in executable && error.message?.contains("error=2") == true
    }

    private fun loginShellCommand(command: List<String>): List<String> {
        val shell = System.getenv("SHELL")
            ?.takeIf { it.isNotBlank() }
            ?: "/bin/zsh"
        val script = command.joinToString(" ") { shellEscape(it) }
        return listOf(shell, "-lc", script)
    }

    private fun shellEscape(argument: String): String =
        "'" + argument.replace("'", "'\"'\"'") + "'"
}
