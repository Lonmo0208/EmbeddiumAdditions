package me.srrapero720.embeddium.additions.mixins.impl.fpsdisplay;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.srrapero720.embeddium.additions.util.EAConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.util.profiling.metrics.profiling.MetricsRecorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MinecraftGPUMixin {
    @Shadow @Nullable public MultiPlayerGameMode gameMode;

    @WrapOperation(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/metrics/profiling/MetricsRecorder;isRecording()Z"))
    private boolean redirect$isRecording(MetricsRecorder instance, Operation<Boolean> original) {
        return gameMode == null ? original.call(instance) : EAConfig.fpsTextMode.get() != EAConfig.FPSDisplayText.NONE || original.call(instance);
    }
}