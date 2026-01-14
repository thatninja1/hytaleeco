package com.hytaleeco.plugin.command;

import com.hytaleeco.plugin.economy.BalanceLedger;
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
import java.util.Optional;

public class PayCommand extends AbstractPlayerCommand {

    private final EconomyService economyService;

    public PayCommand(String name, String description, boolean requiresConfirmation,
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
        if (args.size() < 2) {
            MessageUtil.send(playerRef, "Usage: /pay {user} {amt}");
            return;
        }
        String targetName = args.get(0);
        Long amount = CommandUtil.parsePositiveAmount(args.get(1));
        if (amount == null) {
            MessageUtil.send(playerRef, "Amount must be a number greater than 0.");
            return;
        }
        if (playerRef.getName().equalsIgnoreCase(targetName)) {
            MessageUtil.send(playerRef, "You cannot pay yourself.");
            return;
        }
        Optional<PlayerRef> target = PlayerLookup.findOnlinePlayer(world, targetName);
        if (target.isEmpty()) {
            MessageUtil.send(playerRef, "Player not found or not online.");
            return;
        }
        PlayerRef targetRef = target.get();
        economyService.refreshPlayer(playerRef);
        economyService.refreshPlayer(targetRef);
        BalanceLedger.TransferResult result = economyService.transfer(playerRef.getUuid(), targetRef.getUuid(), amount);
        switch (result) {
            case SUCCESS -> {
                MessageUtil.send(playerRef, "You paid " + targetRef.getName() + " " + amount + ".");
                MessageUtil.send(targetRef, "You received " + amount + " from " + playerRef.getName() + ".");
            }
            case INSUFFICIENT_FUNDS -> MessageUtil.send(playerRef, "You do not have enough funds.");
            case SELF_TRANSFER -> MessageUtil.send(playerRef, "You cannot pay yourself.");
            case INVALID_AMOUNT -> MessageUtil.send(playerRef, "Amount must be a number greater than 0.");
        }
    }
}
