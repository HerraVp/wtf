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
import net.minecraft.entity.Entity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

        // TPS
        String tps = String.valueOf(getAverageTPS());

        DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer,
                Formatting.GREEN + "TPS: " + Formatting.RESET + tps, 1,
                1 + mc.textRenderer.fontHeight + 1,
                0xFFFFFF);

        // Ping
        PlayerListEntry playerEntry = mc.player.networkHandler.getPlayerListEntry(mc.player.getGameProfile().getId());
        int ping = playerEntry == null ? 0 : playerEntry.getLatency();
        DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer,
                Formatting.GREEN + "Ping: " + Formatting.RESET + ping + "ms", 1,
                10 + mc.textRenderer.fontHeight + 1,
                0xFFFFFF);

        // Players
        DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer,
                Formatting.GREEN + "Players Nearby: ", 1,
                180 + mc.textRenderer.fontHeight + 1,
                0xFFFFFF);

        int count = 1;
        for (Entity e : mc.world.getPlayers().stream()
                .filter(e -> e != mc.player)
                .sorted(Comparator.comparing(mc.player::distanceTo)).toList()) {
            int dist = Math.round(mc.player.distanceTo(e));

            String text =
                    e.getDisplayName().getString()
                            + " \u00a77|\u00a7r " +
                            e.getBlockPos().getX() + " " + e.getBlockPos().getY() + " " + e.getBlockPos().getZ()
                            + " (" + dist + "m)";

            int playerColor = 0xff000000 |
                    ((255 - (int) Math.min(dist * 2.1, 255) & 0xFF) << 16) |
                    (((int) Math.min(dist * 4.28, 255) & 0xFF) << 8);

            DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer,
                    Formatting.GREEN + text, 1,
                    190 + count * 10, playerColor);
            count++;
        }



        /* // Biome
        String biome = mc.world.getBiome(mc.player.getBlockPos()).getKey().toString().toUpperCase();

        DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer,
                Formatting.GREEN + "Biome: " + Formatting.RESET + biome, 1,
                30,
                0xFFFFFF);
        */

        // BPS
        DecimalFormat bpsFormat = new DecimalFormat("0.0");
        final double deltaX = Math.abs(mc.player.getPos().getX() - mc.player.prevX);
        final double deltaZ = Math.abs(mc.player.getPos().getZ() - mc.player.prevZ);
        String bps = bpsFormat.format((deltaX + deltaZ) * 20);

        DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer,
                Formatting.GREEN + "BPS: " + Formatting.RESET + bps, 1, 485,
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

        final String overWorld = Formatting.GREEN + "XYZ: " + decimalFormat.format(x) + " " + decimalFormat.format(y) + " " + decimalFormat.format(z);
        final String nether = decimalFormat.format(x / 8) + " " + decimalFormat.format(y) + " " + decimalFormat.format(z / 8);
        DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer, overWorld + Formatting.RESET + " | " + Formatting.RED + nether, 1, mc.getWindow().getScaledHeight() - 10, 0xFFFFFF);
       // DrawableHelper.fill(matrices, mc.textRenderer.getWidth(overWorld + Formatting.RESET + " | " + Formatting.RED + nether), mc.textRenderer.fontHeight + 10, mc.textRenderer.getWidth(overWorld + Formatting.RESET + " | " + Formatting.RED + nether), mc.textRenderer.fontHeight + 10, 0x70003030);

        // Sprinting info
        String movementInfo = "";
        if (mc.player.isSprinting() || mc.options.sprintKey.isPressed()) {
            movementInfo = "You are currently sprinting";
            DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer, Formatting.GREEN + movementInfo, mc.getWindow().getScaledWidth() - mc.textRenderer.getWidth(movementInfo), mc.getWindow().getScaledHeight() - 10, 0xFFFFFF);
        }
        else if (mc.player.getAbilities().flying || mc.player.isFallFlying()) {
            movementInfo = "You are currently flying";
            DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer, Formatting.GREEN + movementInfo, mc.getWindow().getScaledWidth() - mc.textRenderer.getWidth(movementInfo), mc.getWindow().getScaledHeight() - 10, 0xFFFFFF);
        }
        else if (mc.player.isSneaking() || mc.player.isInSneakingPose()) {
            movementInfo = "You are currently sneaking";
            DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer, Formatting.GREEN + movementInfo, mc.getWindow().getScaledWidth() - mc.textRenderer.getWidth(movementInfo), mc.getWindow().getScaledHeight() - 10, 0xFFFFFF);
        }
        else {
            movementInfo = "";
            DrawableHelper.drawStringWithShadow(matrices, mc.textRenderer, Formatting.GREEN + movementInfo, mc.getWindow().getScaledWidth() - mc.textRenderer.getWidth(movementInfo), mc.getWindow().getScaledHeight() - 10, 0xFFFFFF);
        }
    }

    private List<Long> reports = new ArrayList<>();

    public double getTPS(int averageOfSeconds) {
        if (reports.size() < 2) {
            return 20.0; // we can't compare yet
        }

        long currentTimeMS = reports.get(reports.size() - 1);
        long previousTimeMS = reports.get(reports.size() - averageOfSeconds);

        // on average, how long did it take for 20 ticks to execute? (ideal value: 1 second)
        double longTickTime = Math.max((currentTimeMS - previousTimeMS) / (1000.0 * (averageOfSeconds - 1)), 1.0);
        return 20 / longTickTime;
    }

    public double getAverageTPS() {
        return getTPS(reports.size());
    }
}
