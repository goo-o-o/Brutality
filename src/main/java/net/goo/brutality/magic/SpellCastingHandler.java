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
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SpellCastingHandler {
    public static boolean tryCastSpell(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel) {
        // Validate mana
        EntityCapabilities.PlayerManaCap manaHandler = player.getCapability(BrutalityCapabilities.PLAYER_MANA_CAP).orElse(null);
        if (manaHandler.manaValue() < spell.getBaseManaCost()) {
            player.displayClientMessage(Component.translatable("message.brutality.no_mana").withStyle(ChatFormatting.RED), true);
            return false;
        }

        // Validate cooldown
        if (SpellCooldownTracker.isOnCooldown(player, spell)) {
            int remaining = SpellCooldownTracker.getRemainingTicks(player, spell);
            Component cooldown = Component.translatable("message.brutality.on_cooldown")
                    .append(String.valueOf(remaining / 20))
                    .append(Component.translatable("message.brutality.seconds")).withStyle(ChatFormatting.RED);

            player.displayClientMessage(
                    cooldown, true);
            return false;
        }

        // Validate special conditions
        if (!spell.canCast(player, stack, spellLevel)) {
            return false;
        }

        // All validations passed - cast the spell
        manaHandler.decrementMana(getTrueManaCost(player, spell));
        PacketHandler.sendToAllClients(new s2cSyncCapabilitiesPacket(player.getId(), player));

        SpellCooldownTracker.setCooldown(player, spell);
        spell.onCast(player, stack, spellLevel);
        player.playSound(TerramityModSounds.TOMEUSE.get(),
                2F, Mth.nextFloat(player.getRandom(), 0.8F, 1.2F));
        return true;
    }

    private static float getTrueManaCost(Player player, IBrutalitySpell spell) {
        float base = spell.getBaseManaCost();
        AttributeInstance manaCostAttr = player.getAttribute(ModAttributes.MANA_COST.get());
        if (manaCostAttr != null) {
            return (float) (base * manaCostAttr.getValue());
        }
        return base;
    }



}