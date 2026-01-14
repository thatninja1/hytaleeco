package com.hytaleeco.plugin.economy;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BalanceLedgerTest {

    @Test
    void transferValidatesAmountAndFunds() {
        BalanceLedger ledger = new BalanceLedger();
        UUID sender = UUID.randomUUID();
        UUID receiver = UUID.randomUUID();

        ledger.setBalance(sender, 50);

        assertEquals(BalanceLedger.TransferResult.INVALID_AMOUNT, ledger.transfer(sender, receiver, 0));
        assertEquals(BalanceLedger.TransferResult.INSUFFICIENT_FUNDS, ledger.transfer(sender, receiver, 75));
        assertEquals(50, ledger.getBalance(sender));
    }

    @Test
    void transferPreventsSelfPay() {
        BalanceLedger ledger = new BalanceLedger();
        UUID player = UUID.randomUUID();
        ledger.setBalance(player, 100);

        assertEquals(BalanceLedger.TransferResult.SELF_TRANSFER, ledger.transfer(player, player, 10));
        assertEquals(100, ledger.getBalance(player));
    }

    @Test
    void transferMovesBalancesOnSuccess() {
        BalanceLedger ledger = new BalanceLedger();
        UUID sender = UUID.randomUUID();
        UUID receiver = UUID.randomUUID();
        ledger.setBalance(sender, 120);

        assertEquals(BalanceLedger.TransferResult.SUCCESS, ledger.transfer(sender, receiver, 20));
        assertEquals(100, ledger.getBalance(sender));
        assertEquals(20, ledger.getBalance(receiver));
    }

    @Test
    void topBalancesSortsDescending() {
        BalanceLedger ledger = new BalanceLedger();
        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();
        UUID third = UUID.randomUUID();

        ledger.setBalance(first, 500);
        ledger.setBalance(second, 2000);
        ledger.setBalance(third, 750);

        List<BalanceEntry> top = ledger.topBalances(10);

        assertEquals(second, top.get(0).playerId());
        assertEquals(third, top.get(1).playerId());
        assertEquals(first, top.get(2).playerId());
    }
}
