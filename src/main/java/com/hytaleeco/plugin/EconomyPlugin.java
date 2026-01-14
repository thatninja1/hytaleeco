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
        logger.info("EconomyPlugin setup started");
        Path dataDir = init.getDataDirectory();
        try {
            Files.createDirectories(dataDir);
            logger.info("Data dir: " + dataDir.toAbsolutePath());
        } catch (Exception ex) {
            logger.warning("Failed to create data directory: " + ex.getMessage());
        }
        this.economyService = new EconomyService(logger, dataDir);
        init.getCommandRegistry().registerCommand(new BalCommand("bal", "Show your balance", false, economyService));
        logger.info("Registered command: bal");
        init.getCommandRegistry().registerCommand(new BalCommand("balance", "Show your balance", false, economyService));
        logger.info("Registered command: balance");
        init.getCommandRegistry().registerCommand(new PayCommand("pay", "Pay another player", false, economyService));
        logger.info("Registered command: pay");
        init.getCommandRegistry().registerCommand(new BaltopCommand("baltop", "Show top balances", false, economyService));
        logger.info("Registered command: baltop");
        init.getCommandRegistry().registerCommand(new EcoCommand("eco", "Economy admin commands", false, economyService));
        logger.info("Registered command: eco");
        logger.info("EconomyPlugin setup complete");
    }

}
