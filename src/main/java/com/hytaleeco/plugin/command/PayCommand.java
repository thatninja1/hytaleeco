package com.hytaleeco.plugin.command;

import com.hytaleeco.plugin.economy.BalanceLedger;
import com.hytaleeco.plugin.economy.EconomyService;
import com.hytaleeco.plugin.util.MessageUtil;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public class PayCommand extends CommandBase {

    private final EconomyService economyService;
    private final RequiredArg<PlayerRef> playerArg;
    private final RequiredArg<Long> amountArg;

    public PayCommand(String name, String description, EconomyService economyService) {
        super(name, description);
        this.economyService = economyService;
        this.playerArg = this.withRequiredArg("player", "hytaleeco.command.pay.player", ArgTypes.PLAYER_REF);
        this.amountArg = this.withRequiredArg("amount", "hytaleeco.command.pay.amount", ArgTypes.LONG);
    }

    @Override
    protected void executeSync(CommandContext context) {
        PlayerRef sender = context.sender().getPlayer();
        if (sender == null) {
            context.sendMessage(MessageUtil.raw("Only players can use /pay."));
            return;
        }
        PlayerRef target = context.get(this.playerArg);
        Long amount = context.get(this.amountArg);
        if (amount == null || amount <= 0) {
            context.sendMessage(MessageUtil.raw("Amount must be a number greater than 0."));
            return;
        }
        if (sender.getUuid().equals(target.getUuid())) {
            context.sendMessage(MessageUtil.raw("You cannot pay yourself."));
            return;
        }
        economyService.refreshPlayer(sender);
        economyService.refreshPlayer(target);
        BalanceLedger.TransferResult result = economyService.transfer(sender.getUuid(), target.getUuid(), amount);
        switch (result) {
            case SUCCESS -> {
                context.sendMessage(MessageUtil.raw("You paid " + target.getUsername() + " " + amount + "."));
                target.sendMessage(MessageUtil.raw("You received " + amount + " from " + sender.getUsername() + "."));
            }
            case INSUFFICIENT_FUNDS -> context.sendMessage(MessageUtil.raw("You do not have enough funds."));
            case SELF_TRANSFER -> context.sendMessage(MessageUtil.raw("You cannot pay yourself."));
            case INVALID_AMOUNT -> context.sendMessage(MessageUtil.raw("Amount must be a number greater than 0."));
        }
    }
}
