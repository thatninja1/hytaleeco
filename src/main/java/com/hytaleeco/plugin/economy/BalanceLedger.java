package com.hytaleeco.plugin.economy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class BalanceLedger {

    public enum TransferResult {
        SUCCESS,
        INVALID_AMOUNT,
        SELF_TRANSFER,
        INSUFFICIENT_FUNDS
    }

    private final Map<UUID, Long> balances = new HashMap<>();

    public long getBalance(UUID playerId) {
        return balances.getOrDefault(playerId, 0L);
    }

    public void setBalance(UUID playerId, long amount) {
        balances.put(playerId, amount);
    }

    public void addBalance(UUID playerId, long amount) {
        balances.put(playerId, getBalance(playerId) + amount);
    }

    public TransferResult transfer(UUID from, UUID to, long amount) {
        if (amount <= 0) {
            return TransferResult.INVALID_AMOUNT;
        }
        if (from.equals(to)) {
            return TransferResult.SELF_TRANSFER;
        }
        long fromBalance = getBalance(from);
        if (fromBalance < amount) {
            return TransferResult.INSUFFICIENT_FUNDS;
        }
        balances.put(from, fromBalance - amount);
        balances.put(to, getBalance(to) + amount);
        return TransferResult.SUCCESS;
    }

    public List<BalanceEntry> topBalances(int limit) {
        List<BalanceEntry> entries = new ArrayList<>();
        for (Map.Entry<UUID, Long> entry : balances.entrySet()) {
            entries.add(new BalanceEntry(entry.getKey(), entry.getValue()));
        }
        entries.sort(Comparator.comparingLong(BalanceEntry::balance).reversed()
                .thenComparing(BalanceEntry::playerId));
        if (entries.size() > limit) {
            return new ArrayList<>(entries.subList(0, limit));
        }
        return entries;
    }

    public Map<UUID, Long> snapshotBalances() {
        return new HashMap<>(balances);
    }

    public void loadBalances(Map<UUID, Long> newBalances) {
        balances.clear();
        balances.putAll(newBalances);
    }
}
