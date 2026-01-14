package com.hytaleeco.plugin.command;

import com.hytaleeco.plugin.economy.EconomyService;
import com.hytaleeco.plugin.util.CommandArgs;
import com.hytaleeco.plugin.util.MessageUtil;
import com.hytaleeco.plugin.util.PermissionUtil;
import com.hytaleeco.plugin.util.PlayerResolver;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import javax.annotation.Nonnull;

public class EcoCommand extends CommandBase {

    private final EconomyService economyService;
    private final String commandName;

    public EcoCommand(String name, String description, EconomyService economyService) {
        super(name, description);
        this.economyService = economyService;
        this.commandName = name;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        if (!PermissionUtil.hasEconomyAdmin(context.sender())) {
            context.sendMessage(MessageUtil.raw("You do not have permission to use this command."));
            return;
        }
        java.util.List<String> args = CommandArgs.getArgs(context, commandName);
        if (args.size() < 3) {
            context.sendMessage(MessageUtil.raw("Usage: /eco give <user> <amt> | /eco set <user> <amt>"));
            return;
        }
        String action = args.get(0);
        String targetName = args.get(1);
        Long amount = parseAmount(args.get(2));
        if (amount == null) {
            context.sendMessage(MessageUtil.raw("Amount must be a number 0 or higher."));
            return;
        }
        PlayerRef target = PlayerResolver.findOnlinePlayer(targetName);
        if (target == null) {
            context.sendMessage(MessageUtil.raw("Player not found."));
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
            default -> context.sendMessage(MessageUtil.raw("Usage: /eco give <user> <amt> | /eco set <user> <amt>"));
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
