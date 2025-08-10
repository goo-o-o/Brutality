package net.goo.brutality.magic;

import net.goo.brutality.entity.capabilities.EntityCapabilities;
import net.goo.brutality.item.weapon.tome.BaseMagicTome;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.s2cSyncCapabilitiesPacket;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.mcreator.terramity.init.TerramityModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class SpellCastingHandler {
    public static boolean tryCastSingletonSpell(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel) {
        int finalLevel = IBrutalitySpell.getActualSpellLevel(player, spell, spellLevel);
        // Validate mana
        float actualManaCost = IBrutalitySpell.getActualManaCost(player, spell, finalLevel);
        EntityCapabilities.PlayerManaCap manaHandler = player.getCapability(BrutalityCapabilities.PLAYER_MANA_CAP).orElse(null);
        if (manaHandler.manaValue() < actualManaCost) {
            player.displayClientMessage(Component.translatable("message.brutality.no_mana").withStyle(ChatFormatting.RED), true);
            return false;
        }

        if (!spell.getCategories().contains(IBrutalitySpell.SpellCategory.CONTINUOUS))
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
            if (!spell.getCategories().contains(IBrutalitySpell.SpellCategory.CONTINUOUS)) {
                SpellCooldownTracker.setCooldown(player, spell, finalLevel);
                player.playSound(TerramityModSounds.TOMEUSE.get(), 2F, Mth.nextFloat(player.getRandom(), 0.8F, 1.2F));
            }
        }

        if (player.level().isClientSide()) {
            MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Client(player, stack, spell, finalLevel));
        } else {
            MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Server(player, stack, spell, finalLevel));
        }

        return true;
    }

    public static boolean currentlyChannellingSpell(Player player, ItemStack stack) {
        if (stack.getItem() instanceof BaseMagicTome) {
            SpellStorage.SpellEntry currentSpell = SpellStorage.getCurrentSpellEntry(stack);
            if (currentSpell == null) return false;
            if (currentSpell.spell().getCategories().contains(IBrutalitySpell.SpellCategory.CHANNELING)) {
                return player.isUsingItem() && !SpellCooldownTracker.isOnCooldown(player, currentSpell.spell());
            }
        }
        return false;
    }

    public static float getChannellingProgress(Player player, ItemStack stack) {
        if (stack.getItem() instanceof BaseMagicTome) {
            SpellStorage.SpellEntry currentSpell = SpellStorage.getCurrentSpellEntry(stack);
            if (currentSpell == null) return 0F;
            if (currentSpell.spell().getCategories().contains(IBrutalitySpell.SpellCategory.CHANNELING)) {
                return (float) (stack.getUseDuration() - player.getUseItemRemainingTicks()) / IBrutalitySpell.getActualCastTime(player, currentSpell.spell(), currentSpell.level());
            }
        }
        return 0F;
    }
}