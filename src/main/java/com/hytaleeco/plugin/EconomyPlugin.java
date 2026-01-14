package com.hytaleeco.plugin;

import com.hytaleeco.plugin.command.BalCommand;
import com.hytaleeco.plugin.command.BaltopCommand;
import com.hytaleeco.plugin.command.EcoCommand;
import com.hytaleeco.plugin.command.PayCommand;
import com.hytaleeco.plugin.economy.EconomyService;
import com.hytaleeco.plugin.util.CommandRegistrar;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Path dataDir = Paths.get(System.getProperty("user.dir"), "plugins-data", "EconomyPlugin");
        try {
            Files.createDirectories(dataDir);
            logger.info("Data dir: " + dataDir.toAbsolutePath());
        } catch (Exception ex) {
            logger.warning("Failed to create data directory: " + ex.getMessage());
        }
        this.economyService = new EconomyService(logger, dataDir);
        CommandRegistrar registrar = new CommandRegistrar(init, logger);
        logger.info("Command registration mechanism: " + registrar.getMechanismDescription());
        logger.info("Registering commands: bal, pay, baltop, eco");
        registerCommand(registrar, logger, "/bal", new BalCommand("bal", "Show your balance", economyService));
        registerCommand(registrar, logger, "/pay", new PayCommand("pay", "Pay another player", economyService));
        registerCommand(registrar, logger, "/baltop", new BaltopCommand("baltop", "Show top balances", economyService));
        registerCommand(registrar, logger, "/eco", new EcoCommand("eco", "Economy admin commands", economyService));
        logger.info("Registered /eco give");
        logger.info("Registered /eco set");
        logger.info("EconomyPlugin setup complete");
    }

    private void registerCommand(CommandRegistrar registrar, Logger logger, String name, Object command) {
        String error = registrar.registerCommand(command);
        if (error == null) {
            logger.info("Registered " + name);
        } else {
            logger.warning("FAILED to register " + name + ": " + error);
        }
    }

}
