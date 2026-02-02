package net.goo.brutality.util.magic;

import net.goo.brutality.common.item.weapon.tome.BaseMagicTome;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.event.SpellCastEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModLoader;

public class SpellCastingHandler {

    /**
     * Casts an instant spell for the specified player using the provided parameters.
     * <p>
     * This method handles the logic for casting spells that take effect immediately. It
     * checks whether the player has sufficient mana to cast the spell and ensures the spell
     * is not on cooldown. If these conditions are satisfied, the spell's start and end cast
     * behaviors are triggered, the appropriate cooldown is set, and a {@link SpellCastEvent.Post}
     * is posted to notify other systems of the successful spell casting. If the casting process
     * is canceled or fails, no changes are applied.
     * </p>
     *
     * @param player     The player trying to cast the spell.
     * @param stack      The {@link ItemStack} associated with the spell casting process, often the item used for it.
     * @param spell      The {@link BrutalitySpell} to be cast instantly.
     * @param spellLevel The level of the spell being cast, affecting its behavior and mana cost.
     * @return {@code true} if the spell was successfully cast; {@code false} otherwise.
     */
    public static boolean castInstantSpell(Player player, ItemStack stack, BrutalitySpell spell, int spellLevel) {
        SpellCastEvent.Pre event = new SpellCastEvent.Pre(player, stack, spell, spellLevel);
        ModLoader.get().postEvent(event);
        if (event.isCanceled()) return false;

        if (hasEnoughManaToCast(player, spell, spellLevel) && !SpellCooldownTracker.isOnCooldown(player, spell)) {
            if (spell.onStartCast(player, stack, spellLevel)) {
                spell.onEndCast(player, stack, spellLevel);
                subtractSpellCost(player, spell, spellLevel);
                setCooldown(player, spell, spellLevel);
                ModLoader.get().postEvent(new SpellCastEvent.Post(player, stack, spell, spellLevel));
                return true;
            }
        }
        return false;
    }

