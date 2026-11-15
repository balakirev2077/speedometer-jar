package com.example.speedometer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = SpeedometerMod.MODID,
        name = "Speedometer",
        version = "1.0",
        clientSideOnly = true,
        acceptedMinecraftVersions = "[1.12.2]")
public class SpeedometerMod {
    public static final String MODID = "speedometer";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        SpeedHandler handler = new SpeedHandler();
        handler.registerKeys();
        MinecraftForge.EVENT_BUS.register(handler);
    }
}
