package net.goo.brutality.magic.spells.daemonium;

import net.goo.brutality.magic.BrutalitySpell;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DaemonicPickaxeSpell extends BrutalitySpell {


    public DaemonicPickaxeSpell() {
        super(MagicSchool.DAEMONIC, SpellType.UTIL, "daemonic_pickaxe", 30, 5, 80);
    }


    @Override
    public void onCast(Player player, ItemStack stack, int spellLevel) {
        if (!player.level().isClientSide) {
            System.out.println("CASTED DAEMONIUM PICKAXE WITH LEVEL " + spellLevel);
        }
    }
}
