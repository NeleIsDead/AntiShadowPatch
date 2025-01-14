package ru.nern.antishadowpatch.mixin.block.entity.obtain;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.SculkSensorBlock;
import net.minecraft.block.enums.SculkSensorPhase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SculkSensorBlock.class)
public abstract class SculkSensorBlockMixin extends BlockWithEntity {

    @Shadow
    public static SculkSensorPhase getPhase(BlockState state) {
        return null;
    }

    @Shadow
    private static void updateNeighbors(World world, BlockPos pos, BlockState state) {
    }

    protected SculkSensorBlockMixin(Settings settings) {
        super(settings);
    }

    @Redirect(
            method = "onStateReplaced",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/SculkSensorBlock;updateNeighbors(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V")
    )
    private void antishadowpatch$disableComparatorUpdateAfter(World world, BlockPos pos, BlockState state) {}

    @Inject(method = "onStateReplaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockWithEntity;onStateReplaced(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V"))
    private void antishadowpatch$moveComparatorUpdateBefore(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
        if(getPhase(state) == SculkSensorPhase.ACTIVE) {
            updateNeighbors(world, pos, state);
        }
    }
}
