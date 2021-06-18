package pub.silence.antigenius;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pub.silence.antigenius.config.Config;
import pub.silence.antigenius.config.Language;


public class AntiGenius implements ModInitializer, DedicatedServerModInitializer {
    
    private static final Logger LOGGER = LogManager.getLogger(AntiGenius.class);
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
        container = FabricLoader.getInstance().getModContainer("antigenius").orElseThrow(
            () -> new IllegalStateException("AntiGenius Mod Missing.")
        );
        workingDir = FabricLoader.getInstance().getConfigDir().resolve("antigenius");
        if (!Files.exists(workingDir)) {
            try {
                Files.createDirectory(workingDir);
            }
            catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
    
    @Override
    public void onInitializeServer() {
        Language.initialize();
        Config.initialize();
//        logger().error("On initialize server. Stopping for testing mod initialize.");
//        System.exit(114514);
    }
    
    public String getInternalVersion() {
        return container.getMetadata().getVersion().getFriendlyString();
    }
    
    public Path getWorkingDir() {
        return this.workingDir;
    }
    
    public static Logger logger(){
        return LOGGER;
    }
}