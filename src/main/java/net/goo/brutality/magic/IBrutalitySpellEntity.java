package net.goo.brutality.magic;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public interface IBrutalitySpellEntity {

    String SPELL_LEVEL = "spell_level";

//    void setDamage(float damage);
//    float getDamage();

    void setSpellLevel(int spellLevel);
    int getSpellLevel();

    default float getFinalDamage(IBrutalitySpell spell, LivingEntity owner, int spellLevel) {
        return spell.getFinalDamage(owner, spellLevel);
    }
    default float getFinalDamageWithoutAttributes(IBrutalitySpell spell, int spellLevel) {
        return spell.getFinalDamageWithoutAttributes(spellLevel);
    }

    float getSizeScaling();
    BrutalitySpell getSpell();


    /**
     * Creates a new spell entity instance
     *
     * @param entityType The entity type to create
     * @param level      The game spellLevel
     * @param spellLevel The spell spellLevel
     * @return New spell entity instance
     */
    static <T extends Entity & IBrutalitySpellEntity> T create(EntityType<T> entityType, Level level, int spellLevel) {
        T entity = entityType.create(level);
        if (entity != null) {
            entity.setSpellLevel(spellLevel);
        }
        return entity;
    }

}
