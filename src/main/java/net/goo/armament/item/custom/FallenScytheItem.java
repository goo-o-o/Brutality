package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaScytheItem;
import net.goo.armament.util.ModResources;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import static net.goo.armament.util.ModUtils.replaceOrAddModifier;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FallenScytheItem extends ArmaScytheItem {
    public static String SOULS_HARVESTED = "souls_harvested";

    public FallenScytheItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
        this.colors = ModResources.SUPERNOVA_COLORS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        // Check if the damage source is a player
        if (event.getSource().getEntity() instanceof Player player) {
            // Check if the player is holding the custom trident
            ItemStack mainHandItem = player.getMainHandItem();
            if (mainHandItem.getItem() instanceof FallenScytheItem) {
                // Increment the souls harvested counter
                int soulsHarvested = mainHandItem.getOrCreateTag().getInt(SOULS_HARVESTED);
                mainHandItem.getOrCreateTag().putInt(SOULS_HARVESTED, soulsHarvested + 1);

                // Play effects
                if (player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SOUL, event.getEntity().getX(), event.getEntity().getY() + event.getEntity().getBbHeight() / 2, event.getEntity().getZ(), 2, 0.25, 0.25, 0.25, 0);
                    serverLevel.playSound(null, event.getEntity().getOnPos(), SoundEvents.SOUL_ESCAPE, SoundSource.HOSTILE, 4F, 1F);
                }

                // Update attack damage and speed
                double newAttackDamage = Math.min((soulsHarvested + 1) * 0.03, 18);

                replaceOrAddModifier(mainHandItem, Attributes.ATTACK_DAMAGE, BASE_ATTACK_DAMAGE_UUID, newAttackDamage, EquipmentSlot.MAINHAND);
            }
        }
    }

}

