/*
 * Copyright (c) 2022. Created by @HerraVp (https://github.com/HerraVp)
 */

package me.vp.wtf.mixins;

import me.vp.wtf.Wtf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Shadow
    private boolean windowFocused;

    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    public void getFramerateLimit(CallbackInfoReturnable<Integer> cir) {

        if (!this.windowFocused) {
            cir.setReturnValue(1);
        }
    }


    @Inject(method = "getWindowTitle", at = @At("RETURN"), cancellable = true)
    public void getWindowTitle(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(cir.getReturnValue() + " with " + Wtf.client + " " + Wtf.version);
    }
}
