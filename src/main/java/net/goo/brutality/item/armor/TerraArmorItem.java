package net.goo.brutality.item.armor;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.armor.ArmaAutoGlowingArmorRenderer;
import net.goo.brutality.item.base.BrutalityArmorItem;
import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.registry.ModMobEffects;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class TerraArmorItem extends BrutalityArmorItem {

    public TerraArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pMaterial, pType, pProperties, identifier, rarity, descriptionComponents);
    }

    @Override
    public <R extends GeoArmorRenderer<?>> void initGeoArmor(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeoArmor(consumer, ArmaAutoGlowingArmorRenderer.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level().isClientSide()) {
            Player player = event.player;
            if (ModUtils.hasFullArmorSet(event.player, BrutalityArmorMaterials.TERRA)) {
                if (player.isCrouching()) {
                        event.player.addEffect(new MobEffectInstance(ModMobEffects.STONEFORM.get(), 20, 0, false, true));
                } else {
                    event.player.removeEffect(ModMobEffects.STONEFORM.get());
                }
            }
        }
    }



    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
}
