/*
 * Copyright (c) 2022. Created by @HerraVp (https://github.com/HerraVp)
 */

package me.vp.wtf;

import net.fabricmc.api.ClientModInitializer;

public class Wtf implements ClientModInitializer {

    public static final String version = "1.0";
    public static final String client = "WTF";

    @Override
    public void onInitializeClient() {
        System.out.println(client + " " + version + " loaded!");
    }
}