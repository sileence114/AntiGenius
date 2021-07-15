package pub.silence.antigenius.mixins.base;

import com.google.gson.JsonElement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pub.silence.antigenius.AntiGenius;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServer_onInitializeWorld extends ReentrantThreadExecutor<ServerTask> {
    public MinecraftServer_onInitializeWorld(String name) {
        super(name);
    }
    
    @Inject(method = "loadWorld", at = @At("HEAD"))
    private void onInitializeWorld(String name, String serverName, long seed, LevelGeneratorType generatorType, JsonElement generatorSettings, CallbackInfo cb) {
        AntiGenius.LOGGER.debug("Initialize World.");
        AntiGenius.getInstance().onInitializeWorld();
        
    }
}
