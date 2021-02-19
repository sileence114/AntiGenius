package pub.silence.antigenius;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pub.silence.antigenius.config.Config;
import pub.silence.antigenius.handler.ServerLife;
import pub.silence.antigenius.lang.Language;


public class AntiGenius implements ModInitializer, DedicatedServerModInitializer {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static boolean debug = true;
    private static AntiGenius instance;
    
    private ModContainer container;
    private Path workingDir;
    
    public AntiGenius() {
        if (instance == null) {
            instance = this;
        }
    }
    
    public static AntiGenius getInstance() {
        return instance;
    }
    
    
    @Override
    public void onInitialize() {
        container = FabricLoader.getInstance()
                                .getModContainer("antigenius")
                                .orElseThrow(() -> new IllegalStateException("AntiGenius Mod Missing."));
        workingDir = FabricLoader.getInstance().getConfigDir().resolve("antigenius");
        if (!Files.exists(workingDir)) {
            try {
                Files.createDirectory(workingDir);
            }
            catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        Language.initialize();
        Config.initialize();
        ServerLifecycleEvents.SERVER_STARTING.register(ServerLife.getInstance()::onServerStarting);
        ServerLifecycleEvents.SERVER_STOPPING.register(ServerLife.getInstance()::onServerStopping);
    }
    
    @Override
    public void onInitializeServer() {
        error("On initialize server. Stopping for testing mod initialize.");
        System.exit(114514);
    }
    
    public String getInternalVersion() {
        return container.getMetadata().getVersion().getFriendlyString();
    }
    
    public Path getWorkingDir() {
        return this.workingDir;
    }
    
    /**
     * If print debug info is needed, I recommend that you can add `-Dfabric.log.level=debug` to VM
     * arguments. `debug: true` in config will pull debug-level to info-level.
     *
     * @param msg Message
     * @param obj Other objects.
     */
    public static void debug(String msg, Object... obj) {
        if (debug) {
            LOGGER.info("[Debug] " + msg);
            if (obj.length != 0) {
                LOGGER.info(obj);
            }
        }
        else {
            LOGGER.debug(msg);
            if (obj.length != 0) {
                LOGGER.debug(obj);
            }
        }
    }
    
    public static void info(String msg, Object... obj) {
        LOGGER.info(msg);
        if (obj.length != 0) {
            LOGGER.info(obj);
        }
    }
    
    public static void warn(String msg, Object... obj) {
        LOGGER.warn(msg);
        if (obj.length != 0) {
            LOGGER.warn(obj);
        }
    }
    
    public static void error(String msg, Object... obj) {
        LOGGER.error(msg);
        if (obj.length != 0) {
            LOGGER.error(obj);
        }
    }
}