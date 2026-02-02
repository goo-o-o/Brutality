package net.goo.brutality.event.mod;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.entity.mobs.SummonedStray;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityModEvents {

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(BrutalityEntities.SUMMONED_STRAY.get(), SummonedStray.createAttributes().build());
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeModificationEvent event) {

        // --- Attributes that should be given to ALL Living Entities ---

        // 1. Define the attributes that are generic (Friction, Damage Taken, etc.)
        Set<Attribute> genericAttributes = Set.of(
                BrutalityAttributes.AIR_FRICTION.get(),
                BrutalityAttributes.GROUND_FRICTION.get(),
                BrutalityAttributes.DAMAGE_TAKEN.get(),
                BrutalityAttributes.BLUNT_DAMAGE.get(),
                BrutalityAttributes.PIERCING_DAMAGE.get(),
                BrutalityAttributes.SLASH_DAMAGE.get(),
                BrutalityAttributes.AXE_DAMAGE.get(),
                BrutalityAttributes.SWORD_DAMAGE.get(),
                BrutalityAttributes.HAMMER_DAMAGE.get(),
                BrutalityAttributes.SPEAR_DAMAGE.get(),
                BrutalityAttributes.SCYTHE_DAMAGE.get()
        );

        // 2. Loop through all entity types that can be modified
        for (EntityType<? extends LivingEntity> entityType : event.getTypes()) {

            // Add all generic attributes to this entity type
            for (Attribute attribute : genericAttributes) {
                // Check if the attribute is already present (optional, but good practice)
                if (!event.has(entityType, attribute)) {
                    event.add(entityType, attribute); // Uses the default value
                }
            }
        }

        // --- Attributes for Players Only ---

        // 3. Define the attributes that are player-specific (Mana, Spell Levels, etc.)
        Set<Attribute> playerOnlyAttributes = Set.of(
                BrutalityAttributes.RAGE_TIME.get(),
                BrutalityAttributes.LIFESTEAL.get(),
                BrutalityAttributes.OMNIVAMP.get(),
                BrutalityAttributes.RAGE_LEVEL.get(),
                BrutalityAttributes.MAX_RAGE.get(),
                BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get(),
                BrutalityAttributes.MANA_COST.get(),
                BrutalityAttributes.MANA_REGEN.get(),
                BrutalityAttributes.MAX_MANA.get(),
                BrutalityAttributes.MAX_BLOOD.get(),
                BrutalityAttributes.SPELL_COOLDOWN.get(),
                BrutalityAttributes.CAST_TIME.get(),
                BrutalityAttributes.SPELL_DAMAGE.get(),
                BrutalityAttributes.DAEMONIC_SCHOOL_LEVEL.get(),
                BrutalityAttributes.DARKIST_SCHOOL_LEVEL.get(),
                BrutalityAttributes.VOLTWEAVER_SCHOOL_LEVEL.get(),
                BrutalityAttributes.CELESTIA_SCHOOL_LEVEL.get(),
                BrutalityAttributes.UMBRANCY_SCHOOL_LEVEL.get(),
                BrutalityAttributes.EVERGREEN_SCHOOL_LEVEL.get(),
                BrutalityAttributes.DODGE_CHANCE.get(),
                BrutalityAttributes.EXODIC_SCHOOL_LEVEL.get(),
                BrutalityAttributes.COSMIC_SCHOOL_LEVEL.get(),
                BrutalityAttributes.BRIMWIELDER_SCHOOL_LEVEL.get(),
                BrutalityAttributes.VOIDWALKER_SCHOOL_LEVEL.get(),
                BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(),
                BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get(),
                BrutalityAttributes.JUMP_HEIGHT.get(),
                BrutalityAttributes.STUN_CHANCE.get(),
                BrutalityAttributes.STUN_DURATION.get(),
                BrutalityAttributes.TENACITY.get(),
                BrutalityAttributes.LETHALITY.get(),
                BrutalityAttributes.ARMOR_PENETRATION.get(),
                BrutalityAttributes.STEALTH.get(),
                BrutalityAttributes.THROW_STRENGTH.get()
                );

        // 4. Add player-specific attributes only to the PLAYER EntityType
        for (Attribute attribute : playerOnlyAttributes) {
            if (!event.has(EntityType.PLAYER, attribute)) {
                event.add(EntityType.PLAYER, attribute);
            }
        }
    }


}
