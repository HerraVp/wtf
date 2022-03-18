package me.vp.wtf;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class Wtf implements ClientModInitializer {

    public static final String version = "1.0";
    public static final String client = "WTF";

    @Override
    public void onInitializeClient() {
        System.out.println(client + " " + version + " loaded!");
    }
}