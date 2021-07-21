package pub.silence.antigenius.platform;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import pub.silence.antigenius.AntiGenius;


public class AntiGeniusFabric implements AntiGenius, DedicatedServerModInitializer{
    
    private ModContainer container;
    private Path workingDir;
    
    public AntiGeniusFabric() {
        if (InstanceManger.instance == null) {
            InstanceManger.instance = this;
        }
    }
    
    @Override
    public void onInitializeServer() {
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
        
        this.onLoad();
    }
    
    @Override
    public String getAntiGeniusVersion() {
        return this.container.getMetadata().getVersion().toString();
    }
    @Override
    public String getPlatFormName() {
        return "Fabric - FabricLoader";
    }
    @Override
    public String getPlatFormVersion() { // FabricLoader
        return FabricLoader.getInstance().getModContainer("fabricloader").orElseThrow(
            () -> new IllegalStateException("Fabric Loader Missing.")
        ).getMetadata().getVersion().toString();
    }
    @Override
    public String gatGameVersion() {
        return FabricLoader.getInstance().getModContainer("minecraft").orElseThrow(
            () -> new IllegalStateException("Minecraft Missing.")
        ).getMetadata().getVersion().toString();
    }
    @Override
    public Path getWorkingDir() {
        return this.workingDir;
    }
}