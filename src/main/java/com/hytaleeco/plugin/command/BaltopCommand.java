package com.hytaleeco.plugin.command;

import com.hytaleeco.plugin.economy.EconomyService;
import com.hytaleeco.plugin.util.CommandUtil;
import com.hytaleeco.plugin.util.MessageUtil;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.List;

public class BaltopCommand extends AbstractPlayerCommand {

    private final EconomyService economyService;

    public BaltopCommand(String name, String description, boolean requiresConfirmation,
                         EconomyService economyService) {
        super(name, description, requiresConfirmation);
        this.economyService = economyService;
    }

    @Override
    protected void execute(
            CommandContext commandContext,
            Store<EntityStore> store,
            Ref<EntityStore> ref,
            PlayerRef playerRef,
            World world
    ) {
        List<String> args = CommandUtil.getArguments(commandContext);
        if (!args.isEmpty()) {
            MessageUtil.send(playerRef, "Usage: /baltop");
            return;
        }
        List<EconomyService.DisplayBalance> top = economyService.topBalances(10);
        if (top.isEmpty()) {
            MessageUtil.send(playerRef, "No balances recorded yet.");
            return;
        }
        MessageUtil.send(playerRef, "Top balances:");
        int index = 1;
        for (EconomyService.DisplayBalance entry : top) {
            MessageUtil.send(playerRef, index + ") " + entry.name() + " — " + entry.balance());
            index++;
        }
    }
}
