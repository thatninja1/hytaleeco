package com.hytaleeco.plugin.economy;

import java.util.UUID;

public record BalanceEntry(UUID playerId, long balance) {
}
