package com.hytaleeco.plugin.command;

import com.hytaleeco.plugin.economy.EconomyService;
import com.hytaleeco.plugin.util.CommandUtil;
import com.hytaleeco.plugin.util.MessageUtil;
import com.hytaleeco.plugin.util.PlayerLookup;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.List;

public class BalCommand extends AbstractPlayerCommand {

    private final EconomyService economyService;

    public BalCommand(String name, String description, boolean requiresConfirmation,
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
        economyService.refreshPlayer(playerRef);
        if (args.isEmpty()) {
            long balance = economyService.getBalance(playerRef.getUuid());
            MessageUtil.send(playerRef, "Your balance is " + balance + ".");
            return;
        }
        if (args.size() > 1) {
            MessageUtil.send(playerRef, "Usage: /bal [user]");
            return;
        }
        String targetName = args.get(0);
        PlayerLookup.findOnlinePlayer(world, targetName).ifPresentOrElse(targetRef -> {
            economyService.refreshPlayer(targetRef);
            long balance = economyService.getBalance(targetRef.getUuid());
            MessageUtil.send(playerRef, targetRef.getName() + "'s balance is " + balance + ".");
        }, () -> MessageUtil.send(playerRef, "Player not found."));
    }
}
