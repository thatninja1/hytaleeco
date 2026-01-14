package com.hytaleeco.plugin;

import com.hytaleeco.plugin.command.BalCommand;
import com.hytaleeco.plugin.command.BaltopCommand;
import com.hytaleeco.plugin.command.EcoCommand;
import com.hytaleeco.plugin.command.PayCommand;
import com.hytaleeco.plugin.economy.EconomyService;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;


public class EconomyPlugin extends JavaPlugin {

    private EconomyService economyService;

    public EconomyPlugin(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup(JavaPluginInit init) {
        super.setup(init);
        Logger logger = Logger.getLogger("EconomyPlugin");
        Path dataDir = Paths.get(System.getProperty("user.dir"), "plugins-data", "EconomyPlugin");
        try {
            Files.createDirectories(dataDir);
        } catch (Exception ex) {
            logger.warning("Failed to create data directory: " + ex.getMessage());
        }
        this.economyService = new EconomyService(logger, dataDir.resolve("balances.json"));
        init.getCommandRegistry().registerCommand(new BalCommand("bal", "Show your balance", false, economyService));
        init.getCommandRegistry().registerCommand(new PayCommand("pay", "Pay another player", false, economyService));
        init.getCommandRegistry().registerCommand(new BaltopCommand("baltop", "Show top balances", false, economyService));
        init.getCommandRegistry().registerCommand(new EcoCommand("eco", "Economy admin commands", false, economyService));
    }

}
