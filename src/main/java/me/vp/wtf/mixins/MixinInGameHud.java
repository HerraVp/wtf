/*
 * Copyright (c) 2022. Created by @HerraVp (https://github.com/HerraVp)
 */

package me.vp.wtf.mixins;

import me.vp.wtf.mixins.accessor.AccessorMinecraftClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Inject(at = @At(value = "RETURN"), method = "render", cancellable = true)
    public void render(MatrixStack matrices, float float_1, CallbackInfo info) {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player == null || mc.world == null) return;

        // Durability
        if (!mc.player.getMainHandStack().isEmpty() && mc.player.getMainHandStack().isDamageable()) {
            int durability = (mc.player.getMainHandStack().getMaxDamage() - mc.player.getMainHandStack().getDamage());

            DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer,
                        Formatting.GREEN + "Durability: " + Formatting.RESET + durability,
                        1,
                        150,
                        0xFFFFFF);
        }

        // FPS
        int fps = Integer.parseInt(Integer.toString(AccessorMinecraftClient.getFps()));
        DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer,
                Formatting.GREEN + "FPS: " + Formatting.RESET + fps, 1,
                1,
                0xFFFFFF);

        // Ping
        PlayerListEntry playerEntry = mc.player.networkHandler.getPlayerListEntry(mc.player.getGameProfile().getId());
        int ping = playerEntry == null ? 0 : playerEntry.getLatency();
        DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer,
                Formatting.GREEN + "Ping: " + Formatting.RESET + ping + "ms", 1,
                1 + mc.textRenderer.fontHeight + 1,
                0xFFFFFF);

        // Coordinates
        final DecimalFormat decimalFormat = new DecimalFormat("###.#");
        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();

        if (mc.world.getDimension().isRespawnAnchorWorking()) {
            x *= 8;
            z *= 8;
        }

        final String overWorld = "XYZ: " + Formatting.GREEN + decimalFormat.format(x) + " " + decimalFormat.format(y) + " " + decimalFormat.format(z);
        final String nether = decimalFormat.format(x / 8) + " " + decimalFormat.format(y) + " " + decimalFormat.format(z / 8);
        DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer, overWorld + Formatting.RESET + " | " + Formatting.RED + nether, 1, mc.getWindow().getScaledHeight() - 10, 0xFFFFFF);
    }
}
