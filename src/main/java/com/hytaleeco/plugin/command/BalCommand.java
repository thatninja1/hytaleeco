package com.hytaleeco.plugin.command;

import com.hytaleeco.plugin.economy.EconomyService;
import com.hytaleeco.plugin.util.MessageUtil;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public class BalCommand extends CommandBase {

    private final EconomyService economyService;
    private final OptionalArg<PlayerRef> playerArg;

    public BalCommand(String name, String description, EconomyService economyService) {
        super(name, description);
        this.economyService = economyService;
        this.playerArg = this.withOptionalArg("player", "hytaleeco.command.bal.player", ArgTypes.PLAYER_REF);
        this.addAliases("balance");
    }

    @Override
    protected void executeSync(CommandContext context) {
        PlayerRef target = context.get(this.playerArg);
        PlayerRef sender = context.sender().getPlayer();
        if (target == null && sender == null) {
            context.sendMessage(MessageUtil.raw("Only players can use /bal without a target."));
            return;
        }
        if (target == null) {
            economyService.refreshPlayer(sender);
            long balance = economyService.getBalance(sender.getUuid());
            context.sendMessage(MessageUtil.raw("Your balance is " + balance + "."));
            return;
        }
        economyService.refreshPlayer(target);
        long balance = economyService.getBalance(target.getUuid());
        context.sendMessage(MessageUtil.raw(target.getUsername() + "'s balance is " + balance + "."));
    }
}
