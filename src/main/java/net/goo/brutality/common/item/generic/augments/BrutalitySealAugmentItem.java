package net.goo.brutality.common.item.generic.augments;

import net.goo.brutality.common.item.BrutalityCategories;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BrutalitySealAugmentItem extends BrutalityAugmentItem {
    public BrutalitySealAugmentItem(Properties pProperties, BrutalityCategories... itemTypes) {
        super(pProperties, itemTypes);
    }

    @Override
    protected void addCustomTooltipLines(List<Component> components) {

    }

    // intensity is the number of similar seals that are on a specific item, opted for this instead of running the same method multiple times
    // Victim is nullable as an arrow could hit a block but not an entity
    public void onProcAtLocation(LivingEntity attacker, Vec3 location, int intensity) {
    }

    public void onHurtEntity(LivingEntity attacker, Entity victim, float damage, int intensity) {

    }

    public void onHurtByEntity(Entity attacker, LivingEntity victim, DamageSource source, float damage, int intensity) {
    }

    // From things without an attackable source, e.g., fire
    public void onHurt(LivingEntity victim, DamageSource source, float damage, int intensity) {
    }
}
