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

    private final JavaPluginInit init;
    private EconomyService economyService;

    public EconomyPlugin(JavaPluginInit init) {
        super(init);
        this.init = init;
    }

    @Override
    protected void setup() {
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
        logger.info("Registering commands: bal, pay, baltop, eco");
        this.getCommandRegistry().registerCommand(new BalCommand("bal", "Show your balance", economyService));
        logger.info("Registered /bal");
        this.getCommandRegistry().registerCommand(new PayCommand("pay", "Pay another player", economyService));
        logger.info("Registered /pay");
        this.getCommandRegistry().registerCommand(new BaltopCommand("baltop", "Show top balances", economyService));
        logger.info("Registered /baltop");
        this.getCommandRegistry().registerCommand(new EcoCommand("eco", "Economy admin commands", economyService));
        logger.info("Registered /eco");
        logger.info("EconomyPlugin setup complete");
    }

}
