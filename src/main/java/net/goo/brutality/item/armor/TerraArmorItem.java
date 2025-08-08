package net.goo.brutality.item.armor;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.item.base.BrutalityArmorItem;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class TerraArmorItem extends BrutalityArmorItem {


    public TerraArmorItem(ArmorMaterial pMaterial, Type pType, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pMaterial, pType, rarity, descriptionComponents);
    }



    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level().isClientSide()) {
            Player player = event.player;
            if (ModUtils.hasFullArmorSet(event.player, BrutalityArmorMaterials.TERRA)) {
                if (player.isCrouching()) {
                        event.player.addEffect(new MobEffectInstance(BrutalityModMobEffects.STONEFORM.get(), 20, 0, false, true));
                } else {
                    event.player.removeEffect(BrutalityModMobEffects.STONEFORM.get());
                }
            }
        }
    }



    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
}
