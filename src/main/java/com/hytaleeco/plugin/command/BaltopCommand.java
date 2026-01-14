package com.hytaleeco.plugin.command;

import com.hytaleeco.plugin.economy.EconomyService;
import com.hytaleeco.plugin.util.CommandArgs;
import com.hytaleeco.plugin.util.MessageUtil;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import java.util.List;
import javax.annotation.Nonnull;

public class BaltopCommand extends CommandBase {

    private final EconomyService economyService;
    private final String commandName;

    public BaltopCommand(String name, String description, EconomyService economyService) {
        super(name, description);
        this.economyService = economyService;
        this.commandName = name;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        java.util.List<String> args = CommandArgs.getArgs(context, commandName);
        if (!args.isEmpty()) {
            context.sendMessage(MessageUtil.raw("Usage: /baltop"));
            return;
        }
        List<EconomyService.DisplayBalance> top = economyService.topBalances(10);
        if (top.isEmpty()) {
            context.sendMessage(MessageUtil.raw("No balances recorded yet."));
            return;
        }
        context.sendMessage(MessageUtil.raw("Top balances:"));
        int index = 1;
        for (EconomyService.DisplayBalance entry : top) {
            context.sendMessage(MessageUtil.raw(index + ") " + entry.name() + " — " + entry.balance()));
            index++;
        }
    }
}
