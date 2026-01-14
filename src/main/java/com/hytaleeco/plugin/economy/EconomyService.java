package com.hytaleeco.plugin.economy;

import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public final class EconomyService {

    public record DisplayBalance(String name, UUID playerId, long balance) {
    }

    private final BalanceLedger ledger;
    private final Map<UUID, String> names;
    private final Path dataFile;
    private final Logger logger;

    public EconomyService(Logger logger, Path balancesPath) {
        this.ledger = new BalanceLedger();
        this.names = new HashMap<>();
        this.logger = logger == null ? Logger.getLogger("EconomyPlugin") : logger;
        this.dataFile = balancesPath;
        BalanceStore.Snapshot snapshot = BalanceStore.load(dataFile, logger);
        ledger.loadBalances(snapshot.balances());
        names.putAll(snapshot.names());
    }

    public long getBalance(UUID playerId) {
        return ledger.getBalance(playerId);
    }

    public void refreshPlayer(PlayerRef playerRef) {
        names.put(playerRef.getUuid(), playerRef.getName());
    }

    public BalanceLedger.TransferResult transfer(UUID from, UUID to, long amount) {
        BalanceLedger.TransferResult result = ledger.transfer(from, to, amount);
        if (result == BalanceLedger.TransferResult.SUCCESS) {
            save();
        }
        return result;
    }

    public void give(UUID playerId, long amount) {
        ledger.addBalance(playerId, amount);
        save();
    }

    public void setBalance(UUID playerId, long amount) {
        ledger.setBalance(playerId, amount);
        save();
    }

    public List<DisplayBalance> topBalances(int limit) {
        List<BalanceEntry> top = ledger.topBalances(limit);
        List<DisplayBalance> display = new ArrayList<>();
        for (BalanceEntry entry : top) {
            String name = names.getOrDefault(entry.playerId(), entry.playerId().toString());
            display.add(new DisplayBalance(name, entry.playerId(), entry.balance()));
        }
        return display;
    }

    private void save() {
        try {
            BalanceStore.save(dataFile, new BalanceStore.Snapshot(ledger.snapshotBalances(), new HashMap<>(names)));
        } catch (Exception ex) {
            logger.warning("Failed to save balances.json: " + ex.getMessage());
        }
    }
}
