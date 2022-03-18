/*
 * Copyright (c) 2022. Created by @HerraVp (https://github.com/HerraVp)
 */

package me.vp.wtf.mixins.accessor;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface AccessorMinecraftClient {

    @Accessor("currentFps")
    static int getFps() {
        return 0;
    }
}
