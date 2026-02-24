package net.goo.brutality.common.registry;

import com.google.common.collect.Maps;
import net.goo.brutality.util.ColorUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;

public final class BrutalityRarities {

    public static final Rarity LEGENDARY;
    public static final Rarity FABLED;
    public static final Rarity MYTHIC;
    public static final Rarity DIVINE;
    public static final Rarity CATACLYSMIC;
    public static final Rarity GODLY;
    public static final Rarity DARK;
    public static final Rarity GLOOMY;
    public static final Rarity GLACIAL;
    public static final Rarity PRISMATIC;
    public static final Rarity FIRE;
    public static final Rarity STYGIAN;
    public static final Rarity CONDUCTIVE;
    public static final Rarity NOCTURNAL;
    public static final Rarity NULL;

    public static final Map<Rarity, ColorUtils.ColorData> BY_RARITY = Maps.newIdentityHashMap();

    static {
        LEGENDARY = create("legendary", ColorUtils.ColorData.LEGENDARY);
        FABLED = create("fabled", ColorUtils.ColorData.FABLED);
        MYTHIC = create("mythic", ColorUtils.ColorData.MYTHIC);
        DIVINE = create("divine", ColorUtils.ColorData.DIVINE);
        CATACLYSMIC = create("cataclysmic", ColorUtils.ColorData.CATACLYSMIC);
        GODLY = create("godly", ColorUtils.ColorData.GODLY);
        DARK = create("dark", ColorUtils.ColorData.DARK);
        GLOOMY = create("gloomy", ColorUtils.ColorData.GLOOMY);
        GLACIAL = create("glacial", ColorUtils.ColorData.GLACIAL);
        PRISMATIC = create("prismatic", ColorUtils.ColorData.PRISMATIC);
        FIRE = create("fire", ColorUtils.ColorData.FIRE);
        STYGIAN = create("stygian", ColorUtils.ColorData.STYGIAN);
        NOCTURNAL = create("nocturnal", ColorUtils.ColorData.NOCTURNAL);
        CONDUCTIVE = create("conductive", ColorUtils.ColorData.CONDUCTIVE);
        NULL = create("null", ColorUtils.ColorData.CONDUCTIVE);

    }

    private static Rarity create(String name, ColorUtils.ColorData data) {
        Rarity rarity = Rarity.create(name.toUpperCase(Locale.ROOT), style -> {
            Style s = Style.EMPTY.withColor(data.colors[0]);
            return data.bold ? s.withBold(true) : s;
        });
        BY_RARITY.put(rarity, data);
        return rarity;
    }

    public static @Nullable ColorUtils.ColorData from(Rarity rarity) {
        return BY_RARITY.get(rarity);
    }

    public static @Nullable ColorUtils.ColorData from(ItemStack stack) {
        if (stack.isEmpty()) return null;
        return from(stack.getRarity());
    }

}