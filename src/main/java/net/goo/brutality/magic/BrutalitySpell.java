package net.goo.brutality.magic;

import net.minecraft.network.chat.Component;


// Daemonic Spells
public enum DaemonicSpell implements IBrutalitySpell {
    PICKAXE(Component.translatable("spells.brutality.daemonium.pickaxe"), 40, 5, SpellType.UTIL, 100);

    private final Component name;
    private final int manaCost;
    private final int damage;
    private final SpellType type;
    private final int cooldown;

    DaemonicSpell(Component name, int manaCost, int damage, SpellType type, int cooldown) {
        this.name = name;
        this.manaCost = manaCost;
        this.damage = damage;
        this.type = type;
        this.cooldown = cooldown;
    }

    @Override
    public MagicSchool getSchool() {
        return MagicSchool.DAEMONIC;
    }

    @Override
    public SpellType getType() {
        return type;
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public int getBaseDamage() {
        return damage;
    }

}
