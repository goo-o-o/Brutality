package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.client.item.ArmaGeoGlowingWeaponRenderer;
import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaSwordItem;
import net.goo.armament.registry.ModItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.function.Consumer;

import static net.goo.armament.util.ModResources.TRUTHSEEKER_COLORS;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TruthseekerSword extends ArmaSwordItem {
    public TruthseekerSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
        this.colors = TRUTHSEEKER_COLORS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public <T extends Item & ArmaGeoItem, R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, ArmaGeoGlowingWeaponRenderer.class);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onXPChange(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();

        final float EXPERIENCE_BOOST = 2.0F;
        if (player.getMainHandItem().getItem() == ModItems.TRUTHSEEKER_SWORD.get()) {
            int originalXP = event.getAmount();
            int boostedXP = (int) (originalXP * EXPERIENCE_BOOST);
            event.setAmount(boostedXP);
        }
    }

}
