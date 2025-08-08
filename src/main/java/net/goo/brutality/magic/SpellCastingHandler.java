package net.goo.brutality.magic;

import net.goo.brutality.entity.capabilities.EntityCapabilities;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.s2cSyncCapabilitiesPacket;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.ModAttributes;
import net.mcreator.terramity.init.TerramityModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import java.util.Map;

public class SpellCastingHandler {
    public static boolean tryCastSpell(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel) {
        int finalLevel = getCorrectSpellLevel(player, spell, spellLevel);
        // Validate mana
        float actualManaCost = getActualManaCost(player, spell, spellLevel);
        EntityCapabilities.PlayerManaCap manaHandler = player.getCapability(BrutalityCapabilities.PLAYER_MANA_CAP).orElse(null);
        if (manaHandler.manaValue() < actualManaCost) {
            player.displayClientMessage(Component.translatable("message.brutality.no_mana").withStyle(ChatFormatting.RED), true);
            return false;
        }

        if (SpellCooldownTracker.isOnCooldown(player, spell)) {
            int remaining = SpellCooldownTracker.getRemainingTicks(player, spell);
            Component cooldown = Component.translatable("message.brutality.on_cooldown")
                    .append(String.valueOf(remaining / 20))
                    .append(Component.translatable("message.brutality.seconds")).withStyle(ChatFormatting.RED);

            player.displayClientMessage(
                    cooldown, true);
            return false;
        }

        if (!spell.onCast(player, stack, finalLevel)) return false;


        if (!player.level().isClientSide()) {
            manaHandler.decrementMana(actualManaCost);
            PacketHandler.sendToAllClients(new s2cSyncCapabilitiesPacket(player.getId(), player));
            SpellCooldownTracker.setCooldown(player, spell, spellLevel);
            player.playSound(TerramityModSounds.TOMEUSE.get(), 2F, Mth.nextFloat(player.getRandom(), 0.8F, 1.2F));
        }

        if (player.level().isClientSide()) {
            MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Client(player, stack, spell, finalLevel));
        } else {
            MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Server(player, stack, spell, finalLevel));
        }

        return true;
    }


    public static int getCooldownWithoutReduction(Player player, SpellStorage.SpellEntry entry) {
        IBrutalitySpell spell = entry.spell();

        int base = spell.getBaseCooldown();
        base += spell.getCooldownLevelScaling() * getCorrectSpellLevel(player, entry.spell(), entry.level());

        return Math.max(base, 1);
    }

    /**
     * Allows custom {@code spellLevel} for display purposes
     */
    public static int getCooldownWithoutReduction(Player player, SpellStorage.SpellEntry entry, int spellLevel) {
        IBrutalitySpell spell = entry.spell();

        int base = spell.getBaseCooldown();
        base += spell.getCooldownLevelScaling() * getCorrectSpellLevel(player, entry.spell(), spellLevel);

        return Math.max(base, 1);
    }

    public static int getManaCostWithoutReduction(Player player, SpellStorage.SpellEntry entry) {
        IBrutalitySpell spell = entry.spell();

        int base = spell.getBaseManaCost();
        base += spell.getManaCostLevelScaling() * getCorrectSpellLevel(player, entry.spell(), entry.level());
        return base;
    }

    /**
     * Allows custom {@code spellLevel} for display purposes
     */
    public static int getManaCostWithoutReduction(Player player, SpellStorage.SpellEntry entry, int spellLevel) {
        IBrutalitySpell spell = entry.spell();

        int base = spell.getBaseManaCost();
        base += spell.getManaCostLevelScaling() * getCorrectSpellLevel(player, entry.spell(), spellLevel);
        return base;
    }

    public static int getActualCooldown(Player player, SpellStorage.SpellEntry entry) {
        IBrutalitySpell spell = entry.spell();

        AttributeInstance cdrInstance = player.getAttribute(ModAttributes.SPELL_COOLDOWN_REDUCTION.get());
        int base = spell.getBaseCooldown();
        base += spell.getCooldownLevelScaling() * entry.level();

        return Math.max((int) (cdrInstance != null ? (base * (1 - cdrInstance.getValue())) : base), 1);
    }

    public static int getActualCooldown(Player player, IBrutalitySpell spell, int spellLevel) {
        AttributeInstance cdrInstance = player.getAttribute(ModAttributes.SPELL_COOLDOWN_REDUCTION.get());
        int base = spell.getBaseCooldown();
        base += spell.getCooldownLevelScaling() * spellLevel;

        return Math.max((int) (cdrInstance != null ? (base * (1 - cdrInstance.getValue())) : base), 1);
    }

    public static float getActualManaCost(Player player, SpellStorage.SpellEntry entry) {
        IBrutalitySpell spell = entry.spell();
        float base = spell.getBaseManaCost();
        base += spell.getManaCostLevelScaling() * entry.level();
        AttributeInstance manaCostAttr = player.getAttribute(ModAttributes.MANA_COST.get());
        if (manaCostAttr != null) {
            return (float) (base * manaCostAttr.getValue());
        }
        return base;
    }

    public static float getActualManaCost(Player player, IBrutalitySpell spell, int spellLevel) {
        float base = spell.getBaseManaCost();
        base += spell.getManaCostLevelScaling() * spellLevel;
        AttributeInstance manaCostAttr = player.getAttribute(ModAttributes.MANA_COST.get());
        if (manaCostAttr != null) {
            return (float) (base * manaCostAttr.getValue());
        }
        return base;
    }

    private static final Map<IBrutalitySpell.MagicSchool, Attribute> ATTRIBUTE_MAP = Map.of(
            IBrutalitySpell.MagicSchool.DAEMONIC, ModAttributes.DAEMONIC_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.DARKIST, ModAttributes.DARKIST_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.EVERGREEN, ModAttributes.EVERGREEN_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.VOLTWEAVER, ModAttributes.VOLTWEAVER_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.COSMIC, ModAttributes.COSMIC_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.CELESTIA, ModAttributes.CELESTIA_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.EXODIC, ModAttributes.EXODIC_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.BRIMWIELDER, ModAttributes.BRIMWIELDER_SCHOOL_LEVEL.get(),
            IBrutalitySpell.MagicSchool.VOIDWALKER, ModAttributes.VOIDWALKER_SCHOOL_LEVEL.get()
    );


    public static int getCorrectSpellLevel(LivingEntity caster, IBrutalitySpell spell, int spellLevel) {
        IBrutalitySpell.MagicSchool school = spell.getSchool();
        AttributeInstance attributeInstance = caster.getAttribute(ATTRIBUTE_MAP.get(school));

        return Math.max((int) (spellLevel + (attributeInstance != null ? attributeInstance.getValue() : 0)), 0);
    }


}