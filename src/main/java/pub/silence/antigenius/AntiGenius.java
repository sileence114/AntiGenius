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
    private static boolean debug = false;
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

        ServerLifecycleEvents.SERVER_STARTING.register(ServerLife.getInstance()::onServerStarting);
        ServerLifecycleEvents.SERVER_STOPPING.register(ServerLife.getInstance()::onServerStopping);

    }

    @Override
    public void onInitializeServer() {
        info("onInitializeServer");
    }

    public String getInternalVersion() {
        return this.container.getMetadata().getVersion().getFriendlyString();
    }

    public Path getWorkingDir() {
        return this.workingDir;
    }

    /**
     * If print debug info is needed, I recommend that you can add `-Dfabric.log.level=debug` to VM arguments.
     * `debug: true` in config will pull debug-level to info-level.
     *
     * @param message messages
     */
    public static void debug(Object... message) {
        if (debug) {
            LOGGER.debug(message);
        } else {
            LOGGER.info("[Debug] ", message);
        }

    }

    public static void info(Object... message) {
        LOGGER.info(message);
    }

    public static void warn(Object... message) {
        LOGGER.warn(message);
    }

    public static void error(Object... message) {
        LOGGER.error(message);
    }
}
