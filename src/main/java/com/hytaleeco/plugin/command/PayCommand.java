package com.hytaleeco.plugin.command;

import com.hytaleeco.plugin.economy.BalanceLedger;
import com.hytaleeco.plugin.economy.EconomyService;
import com.hytaleeco.plugin.util.CommandArgs;
import com.hytaleeco.plugin.util.MessageUtil;
import com.hytaleeco.plugin.util.PlayerResolver;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import javax.annotation.Nonnull;

public class PayCommand extends CommandBase {

    private final EconomyService economyService;
    private final String commandName;

    public PayCommand(String name, String description, EconomyService economyService) {
        super(name, description);
        this.economyService = economyService;
        this.commandName = name;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        PlayerRef sender = context.sender().getPlayer();
        if (sender == null) {
            context.sendMessage(MessageUtil.raw("Only players can use /pay."));
            return;
        }
        java.util.List<String> args = CommandArgs.getArgs(context, commandName);
        if (args.size() < 2) {
            context.sendMessage(MessageUtil.raw("Usage: /pay <user> <amt>"));
            return;
        }
        String targetName = args.get(0);
        Long amount = parsePositiveAmount(args.get(1));
        if (amount == null) {
            context.sendMessage(MessageUtil.raw("Amount must be a number greater than 0."));
            return;
        }
        PlayerRef target = PlayerResolver.findOnlinePlayer(targetName);
        if (target == null) {
            context.sendMessage(MessageUtil.raw("Player not found."));
            return;
        }
        if (sender.getUsername().equals(target.getUsername())) {
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

    private Long parsePositiveAmount(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            long value = Long.parseLong(raw);
            return value > 0 ? value : null;
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
