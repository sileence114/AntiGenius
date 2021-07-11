package pub.silence.antigenius;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pub.silence.antigenius.command.AntigeniusCommand;
import pub.silence.antigenius.config.Config;
import pub.silence.antigenius.config.Language;


public class AntiGenius implements ModInitializer, DedicatedServerModInitializer{
    
    public static final Logger LOGGER = LogManager.getLogger(AntiGenius.class);
    private static AntiGenius instance;
    
    private ModContainer container;
    private Path workingDir;
    
    public AntiGenius() {
        if (instance == null) {
            instance = this;
        }
    }
    public static AntiGenius getInstance() {
        if(instance == null){
            new AntiGenius();
        }
        return instance;
    }
    
    
    @Override
    public void onInitialize() {
        this.container = FabricLoader.getInstance().getModContainer("antigenius").orElseThrow(
            () -> new IllegalStateException("AntiGenius Mod Missing.")
        );
        this.workingDir = FabricLoader.getInstance().getConfigDir().resolve("antigenius");
        if (!Files.exists(this.workingDir)) {
            try {
                Files.createDirectory(this.workingDir);
            }
            catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        LOGGER.info("AntiGenius v" + this.getInternalVersion() + " is loading.");
    }
    
    @Override
    public void onInitializeServer() {
        // Initialize config and language
        Language.initialize();
        Config.initialize();
        Language.setLanguage(Config.getString("language"));
        
        // Register Command -> Fabric-Command-API
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            if(dedicated) {
                AntigeniusCommand.register(dispatcher);
                LOGGER.debug("Command registration Completed.");
            }
        });
    }
    
    public String getInternalVersion() {
        return this.container.getMetadata().getVersion().toString();
    }
    
    public Path getWorkingDir() {
        return this.workingDir;
    }
}