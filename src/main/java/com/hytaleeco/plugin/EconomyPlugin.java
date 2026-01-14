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
import javax.annotation.Nonnull;


public class EconomyPlugin extends JavaPlugin {

    private final JavaPluginInit init;
    private EconomyService economyService;

    public EconomyPlugin(@Nonnull JavaPluginInit init) {
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
        init.commandRegistry().registerCommand(new BalCommand("bal", "Show your balance", economyService));
        logger.info("Registered /bal");
        init.commandRegistry().registerCommand(new PayCommand("pay", "Pay another player", economyService));
        logger.info("Registered /pay");
        init.commandRegistry().registerCommand(new BaltopCommand("baltop", "Show top balances", economyService));
        logger.info("Registered /baltop");
        init.commandRegistry().registerCommand(new EcoCommand("eco", "Economy admin commands", economyService));
        logger.info("Registered /eco");
        logger.info("Registered /eco give");
        logger.info("Registered /eco set");
        logger.info("EconomyPlugin setup complete");
    }

}
