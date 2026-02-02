package net.goo.brutality.common.item.generic;

import net.goo.brutality.common.magic.IBrutalitySpell;
import net.minecraft.world.item.Item;

public class SpellScroll extends Item {
    IBrutalitySpell.MagicSchool school;
    public SpellScroll(Properties pProperties) {
        super(pProperties);
    }

    public SpellScroll withSchool(IBrutalitySpell.MagicSchool school) {
        this.school = school;
        return this;
    }
}
