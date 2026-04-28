package net.goo.brutality.common.item.generic.coins;

import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Paradime extends BrutalityCoinItem {


    public Paradime(Properties pProperties, int cooldownTime, List<ItemDescriptionComponent> descriptionComponents) {
        super(pProperties, cooldownTime, descriptionComponents);
    }

    @Override
    protected float getBasePixelDiameter() {
        return 10;
    }

    @Override
    public void onHeads(Player player, ItemStack stack, Vec3 location) {
        playBuffSounds(player);
        player.addEffect(new MobEffectInstance(TerramityModMobEffects.AMPED.get(), 100, 0));
    }

    @Override
    public void onTails(Player player, ItemStack stack, Vec3 location) {
        playDebuffSounds(player);
        player.addEffect(new MobEffectInstance(TerramityModMobEffects.VULNERABLE.get(), 60, 0));
    }

}
