package pub.silence.antigenius;

import pub.silence.antigenius.config.Config;
import pub.silence.antigenius.handler.ServerLife;
import pub.silence.antigenius.lang.Language;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Files;


public class AntiGenius implements ModInitializer, DedicatedServerModInitializer {
    private static final Logger LOGGER = LogManager.getLogger();
    private static AntiGenius instance;

    private ModContainer container;
    private Path workingDir;

    public AntiGenius(){
        if(instance == null){
            instance = this;
        }
    }
    public static AntiGenius getInstance(){
        return instance;
    }


    @Override
    public void onInitialize() {
        this.container = FabricLoader.getInstance().getModContainer("antigenius").orElseThrow(
            () -> new IllegalStateException("AntiGenius Mod Missing.")
        );
        info("AntiGenius mod version: " + this.getInternalVersion());
        workingDir = FabricLoader.getInstance().getConfigDir().resolve("antigenius");
        if (!Files.exists(workingDir)) {
            try {
                Files.createDirectory(workingDir);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        info("Working direction: " + this.workingDir);
        Language.initialize();

        Config.initialize();
        ServerLifecycleEvents.SERVER_STARTING.register(ServerLife.getInstance()::onServerStarting);
        ServerLifecycleEvents.SERVER_STOPPING.register(ServerLife.getInstance()::onServerStopping);

    }

    @Override
    public void onInitializeServer() {
        info("");
    }


    public String getInternalVersion() {
        return this.container.getMetadata().getVersion().getFriendlyString();
    }

    public Path getWorkingDir(){
        return this.workingDir;
    }



    public static void debug(String message){
        LOGGER.info("[Debug] " + message);
//        LOGGER.debug(message);
    }
    public static void info(String message){
        LOGGER.info(message);
    }
    public static void warn(String message) {
        LOGGER.warn(message);
    }
    public static void warn(String message, Exception e){
        LOGGER.warn(message, e);
    }
    public static void error(String message){
        LOGGER.error(message);
    }
    public static void error(String message, Exception e){
        LOGGER.error(message, e);
    }
}
