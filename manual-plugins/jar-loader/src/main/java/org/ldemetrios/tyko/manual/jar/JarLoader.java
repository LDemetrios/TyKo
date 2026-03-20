package org.ldemetrios.tyko.manual.jar;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Stream;

public final class JarLoader {
    private JarLoader() {
    }

    public static byte[] unjar(byte[] jarBytes) throws Throwable {
        Map<String, byte[]> map = new LinkedHashMap<>();
        try (JarInputStream jarInputStream = new JarInputStream(new ByteArrayInputStream(jarBytes))) {
            JarEntry entry;
            while ((entry = jarInputStream.getNextJarEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                if (!entry.getName().endsWith(".class") || entry.getName().equals("module-info.class")) {
                    continue;
                }
                map.put(classNameOf(entry), jarInputStream.readAllBytes());
            }
        }
        List<byte[]> pairs = map.entrySet().stream()
                .flatMap(entry -> Stream.of(entry.getKey().getBytes(StandardCharsets.UTF_8), entry.getValue()))
                .toList();

        int[] lengths = pairs.stream().mapToInt(it -> it.length).toArray();
        int totalEntries = pairs.size();
        int totalPayloadLength = pairs.stream().mapToInt(it -> it.length).sum();
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES + Integer.BYTES * totalEntries + totalPayloadLength);
        buffer.putInt(totalEntries);
        for (int length : lengths) {
            buffer.putInt(length);
        }
        for (byte[] pair : pairs) {
            buffer.put(pair);
        }
        return buffer.array();
    }

    private static String classNameOf(JarEntry entry) throws IOException {
        String name = entry.getName();
        if (!name.endsWith(".class")) {
            throw new IOException("Not a class entry: " + name);
        }
        return name.substring(0, name.length() - ".class".length()).replace('/', '.');
    }
}
