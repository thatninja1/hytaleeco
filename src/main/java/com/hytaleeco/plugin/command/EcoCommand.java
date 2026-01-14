package com.hytaleeco.plugin.command;

import com.hytaleeco.plugin.economy.EconomyService;
import com.hytaleeco.plugin.util.CommandUtil;
import com.hytaleeco.plugin.util.MessageUtil;
import com.hytaleeco.plugin.util.PermissionUtil;
import com.hytaleeco.plugin.util.PlayerLookup;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.List;
import java.util.Optional;

public class EcoCommand extends AbstractPlayerCommand {

    private final EconomyService economyService;

    public EcoCommand(String name, String description, boolean requiresConfirmation,
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
        if (!PermissionUtil.hasEconomyAdmin(playerRef)) {
            MessageUtil.send(playerRef, "You do not have permission to use this command.");
            return;
        }
        List<String> args = CommandUtil.getArguments(commandContext);
        if (args.size() < 3) {
            MessageUtil.send(playerRef, "Usage: /eco give {user} {amt} | /eco set {user} {amt}");
            return;
        }
        String subcommand = args.get(0).toLowerCase();
        String targetName = args.get(1);
        Optional<PlayerRef> target = PlayerLookup.findOnlinePlayer(world, targetName);
        if (target.isEmpty()) {
            MessageUtil.send(playerRef, "Player not found or not online.");
            return;
        }
        PlayerRef targetRef = target.get();
        economyService.refreshPlayer(targetRef);
        economyService.refreshPlayer(playerRef);

        switch (subcommand) {
            case "give" -> handleGive(args.get(2), playerRef, targetRef);
            case "set" -> handleSet(args.get(2), playerRef, targetRef);
            default -> MessageUtil.send(playerRef, "Usage: /eco give {user} {amt} | /eco set {user} {amt}");
        }
    }

    private void handleGive(String rawAmount, PlayerRef actor, PlayerRef target) {
        Long amount = CommandUtil.parsePositiveAmount(rawAmount);
        if (amount == null) {
            MessageUtil.send(actor, "Amount must be a number greater than 0.");
            return;
        }
        economyService.give(target.getUuid(), amount);
        MessageUtil.send(actor, "Gave " + target.getName() + " " + amount + ".");
        MessageUtil.send(target, "You received " + amount + " from an admin.");
    }

    private void handleSet(String rawAmount, PlayerRef actor, PlayerRef target) {
        Long amount = CommandUtil.parseNonNegativeAmount(rawAmount);
        if (amount == null) {
            MessageUtil.send(actor, "Amount must be a number 0 or higher.");
            return;
        }
        economyService.setBalance(target.getUuid(), amount);
        MessageUtil.send(actor, "Set " + target.getName() + "'s balance to " + amount + ".");
        MessageUtil.send(target, "Your balance was set to " + amount + " by an admin.");
    }
}
