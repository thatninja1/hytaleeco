package com.hytaleeco.plugin;

import com.hytaleeco.plugin.command.BalCommand;
import com.hytaleeco.plugin.command.BaltopCommand;
import com.hytaleeco.plugin.command.EcoCommand;
import com.hytaleeco.plugin.command.PayCommand;
import com.hytaleeco.plugin.economy.EconomyService;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import java.util.logging.Logger;


public class EconomyPlugin extends JavaPlugin {

    private EconomyService economyService;

    public EconomyPlugin(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        super.setup();
        Logger logger = Logger.getLogger("EconomyPlugin");
        this.economyService = new EconomyService(logger, this.getDataFolder().toPath());
        this.getCommandRegistry().registerCommand(new BalCommand("bal", "Show your balance", false, economyService));
        this.getCommandRegistry().registerCommand(new PayCommand("pay", "Pay another player", false, economyService));
        this.getCommandRegistry().registerCommand(new BaltopCommand("baltop", "Show top balances", false, economyService));
        this.getCommandRegistry().registerCommand(new EcoCommand("eco", "Economy admin commands", false, economyService));
    }

}
