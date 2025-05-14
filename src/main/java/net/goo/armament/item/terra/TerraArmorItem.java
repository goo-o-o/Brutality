package net.goo.armament.item.terra;

import net.goo.armament.Armament;
import net.goo.armament.client.renderers.armor.ArmaAutoGlowingArmorRenderer;
import net.goo.armament.item.ArmaArmorItem;
import net.goo.armament.item.ModArmorMaterials;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.registry.ModEffects;
import net.goo.armament.util.ModUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID)
public class TerraArmorItem extends ArmaArmorItem {
    public TerraArmorItem(Type pType, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(ModArmorMaterials.TERRA, pType, pProperties, identifier, category, rarity, abilityCount);
    }

    @Override
    public <R extends GeoArmorRenderer<?>> void initGeoArmor(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeoArmor(consumer, ArmaAutoGlowingArmorRenderer.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level().isClientSide()) {
            Player player = event.player;
            if (ModUtils.hasFullArmorSet(event.player, ModArmorMaterials.TERRA)) {
                if (player.isCrouching()) {
                        event.player.addEffect(new MobEffectInstance(ModEffects.STONEFORM.get(), 20, 0, false, true));
                } else {
                    event.player.removeEffect(ModEffects.STONEFORM.get());
                }
            }
        }
    }



    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
}
