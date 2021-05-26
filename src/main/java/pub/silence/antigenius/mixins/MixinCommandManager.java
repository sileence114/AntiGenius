package pub.silence.antigenius.mixins;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pub.silence.antigenius.command.AntigeniusCommand;

@Mixin(CommandManager.class)
public abstract class MixinCommandManager{
    @Shadow @Final
    private CommandDispatcher<ServerCommandSource> dispatcher;
    
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onRegister(CallbackInfo callbackInfo) {
//        SharedConstants.isDevelopment = true;
        AntigeniusCommand.register(dispatcher);
    }
}