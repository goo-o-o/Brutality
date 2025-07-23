package net.goo.brutality.magic;


public abstract class BrutalitySpell implements IBrutalitySpell {
    private final MagicSchool school;
    private final SpellType type;
    private final String name;
    private final int baseManaCost;
    private final float baseDamage;
    private final int baseCooldown;

    protected BrutalitySpell(MagicSchool school, SpellType type, String name,
                             int baseManaCost, float baseDamage, int baseCooldown) {
        this.school = school;
        this.type = type;
        this.name = name;
        this.baseManaCost = baseManaCost;
        this.baseDamage = baseDamage;
        this.baseCooldown = baseCooldown;
    }

    @Override
    public MagicSchool getSchool() {
        return school;
    }


    @Override
    public String getSpellName() {
        return name;
    }


    @Override
    public SpellType getType() {
        return type;
    }

    @Override
    public float getBaseDamage() {
        return baseDamage;
    }

    @Override
    public int getBaseCooldown() {
        return baseCooldown;
    }

    @Override
    public int getBaseManaCost() {
        return baseManaCost;
    }

}