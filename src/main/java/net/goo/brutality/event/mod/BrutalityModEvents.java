package net.goo.brutality.event.mod;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.mobs.SummonedStray;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModAttributes;
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
        event.put(BrutalityModEntities.SUMMONED_STRAY.get(), SummonedStray.createAttributes().build());
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeModificationEvent event) {

        // --- Attributes that should be given to ALL Living Entities ---

        // 1. Define the attributes that are generic (Friction, Damage Taken, etc.)
        Set<Attribute> genericAttributes = Set.of(
                BrutalityModAttributes.AIR_FRICTION.get(),
                BrutalityModAttributes.GROUND_FRICTION.get(),
                BrutalityModAttributes.DAMAGE_TAKEN.get(),
                BrutalityModAttributes.BLUNT_DAMAGE.get(),
                BrutalityModAttributes.PIERCING_DAMAGE.get(),
                BrutalityModAttributes.SLASH_DAMAGE.get(),
                BrutalityModAttributes.AXE_DAMAGE.get(),
                BrutalityModAttributes.SWORD_DAMAGE.get(),
                BrutalityModAttributes.HAMMER_DAMAGE.get(),
                BrutalityModAttributes.SPEAR_DAMAGE.get(),
                BrutalityModAttributes.SCYTHE_DAMAGE.get()
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
                BrutalityModAttributes.RAGE_TIME.get(),
                BrutalityModAttributes.RAGE_LEVEL.get(),
                BrutalityModAttributes.MAX_RAGE.get(),
                BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get(),
                BrutalityModAttributes.MANA_COST.get(),
                BrutalityModAttributes.MANA_REGEN.get(),
                BrutalityModAttributes.MAX_MANA.get(),
                BrutalityModAttributes.SPELL_COOLDOWN_REDUCTION.get(),
                BrutalityModAttributes.CAST_TIME_REDUCTION.get(),
                BrutalityModAttributes.SPELL_DAMAGE.get(),
                BrutalityModAttributes.DAEMONIC_SCHOOL_LEVEL.get(),
                BrutalityModAttributes.DARKIST_SCHOOL_LEVEL.get(),
                BrutalityModAttributes.VOLTWEAVER_SCHOOL_LEVEL.get(),
                BrutalityModAttributes.CELESTIA_SCHOOL_LEVEL.get(),
                BrutalityModAttributes.UMBRANCY_SCHOOL_LEVEL.get(),
                BrutalityModAttributes.EXODIC_SCHOOL_LEVEL.get(),
                BrutalityModAttributes.BRIMWIELDER_SCHOOL_LEVEL.get(),
                BrutalityModAttributes.VOIDWALKER_SCHOOL_LEVEL.get(),
                BrutalityModAttributes.CRITICAL_STRIKE_CHANCE.get(),
                BrutalityModAttributes.CRITICAL_STRIKE_DAMAGE.get(),
                BrutalityModAttributes.JUMP_HEIGHT.get(),
                BrutalityModAttributes.STUN_CHANCE.get(),
                BrutalityModAttributes.STUN_DURATION.get(),
                BrutalityModAttributes.TENACITY.get(),
                BrutalityModAttributes.LETHALITY.get(),
                BrutalityModAttributes.ARMOR_PENETRATION.get(),
                BrutalityModAttributes.ENTITY_VISIBILITY.get(),
                BrutalityModAttributes.THROW_STRENGTH.get()
                );

        // 4. Add player-specific attributes only to the PLAYER EntityType
        for (Attribute attribute : playerOnlyAttributes) {
            if (!event.has(EntityType.PLAYER, attribute)) {
                event.add(EntityType.PLAYER, attribute);
            }
        }
    }


}