    /**
     * Initiates the process of continuously casting a spell for the specified player.
     * <p>
     * This method begins the casting sequence for a continuous spell. It verifies
     * that the player has sufficient mana and that the spell is not on cooldown.
     * If these conditions are met and the casting starts successfully, the required
     * mana is deducted, and a {@link SpellCastEvent.Post} is triggered. If the casting
     * fails or is cancelled, no changes are made.
     * </p>
     *
     * @param player     The player attempting to cast the spell.
     * @param stack      The {@link ItemStack} associated with the spell, usually the item used for casting.
     * @param spell      The {@link BrutalitySpell} being continuously cast.
     * @param spellLevel The level of the spell being cast, which may affect its behavior, damage, or mana cost.
     * @return {@code true} if the casting sequence was initiated successfully; {@code false} otherwise.
     */
    public static boolean startContinuousCast(Player player, ItemStack stack, BrutalitySpell spell, int spellLevel) {
        SpellCastEvent.Pre event = new SpellCastEvent.Pre(player, stack, spell, spellLevel);
        ModLoader.get().postEvent(event);
        if (event.isCanceled()) return false;

        if (hasEnoughManaToCast(player, spell, spellLevel) && !SpellCooldownTracker.isOnCooldown(player, spell)) {
            if (spell.onStartCast(player, stack, spellLevel)) {
                subtractSpellCost(player, spell, spellLevel);
                ModLoader.get().postEvent(new SpellCastEvent.Post(player, stack, spell, spellLevel));
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the per-tick logic for maintaining a continuous spell cast for a player.
     * <p>
     * This method is invoked for every game tick during the continuous casting
     * of a spell. It ensures that the player has sufficient mana and that the
     * spell is not on cooldown. If these conditions are met, the spell's behavior
     * for this tick is triggered, mana is deducted from the player, and a {@link SpellCastEvent.Post}
     * is posted. If the conditions are not met, the player's casting action is terminated.
     * </p>
     *
     * @param player     The player who is casting the spell.
     * @param stack      The {@link ItemStack} associated with the spell, likely the item used for casting.
     * @param spell      The {@link BrutalitySpell} being continuously cast by the player.
     * @param spellLevel The level of the spell being cast, affecting its behavior and mana cost.
     */
    public static void tickContinuousCast(Player player, ItemStack stack, BrutalitySpell spell, int spellLevel) {
        if (hasEnoughManaToCast(player, spell, spellLevel) && !SpellCooldownTracker.isOnCooldown(player, spell)) {
            spell.onCastTick(player, stack, spellLevel);
            subtractSpellCost(player, spell, spellLevel);
            ModLoader.get().postEvent(new SpellCastEvent.Post(player, stack, spell, spellLevel));
        } else {
            player.releaseUsingItem();
        }
    }

    /**
     * Ends a continuous spell casting process for the specified player.
     * <p>
     * This method finalizes the continuous casting process of a spell. It triggers
     * the spell's end-cast behavior, sets a cooldown for the spell, and posts a
     * {@link SpellCastEvent.Post} event to notify other systems of the completion.
     * </p>
     *
     * @param player     The player for whom the continuous spell casting is to be ended.
     * @param stack      The {@link ItemStack} associated with the spell.
     * @param spell      The spell being cast, represented by a {@link BrutalitySpell} instance.
     * @param spellLevel The level of the spell being cast, determining its effectiveness and cost.
     */
    public static void endContinuousCast(Player player, ItemStack stack, BrutalitySpell spell, int spellLevel) {
        spell.onEndCast(player, stack, spellLevel);
        setCooldown(player, spell, spellLevel);
        ModLoader.get().postEvent(new SpellCastEvent.Post(player, stack, spell, spellLevel));
    }

    /**
     * Casts a channeling spell for the given player, item stack, and spell details.
     * <p>
     * This method executes the logic to cast a spell that requires channeling. It checks
     * conditions such as the remaining time, starts the casting process, triggers relevant
     * events, and finalizes the spell by applying its effects and initiating a cooldown.
     * </p>
     *
     * @param player         The player attempting to cast the channeling spell.
     * @param stack          The {@link ItemStack} associated with the spell casting process.
     * @param spell          The {@link BrutalitySpell} being cast.
     * @param spellLevel     The level of the spell being cast, determining its effectiveness or behavior.
     * @param remainingTicks The number of remaining ticks for the spell-casting process to complete.
     * @return {@code true} if the channeling spell was successfully cast; {@code false} otherwise.
     */
    public static boolean castChannellingSpell(Player player, ItemStack stack, BrutalitySpell spell, int spellLevel, int remainingTicks) {
        int castTime = IBrutalitySpell.getActualCastTime(player, spell, spellLevel);
        if (remainingTicks <= stack.getUseDuration() - castTime) {
            SpellCastEvent.Pre event = new SpellCastEvent.Pre(player, stack, spell, spellLevel);
            ModLoader.get().postEvent(event);
            if (event.isCanceled()) return false;

            if (spell.onStartCast(player, stack, spellLevel)) {
                spell.onEndCast(player, stack, spellLevel);
                subtractSpellCost(player, spell, spellLevel);
                setCooldown(player, spell, spellLevel);
                ModLoader.get().postEvent(new SpellCastEvent.Post(player, stack, spell, spellLevel));
                player.releaseUsingItem();
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the cooldown for a specified player and spell based on the spell level.
     * <p>
     * This method computes the actual cooldown for a spell, considering the player's
     * attributes and the spell's level, and applies it to the specified player.
     * </p>
     *
     * @param player     The player for whom the cooldown is being set.
     * @param spell      The {@link BrutalitySpell} for which the cooldown is being applied.
     * @param spellLevel The level of the spell, which influences the cooldown duration.
     */
    public static void setCooldown(Player player, BrutalitySpell spell, int spellLevel) {
        SpellCooldownTracker.setCooldown(player, spell, IBrutalitySpell.getActualCooldown(player, spell, spellLevel));
    }


    /**
     * Deducts the mana cost of a specified spell from the player's available mana.
     * <p>
     * This method calculates the actual mana cost of the spell based on the player's attributes and
     * the spell's level. It then adjusts the player's mana accordingly by invoking
     * {@link ManaHelper#modifyManaValue}.
     * </p>
     *
     * @param player     The {@link Player} whose mana is to be adjusted.
     * @param spell      The {@link BrutalitySpell} for which the mana cost is being subtracted.
     * @param spellLevel The level of the spell being cast, influencing the mana cost.
     */
    public static void subtractSpellCost(Player player, BrutalitySpell spell, int spellLevel) {
        ManaHelper.modifyManaValue(player, -IBrutalitySpell.getActualManaCost(player, spell, spellLevel));
    }

    public static boolean currentlyChannellingSpell(Player player, ItemStack stack) {
        if (stack.getItem() instanceof BaseMagicTome) {
            SpellStorage.SpellEntry currentSpell = SpellStorage.getCurrentSpellEntry(stack);
            if (currentSpell == null) return false;
            if (currentSpell.spell().getCategories().contains(BrutalitySpell.SpellCategory.CHANNELLING)) {
                return player.isUsingItem() && !SpellCooldownTracker.isOnCooldown(player, currentSpell.spell());
            }
        }
        return false;
    }

    public static boolean currentlyCastingContinuousSpell(Player player, ItemStack stack) {
        if (stack.getItem() instanceof BaseMagicTome) {
            SpellStorage.SpellEntry currentSpell = SpellStorage.getCurrentSpellEntry(stack);
            if (currentSpell == null) return false;
            if (currentSpell.spell().getCategories().contains(BrutalitySpell.SpellCategory.CONTINUOUS)) {
                return player.isUsingItem() && !SpellCooldownTracker.isOnCooldown(player, currentSpell.spell());
            }
        }
        return false;
    }

    public static float getChannellingProgress(Player player, ItemStack stack) {
        if (stack.getItem() instanceof BaseMagicTome) {
            SpellStorage.SpellEntry currentSpell = SpellStorage.getCurrentSpellEntry(stack);
            if (currentSpell == null) return 0F;
            if (currentSpell.spell().getCategories().contains(BrutalitySpell.SpellCategory.CHANNELLING)) {
                return (float) (stack.getUseDuration() - player.getUseItemRemainingTicks()) / IBrutalitySpell.getActualCastTime(player, currentSpell.spell(), currentSpell.level());
            }
        }
        return 0F;
    }

    public static boolean hasEnoughManaToCast(Player player, SpellStorage.SpellEntry spellEntry) {
        return ManaHelper.getMana(player) >= IBrutalitySpell.getActualManaCost(player, spellEntry);
    }

    public static boolean hasEnoughManaToCast(Player player, BrutalitySpell spell, int spellLevel) {
        return ManaHelper.getMana(player) >= IBrutalitySpell.getActualManaCost(player, spell, spellLevel);
    }
}