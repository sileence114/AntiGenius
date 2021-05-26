package pub.silence.antigenius.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.container.ContainerListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity implements ContainerListener {
    @Shadow
    private String clientLanguage;
    
    public MixinServerPlayerEntity(World world, GameProfile profile) {
        super(world, profile);
    }
    /*
     Try to get Client Language from ServerPlayerEntity.clientLanguage,
     But it always returns "en_US"
    */
    @Inject(method = "<init>", at = @At("RETURN"))
    public void getClientLanguage(CallbackInfo callbackInfo){
//        AntiGenius.info("Client Language: " + clientLanguage);
    }
}
