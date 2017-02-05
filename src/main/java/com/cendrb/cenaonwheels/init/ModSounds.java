package com.cendrb.cenaonwheels.init;

import com.cendrb.cenaonwheels.RefStrings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by cendr on 27/01/2017.
 */
public class ModSounds {

    public static SoundEvent heimSong;
    public static SoundEvent heimRiot;
    public static SoundEvent heimArmed;
    public static SoundEvent heimExplosion;
    public static SoundEvent psim;
    public static SoundEvent surprise;
    public static SoundEvent gei;
    public static SoundEvent adhan;
    public static SoundEvent kana;
    public static SoundEvent klid;
    public static SoundEvent nadavat;
    public static SoundEvent nesmysl;
    public static SoundEvent saka;
    public static SoundEvent whoa;
    public static SoundEvent jes;

    private static int size = 0;

    public static void init()
    {
        size = SoundEvent.REGISTRY.getKeys().size();

        heimSong = loadSound("heimSong");
        heimRiot = loadSound("heimRiot");
        heimArmed = loadSound("heimArmed");
        heimExplosion = loadSound("heimExplosion");
        psim = loadSound("psim");
        surprise = loadSound("surprise");
        gei = loadSound("gei");
        adhan = loadSound("adhan");
        kana = loadSound("kana");
        klid = loadSound("klid");
        nadavat = loadSound("nadavat");
        nesmysl = loadSound("nesmysl");
        saka = loadSound("saka");
        whoa = loadSound("whoa");
        jes = loadSound("jes");
    }

    private static SoundEvent loadSound(String name)
    {
        final ResourceLocation resourceLocation = new ResourceLocation(RefStrings.MODID, name);
        SoundEvent soundEvent = new SoundEvent(resourceLocation);
        SoundEvent.REGISTRY.register(size, resourceLocation, soundEvent);
        size++;
        return soundEvent;
    }
}
