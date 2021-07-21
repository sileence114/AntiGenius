package pub.silence.antigenius.mixins.event;

import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pub.silence.antigenius.events.BlockDestroy;
import pub.silence.antigenius.events.LoggedEvent;
import pub.silence.antigenius.events.operate.PlayerOperator;

@Mixin(Block.class)
public abstract class Block_onBreak {
    
    @Inject(method = "onBreak", at = @At("RETURN"))
    private void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
        LoggedEvent.onEvent(new BlockDestroy(
            Registry.BLOCK.getId(state.getBlock()).toString(),
            Objects.requireNonNull(DimensionType.getId(world.dimension.getType())).toString(),
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            new PlayerOperator(player.getGameProfile().getName(), player.getGameProfile().getId().toString())
        ));
    }

}
