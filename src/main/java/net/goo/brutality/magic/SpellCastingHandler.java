package net.goo.brutality.magic;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.capabilities.EntityCapabilities;
import net.goo.brutality.event.SpellCastEvent;
import net.goo.brutality.item.weapon.tome.BaseMagicTome;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.ClientboundSyncCapabilitiesPacket;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class SpellCastingHandler {
    public static boolean castInstantSpell(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel) {
        CastConditionResult result = checkAllConditions(player, spell, spellLevel);
        if (!result.canCast()) {
            if (player.level().isClientSide()) player.displayClientMessage(result.feedback(), true);
            return false;
        }
        SpellCastEvent.Pre event = new SpellCastEvent.Pre(player, stack, spell, spellLevel);
        if (MinecraftForge.EVENT_BUS.post(event) || event.isCanceled()) {
            return false;
        }
        if (spell.onStartCast(player, stack, spellLevel)) {
            spell.onEndCast(player, stack, spellLevel);
            decrementManaAndStartCooldown(player, spell, spellLevel);
            MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(player, stack, spell, spellLevel));
            return true;
        }
        return false;
    }

    public static boolean startContinuousCast(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel) {
        CastConditionResult result = checkAllConditions(player, spell, spellLevel);
        if (!result.canCast()) {
            if (player.level().isClientSide()) player.displayClientMessage(result.feedback(), true);
            return false;
        }

        SpellCastEvent.Pre event = new SpellCastEvent.Pre(player, stack, spell, spellLevel);
        if (MinecraftForge.EVENT_BUS.post(event) || event.isCanceled()) {
            return false;
        }

        if (spell.onStartCast(player, stack, spellLevel)) {
            handleSpellManaCost(player, spell, spellLevel);
            MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(player, stack, spell, spellLevel));
            return true;
        }

        return false;
    }

    public static void tickContinuousCast(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel) {
        CastConditionResult result = checkAllConditions(player, spell, spellLevel);
        if (result.canCast()) {
            spell.onCastTick(player, stack, spellLevel);
            handleSpellManaCost(player, spell, spellLevel);
            MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(player, stack, spell, spellLevel));
        }
    }

    public static void endContinuousCast(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel) {
        spell.onEndCast(player, stack, spellLevel);
        SpellCooldownTracker.setCooldown(player, spell, spellLevel);
        MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(player, stack, spell, spellLevel));
    }

    public static boolean castChannellingSpell(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel, int remainingTicks) {
        CastConditionResult result = checkAllConditions(player, spell, spellLevel);
        if (!result.canCast()) {
            if (player.level().isClientSide()) player.displayClientMessage(result.feedback(), true);
            return false;
        }

        int castTime = IBrutalitySpell.getActualCastTime(player, spell, spellLevel);
        if (remainingTicks <= stack.getUseDuration() - castTime) {
            SpellCastEvent.Pre event = new SpellCastEvent.Pre(player, stack, spell, spellLevel);
            if (MinecraftForge.EVENT_BUS.post(event) || event.isCanceled()) {
                return false;
            }
            if (spell.onStartCast(player, stack, spellLevel)) {
                spell.onEndCast(player, stack, spellLevel);
                decrementManaAndStartCooldown(player, spell, spellLevel);
                MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(player, stack, spell, spellLevel));
                return true;
            }
        }

        return false;
    }

    private static void handleSpellManaCost(Player player, IBrutalitySpell spell, int spellLevel) {
        EntityCapabilities.PlayerManaCap manaHandler = getManaHandler(player);
        manaHandler.decrementMana(player, spell, spellLevel, IBrutalitySpell.getActualManaCost(player, spell, spellLevel));
        if (!player.level().isClientSide()) {
            PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(player.getId(), player));
        }
    }

    public static void addMana(Player player, float amount) {
        EntityCapabilities.PlayerManaCap manaHandler = getManaHandler(player);
        manaHandler.incrementMana(amount);
        if (!player.level().isClientSide()) {
            PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(player.getId(), player));
        }
    }

    /**
     * This should be run on both sides
     */
    public static void decrementManaAndStartCooldown(Player player, IBrutalitySpell spell, int spellLevel) {
        handleSpellManaCost(player, spell, spellLevel);
        SpellCooldownTracker.setCooldown(player, spell, spellLevel);
    }

    public static EntityCapabilities.PlayerManaCap getManaHandler(Player player) {
        return player.getCapability(BrutalityCapabilities.PLAYER_MANA_CAP).orElse(null);
    }

    public static boolean hasEnoughMana(Player player, IBrutalitySpell spell, int spellLevel) {
        if (!player.level().isClientSide())
            PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(player.getId(), player));
        EntityCapabilities.PlayerManaCap manaHandler = getManaHandler(player);
        return manaHandler.manaValue() >= IBrutalitySpell.getActualManaCost(player, spell, spellLevel);
    }


    public record CastConditionResult(boolean canCast, Component feedback) {
    }

    public static CastConditionResult checkMana(Player player, IBrutalitySpell spell, int spellLevel) {
        if (!hasEnoughMana(player, spell, spellLevel)) {
            return new CastConditionResult(false, Component.translatable("message." + Brutality.MOD_ID + ".no_mana").withStyle(ChatFormatting.RED));
        }
        return new CastConditionResult(true, null);
    }

    public static CastConditionResult checkCooldown(Player player, IBrutalitySpell spell) {
        if (SpellCooldownTracker.isOnCooldown(player, spell)) {
            int remaining = SpellCooldownTracker.getRemainingTicks(player, spell);
            Component feedback = Component.translatable("message." + Brutality.MOD_ID + ".on_cooldown")
                    .append(String.valueOf(remaining / 20))
                    .append(Component.translatable("message." + Brutality.MOD_ID + ".seconds")).withStyle(ChatFormatting.RED);
            return new CastConditionResult(false, feedback);
        }
        return new CastConditionResult(true, null);
    }

    public static CastConditionResult checkAllConditions(Player player, IBrutalitySpell spell, int spellLevel) {
        CastConditionResult manaResult = checkMana(player, spell, spellLevel);
        if (!manaResult.canCast()) return manaResult;
        CastConditionResult cooldownResult = checkCooldown(player, spell);
        if (!cooldownResult.canCast()) return cooldownResult;
        return new CastConditionResult(true, null);
    }

    public static boolean currentlyChannellingSpell(Player player, ItemStack stack) {
        if (stack.getItem() instanceof BaseMagicTome) {
            SpellStorage.SpellEntry currentSpell = SpellStorage.getCurrentSpellEntry(stack);
            if (currentSpell == null) return false;
            if (currentSpell.spell().getCategories().contains(IBrutalitySpell.SpellCategory.CHANNELLING)) {
                return player.isUsingItem() && !SpellCooldownTracker.isOnCooldown(player, currentSpell.spell());
            }
        }
        return false;
    }

    public static boolean currentlyCastingContinuousSpell(Player player, ItemStack stack) {
        if (stack.getItem() instanceof BaseMagicTome) {
            SpellStorage.SpellEntry currentSpell = SpellStorage.getCurrentSpellEntry(stack);
            if (currentSpell == null) return false;
            if (currentSpell.spell().getCategories().contains(IBrutalitySpell.SpellCategory.CONTINUOUS)) {
                return player.isUsingItem() && !SpellCooldownTracker.isOnCooldown(player, currentSpell.spell());
            }
        }
        return false;
    }

    public static float getChannellingProgress(Player player, ItemStack stack) {
        if (stack.getItem() instanceof BaseMagicTome) {
            SpellStorage.SpellEntry currentSpell = SpellStorage.getCurrentSpellEntry(stack);
            if (currentSpell == null) return 0F;
            if (currentSpell.spell().getCategories().contains(IBrutalitySpell.SpellCategory.CHANNELLING)) {
                return (float) (stack.getUseDuration() - player.getUseItemRemainingTicks()) / IBrutalitySpell.getActualCastTime(player, currentSpell.spell(), currentSpell.level());
            }
        }
        return 0F;
    }
}