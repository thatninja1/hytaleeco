package com.hytaleeco.plugin.economy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BalanceStore {

    public record Snapshot(Map<UUID, Long> balances, Map<UUID, String> names) {
    }

    private static final Pattern BALANCE_ENTRY = Pattern.compile("\\\"([^\\\"]+)\\\"\\s*:\\s*(\\d+)");
    private static final Pattern NAME_ENTRY = Pattern.compile("\\\"([^\\\"]+)\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"");

    private BalanceStore() {
    }

    public static Snapshot load(Path filePath, Logger logger) {
        if (!Files.exists(filePath)) {
            return new Snapshot(new HashMap<>(), new HashMap<>());
        }
        try {
            String json = Files.readString(filePath, StandardCharsets.UTF_8);
            Map<UUID, Long> balances = parseBalances(json);
            Map<UUID, String> names = parseNames(json);
            return new Snapshot(balances, names);
        } catch (IOException | RuntimeException ex) {
            logger.log(Level.WARNING, "Failed to read balances.json, starting fresh.", ex);
            return new Snapshot(new HashMap<>(), new HashMap<>());
        }
    }

    public static void save(Path filePath, Snapshot snapshot) throws IOException {
        Files.createDirectories(filePath.getParent());
        String json = toJson(snapshot);
        Path tempFile = filePath.resolveSibling(filePath.getFileName() + ".tmp");
        Files.writeString(tempFile, json, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        try {
            Files.move(tempFile, filePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException ex) {
            Files.move(tempFile, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static Map<UUID, Long> parseBalances(String json) {
        String objectJson = extractObject(json, "balances");
        Map<UUID, Long> balances = new HashMap<>();
        Matcher matcher = BALANCE_ENTRY.matcher(objectJson);
        while (matcher.find()) {
            String id = matcher.group(1);
            String value = matcher.group(2);
            try {
                balances.put(UUID.fromString(id), Long.parseLong(value));
            } catch (IllegalArgumentException ex) {
                // Skip invalid entries
            }
        }
        return balances;
    }

    private static Map<UUID, String> parseNames(String json) {
        String objectJson = extractObject(json, "names");
        Map<UUID, String> names = new HashMap<>();
        Matcher matcher = NAME_ENTRY.matcher(objectJson);
        while (matcher.find()) {
            String id = matcher.group(1);
            String value = matcher.group(2);
            try {
                names.put(UUID.fromString(id), value);
            } catch (IllegalArgumentException ex) {
                // Skip invalid entries
            }
        }
        return names;
    }

    private static String extractObject(String json, String key) {
        int keyIndex = json.indexOf("\"" + key + "\"");
        if (keyIndex < 0) {
            return "";
        }
        int start = json.indexOf('{', keyIndex);
        if (start < 0) {
            return "";
        }
        int depth = 0;
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    return json.substring(start + 1, i);
                }
            }
        }
        return "";
    }

    private static String toJson(Snapshot snapshot) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("  \"balances\": {");
        appendBalances(builder, snapshot.balances());
        builder.append("  },\n");
        builder.append("  \"names\": {");
        appendNames(builder, snapshot.names());
        builder.append("  }\n");
        builder.append("}\n");
        return builder.toString();
    }

    private static void appendBalances(StringBuilder builder, Map<UUID, Long> balances) {
        boolean first = true;
        for (Map.Entry<UUID, Long> entry : balances.entrySet()) {
            if (!first) {
                builder.append(",");
            }
            builder.append("\n    \"").append(entry.getKey()).append("\": ")
                    .append(entry.getValue());
            first = false;
        }
        if (!balances.isEmpty()) {
            builder.append("\n  ");
        }
    }

    private static void appendNames(StringBuilder builder, Map<UUID, String> names) {
        boolean first = true;
        for (Map.Entry<UUID, String> entry : names.entrySet()) {
            if (!first) {
                builder.append(",");
            }
            builder.append("\n    \"").append(entry.getKey()).append("\": \"")
                    .append(escape(entry.getValue())).append("\"");
            first = false;
        }
        if (!names.isEmpty()) {
            builder.append("\n  ");
        }
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
