package com.hytaleeco.plugin.command;

import com.hytaleeco.plugin.economy.EconomyService;
import com.hytaleeco.plugin.util.CommandArgs;
import com.hytaleeco.plugin.util.MessageUtil;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import javax.annotation.Nonnull;

public class BalCommand extends CommandBase {

    private final EconomyService economyService;
    private final String commandName;
    public BalCommand(String name, String description, EconomyService economyService) {
        super(name, description);
        this.economyService = economyService;
        this.commandName = name;
        this.addAliases("balance");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        PlayerRef sender = context.sender().getPlayer();
        if (sender == null) {
            context.sendMessage(MessageUtil.raw("Only players can use /bal."));
            return;
        }
        java.util.List<String> args = CommandArgs.getArgs(context, commandName);
        if (!args.isEmpty()) {
            context.sendMessage(MessageUtil.raw("Usage: /bal"));
            return;
        }
        economyService.refreshPlayer(sender);
        long balance = economyService.getBalance(sender.getUuid());
        context.sendMessage(MessageUtil.raw("Your balance is " + balance + "."));
    }
}
