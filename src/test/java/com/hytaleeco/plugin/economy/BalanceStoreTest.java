package com.hytaleeco.plugin.economy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BalanceStoreTest {

    @TempDir
    Path tempDir;

    @Test
    void saveAndLoadSnapshot() throws Exception {
        Path filePath = tempDir.resolve("balances.json");
        Map<UUID, Long> balances = new HashMap<>();
        Map<UUID, String> names = new HashMap<>();
        UUID playerId = UUID.randomUUID();
        balances.put(playerId, 250L);
        names.put(playerId, "TestPlayer");

        BalanceStore.save(filePath, new BalanceStore.Snapshot(balances, names));

        BalanceStore.Snapshot loaded = BalanceStore.load(filePath, Logger.getLogger("test"));
        assertEquals(250L, loaded.balances().get(playerId));
        assertEquals("TestPlayer", loaded.names().get(playerId));
    }
}
