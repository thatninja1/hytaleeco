package com.hytaleeco.plugin.command;

import com.hytaleeco.plugin.economy.EconomyService;
import com.hytaleeco.plugin.util.MessageUtil;
import com.hytaleeco.plugin.util.PermissionUtil;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import javax.annotation.Nonnull;

public class EcoCommand extends CommandBase {

    private final EconomyService economyService;
    private final RequiredArg<String> actionArg;
    private final RequiredArg<PlayerRef> playerArg;
    private final RequiredArg<String> amountArg;

    public EcoCommand(String name, String description, EconomyService economyService) {
        super(name, description);
        this.economyService = economyService;
        this.actionArg = this.withRequiredArg("action", "hytaleeco.command.eco.action", ArgTypes.STRING);
        this.playerArg = this.withRequiredArg("player", "hytaleeco.command.eco.player", ArgTypes.PLAYER_REF);
        this.amountArg = this.withRequiredArg("amount", "hytaleeco.command.eco.amount", ArgTypes.STRING);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        if (!PermissionUtil.hasEconomyAdmin(context.sender())) {
            context.sendMessage(MessageUtil.raw("You do not have permission to use this command."));
            return;
        }
        String action = context.get(this.actionArg);
        PlayerRef target = context.get(this.playerArg);
        Long amount = parseAmount(context.get(this.amountArg));
        if (action == null || target == null || amount == null) {
            context.sendMessage(MessageUtil.raw("Usage: /eco give {user} {amt} | /eco set {user} {amt}"));
            return;
        }
        economyService.refreshPlayer(target);
        PlayerRef sender = context.sender().getPlayer();
        if (sender != null) {
            economyService.refreshPlayer(sender);
        }

        switch (action.toLowerCase()) {
            case "give" -> handleGive(amount, context, target);
            case "set" -> handleSet(amount, context, target);
            default -> context.sendMessage(MessageUtil.raw("Usage: /eco give {user} {amt} | /eco set {user} {amt}"));
        }
    }

    private void handleGive(long amount, CommandContext context, PlayerRef target) {
        if (amount <= 0) {
            context.sendMessage(MessageUtil.raw("Amount must be a number greater than 0."));
            return;
        }
        economyService.give(target.getUuid(), amount);
        context.sendMessage(MessageUtil.raw("Gave " + target.getUsername() + " " + amount + "."));
        target.sendMessage(MessageUtil.raw("You received " + amount + " from an admin."));
    }

    private void handleSet(long amount, CommandContext context, PlayerRef target) {
        if (amount < 0) {
            context.sendMessage(MessageUtil.raw("Amount must be a number 0 or higher."));
            return;
        }
        economyService.setBalance(target.getUuid(), amount);
        context.sendMessage(MessageUtil.raw("Set " + target.getUsername() + "'s balance to " + amount + "."));
        target.sendMessage(MessageUtil.raw("Your balance was set to " + amount + " by an admin."));
    }

    private Long parseAmount(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
