package net.goo.brutality.event.mod;

import net.goo.brutality.Brutality;
import net.goo.brutality.event.ConsumeManaEvent;
import net.goo.brutality.event.SpellCastEvent;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.magic.IBrutalitySpell;
import net.goo.brutality.magic.SpellCastingHandler;
import net.goo.brutality.magic.SpellCooldownTracker;
import net.goo.brutality.magic.SpellStorage;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityMagicEventHandler {

    @SubscribeEvent
    public static void onSpellCast(SpellCastEvent.Post event) {
        Player player = event.getPlayer();
        IBrutalitySpell spell = event.getSpell();
        int spellLevel = event.getSpellLevel();
        ItemStack tome = event.getStack();
        float manaCost = IBrutalitySpell.getActualManaCost(player, spell, spellLevel);

        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityModItems.HELLSPEC_TIE.get())) {
                if (spell.getSchool() == IBrutalitySpell.MagicSchool.BRIMWIELDER) {
                    SpellCastingHandler.addMana(player, manaCost * 0.25F);
                }
            }
            if (handler.isEquipped(BrutalityModItems.SOUL_STONE.get())) {
                float chance = ModUtils.getSyncedSeededRandom(player).nextFloat(0, 1);
                if (chance < 0.05F) {
                    SpellCastingHandler.addMana(player, manaCost);
                } else {
                    SpellCastingHandler.addMana(player, manaCost * 0.15F);
                }
            }

            if (!spell.getCategories().contains(IBrutalitySpell.SpellCategory.CONTINUOUS)) {
                handleRecursors(handler, player, tome, spell, spellLevel);
                handleMulticasts(handler, player, tome);
            }

        });
    }

    public static void handleMulticasts(ICuriosItemHandler handler, Player player, ItemStack tome) {
        List<SpellStorage.SpellEntry> entries = SpellStorage.getSpells(tome);
        SpellStorage.SpellEntry current = SpellStorage.getCurrentSpellEntry(tome);
        if (current != null) {
            entries.remove(current); // exclude current spell
        }
        if (entries.isEmpty()) return;

        RandomSource random = player.getRandom();
        float roll = random.nextFloat();

        // Define paragon configs: item, threshold base, luck bonus per level, max extra casts, mana multiplier
        record MulticastConfig(RegistryObject<Item> item, float baseThreshold, float luckBonus, int maxExtra, float manaMult) {}

        MulticastConfig[] configs = {
                new MulticastConfig(BrutalityModItems.PARAGON_OF_THE_FIRST_MAGE,         0.20F, 0.03F, 4, 0.25F),
                new MulticastConfig(BrutalityModItems.ARCHMAGES_THESIS_TO_MASTERFUL_MULTICASTING, 0.15F, 0.03F, 3, 0.50F),
                new MulticastConfig(BrutalityModItems.WIZARDS_GUIDEBOOK_TO_ADVANCED_MULTICASTING, 0.15F, 0.00F, 2, 0.75F),
                new MulticastConfig(BrutalityModItems.APPRENTICES_MANUAL_TO_BASIC_MULTICASTING, 0.15F, 0.00F, 1, 1.00F)
        };

        for (MulticastConfig cfg : configs) {
            if (handler.isEquipped(cfg.item().get())) {
                float threshold = cfg.baseThreshold();
                threshold += (float) player.getAttributeValue(Attributes.LUCK) * cfg.luckBonus();

                if (roll < threshold) {
                    Collections.shuffle(entries, new Random());
                    int count = Math.min(cfg.maxExtra(), entries.size());

                    for (int i = 0; i < count; i++) {
                        SpellStorage.SpellEntry entry = entries.get(i);
                        int spellLevel = IBrutalitySpell.getActualSpellLevel(player, entry.spell(), entry.level());
                        float cost = IBrutalitySpell.getActualManaCost(player, entry.spell(), spellLevel) * cfg.manaMult();

                        if (SpellCastingHandler.hasEnoughMana(player, cost)
                                && !SpellCooldownTracker.isOnCooldown(player, entry.spell())) {
                            entry.spell().onStartCast(player, tome, spellLevel);
                            SpellCastingHandler.decrementManaAndStartCooldown(player, entry.spell(), spellLevel);
                        }
                    }
                }
                return; // only one paragon can trigger
            }
        }
    }

    public static void handleRecursors(ICuriosItemHandler handler, Player player, ItemStack tome, IBrutalitySpell spell, int spellLevel) {
        RandomSource random = player.getRandom();

        record RecursorConfig(RegistryObject<Item> item, float baseThreshold, float luckBonusPerLevel, float thresholdDecay, float manaMult) {}

        RecursorConfig[] configs = {
                new RecursorConfig(BrutalityModItems.INFINITE_RECURSOR,     0.25F, 0.03F, 0.02F, 0.50F),
                new RecursorConfig(BrutalityModItems.CONVERGENT_RECURSOR,   0.15F, 0.00F, 0.00F, 0.75F),
                new RecursorConfig(BrutalityModItems.DIVERGENT_RECURSOR,    0.15F, 0.00F, 0.00F, 1.00F)
        };

        for (RecursorConfig cfg : configs) {
            if (handler.isEquipped(cfg.item().get())) {
                float threshold = cfg.baseThreshold();
                threshold += (float) player.getAttributeValue(Attributes.LUCK) * cfg.luckBonusPerLevel();

                int count = 0;
                float currentThreshold = threshold;

                // Divergent: single roll, max 1 extra cast
                if (cfg.item() == BrutalityModItems.DIVERGENT_RECURSOR) {
                    if (random.nextFloat() < threshold) count = 1;
                } else {
                    // Infinite / Convergent: chained rolls
                    while (random.nextFloat() < currentThreshold) {
                        count++;
                        if (cfg.thresholdDecay() > 0) {
                            currentThreshold -= cfg.thresholdDecay();
                        }
                    }
                }

                float costMult = cfg.manaMult();
                for (int i = 0; i < count; i++) {
                    final int delay = i + 1; // slight stagger
                    DelayedTaskScheduler.queueServerWork(player.level(), delay, () -> {
                        float cost = IBrutalitySpell.getActualManaCost(player, spell, spellLevel) * costMult;
                        if (SpellCastingHandler.hasEnoughMana(player, cost)) {
                            spell.onStartCast(player, tome, spellLevel);
                        }
                    });
                }
                return;
            }
        }
    }


    @SubscribeEvent
    public static void onConsumeMana(ConsumeManaEvent event) {
        float amount = event.getAmount();
        int level = event.getSpellLevel();
        IBrutalitySpell spell = event.getSpell();
        Player player = event.getPlayer();

        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            handler.findFirstCurio(BrutalityModItems.ONYX_IDOL.get()).ifPresent(slot -> {
                ItemStack stack = slot.stack();
                CompoundTag tag = stack.getOrCreateTag();
                tag.putFloat("mana", tag.getFloat("mana") + amount);
                if (tag.getFloat("mana") > 200) {
                    SpellCooldownTracker.resetCooldowns(player);
                    tag.putFloat("mana", amount % 200);
                }
            });
        });
    }

}
