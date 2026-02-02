package net.goo.brutality.common.item.curios.charm;

import com.google.common.base.Suppliers;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A Curio item that provides temporary buffs upon respawning.
 * <p>
 * This system uses a state-machine approach to detect when a player approaches their
 * last death location, at which point the booster effects are cleared.
 * </p>
 */
public class BoosterPack extends BrutalityCurioItem {
    private static final String TAG_WAS_IN_RANGE = "wasInRange";

    /**
     * Defines the tiers of Booster Packs and their associated effects/cooldowns.
     */
    public enum BoosterType {
        NONE(null, 0, 0, null),
        SILVER(BrutalityItems.SILVER_BOOSTER_PACK, 0, 20 * 30,
                Suppliers.memoize(() -> Set.of(MobEffects.SATURATION, MobEffects.MOVEMENT_SPEED, MobEffects.ABSORPTION, MobEffects.JUMP))),

        DIAMOND(BrutalityItems.DIAMOND_BOOSTER_PACK, 20 * 30 * 60, 20 * 120,
                Suppliers.memoize(() -> Set.of(MobEffects.SATURATION, MobEffects.MOVEMENT_SPEED, MobEffects.ABSORPTION, MobEffects.JUMP, MobEffects.DAMAGE_RESISTANCE))),

        EVIL_KING(BrutalityItems.EVIL_KING_BOOSTER_PACK, 20 * 15 * 60, 20 * 5 * 60,
                Suppliers.memoize(() -> Set.of(MobEffects.SATURATION, MobEffects.MOVEMENT_SPEED, MobEffects.ABSORPTION, MobEffects.JUMP, MobEffects.DAMAGE_RESISTANCE, TerramityModMobEffects.IMMUNITY.get())));

        public final int duration, cooldown;
        public final Supplier<Set<MobEffect>> effects;
        public final RegistryObject<Item> item;

        BoosterType(RegistryObject<Item> item, int cooldown, int duration, Supplier<Set<MobEffect>> effects) {
            this.item = item;
            this.cooldown = cooldown;
            this.duration = duration;
            this.effects = effects;
        }

        /**
         * Applies the tiered effects to the player and triggers the item cooldown.
         */
        public void applyEffects(Player player) {
            if (item == null || effects == null || this == NONE) return;
            if (player.getCooldowns().isOnCooldown(item.get())) return;

            player.getCooldowns().addCooldown(item.get(), cooldown);
            effects.get().forEach(effect -> player.addEffect(new MobEffectInstance(effect, duration, 1)));
        }

        /**
         * Clears the specific effects granted by this booster tier.
         */
        public void removeEffects(Player player) {
            if (effects != null) {
                effects.get().forEach(player::removeEffect);
            }
        }
    }

    public BoosterPack(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    /**
     * Ticks the curio to check distance to the death location.
     * Uses NBT on the ItemStack to track state across ticks without memory leaks.
     */
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            boolean wasInRange = stack.getOrCreateTag().getBoolean(TAG_WAS_IN_RANGE);
            boolean isInRange = false;

            Optional<GlobalPos> deathLoc = player.getLastDeathLocation();
            if (deathLoc.isPresent() && player.level().dimension() == deathLoc.get().dimension()) {
                if (player.distanceToSqr(deathLoc.get().pos().getCenter()) < 100) {
                    isInRange = true;
                }
            }

            // Trigger: State transition from Out-of-Range to In-Range
            if (!wasInRange && isInRange) {
                player.getCapability(BrutalityCapabilities.BOOSTER_PACK).ifPresent(cap -> cap.getBoosterType().removeEffects(player));
                player.playSound(SoundEvents.BEACON_DEACTIVATE, 1.0F, 1.0F);
            }

            stack.getOrCreateTag().putBoolean(TAG_WAS_IN_RANGE, isInRange);
        }
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.enchant(Enchantments.VANISHING_CURSE, 1);
        return stack;
    }

    /**
     * An upgraded version of the Booster Pack that is kept on death
     * and provides advanced tooltips.
     */
    public static class Upgraded extends BoosterPack {
        public Upgraded(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
            super(rarity, descriptionComponents);
        }

        @Override
        public @NotNull ItemStack getDefaultInstance() {
            return new ItemStack(this); // No Vanishing Curse
        }

        @Override
        public @NotNull ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
            return ICurio.DropRule.ALWAYS_KEEP;
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
            super.appendHoverText(stack, level, tooltip, flag);

            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.getLastDeathLocation().ifPresent(globalPos -> {
                    tooltip.add(Component.empty());
                    tooltip.add(Component.translatable("message." + Brutality.MOD_ID + ".last_death_location")
                            .append(" " + globalPos.pos().toShortString())
                            .withStyle(ChatFormatting.DARK_GRAY));
                });
            }
        }
    }

    /**
     * Static entry point for the respawn logic.
     * Usually called from a {@link net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent}.
     */
    public static void handleRespawn(Player player) {
        DelayedTaskScheduler.queueServerWork(player.level(), 2, () ->
                player.getCapability(BrutalityCapabilities.BOOSTER_PACK).ifPresent(cap -> {
                    BoosterType type = cap.getBoosterType();

                    if (type != BoosterType.NONE) {
                        type.applyEffects(player);
                        cap.setBoosterType(BoosterType.NONE);
                    }
                }));
    }

    /**
     * Handles the death event of a living entity, specifically a Player entity. It checks the player's
     * inventory for specific booster pack items and updates the player's booster type capability based
     * on the items present and their cooldown state.
     *
     * @param event The {@link LivingDeathEvent} fired when an entity dies. The event contains details about
     *              the dead entity, allowing to determine if the entity is a player and to access the inventory
     *              and capabilities for processing booster pack items.
     */
    public static void handleDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player)
            player.getCapability(BrutalityCapabilities.BOOSTER_PACK).ifPresent(cap -> {
                CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                    ItemCooldowns cooldowns = player.getCooldowns();
                    if (handler.isEquipped(BrutalityItems.SILVER_BOOSTER_PACK.get())) {
                        cap.setBoosterType(BoosterPack.BoosterType.SILVER);
                    }
                    if (handler.isEquipped(BrutalityItems.DIAMOND_BOOSTER_PACK.get()) && !cooldowns.isOnCooldown(BrutalityItems.DIAMOND_BOOSTER_PACK.get())) {
                        cap.setBoosterType(BoosterPack.BoosterType.DIAMOND);
                    }
                    if (handler.isEquipped(BrutalityItems.EVIL_KING_BOOSTER_PACK.get()) && !cooldowns.isOnCooldown(BrutalityItems.EVIL_KING_BOOSTER_PACK.get())) {
                        cap.setBoosterType(BoosterPack.BoosterType.EVIL_KING);
                    }
                });
            });

    }
}