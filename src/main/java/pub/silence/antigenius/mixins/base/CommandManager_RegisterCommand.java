package pub.silence.antigenius.mixins.base;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pub.silence.antigenius.AntiGenius;
import pub.silence.antigenius.command.AntigeniusCommand;

@Mixin(CommandManager.class)
public abstract class CommandManager_RegisterCommand {
    @Shadow @Final
    private CommandDispatcher<ServerCommandSource> dispatcher;
    
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onRegister(boolean isDedicatedServer, CallbackInfo ci) {
        AntigeniusCommand.register(dispatcher);
        AntiGenius.LOGGER.debug("Command registration Completed.");
    }
}
