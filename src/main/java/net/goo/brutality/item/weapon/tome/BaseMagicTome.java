package net.goo.brutality.item.weapon.tome;

import com.mojang.blaze3d.platform.InputConstants;
import net.goo.brutality.Brutality;
import net.goo.brutality.entity.capabilities.EntityCapabilities;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityGenericItem;
import net.goo.brutality.magic.IBrutalitySpell;
import net.goo.brutality.magic.SpellCastingHandler;
import net.goo.brutality.magic.SpellCooldownTracker;
import net.goo.brutality.magic.SpellStorage;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.List;

import static net.goo.brutality.magic.IBrutalitySpell.SpellCategory.CHANNELING;
import static net.goo.brutality.magic.IBrutalitySpell.SpellCategory.CONTINUOUS;
import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.CHANCE;
import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.DURATION;

public class BaseMagicTome extends BrutalityGenericItem {


    public BaseMagicTome(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.ItemType.TOME;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        List<SpellStorage.SpellEntry> spells = SpellStorage.getSpells(stack);
        if (!spells.isEmpty()) {
            SpellStorage.SpellEntry spellEntry = SpellStorage.getCurrentSpellEntry(stack);
            if (spellEntry == null) return InteractionResultHolder.pass(stack);
            List<IBrutalitySpell.SpellCategory> categories = spellEntry.spell().getCategories();

            if (categories.contains(CHANNELING) || categories.contains(CONTINUOUS)) {
                player.startUsingItem(hand);
                if (level instanceof ServerLevel serverLevel && !SpellCooldownTracker.isOnCooldown(player, spellEntry.spell())) {
                    serverLevel.playSound(null, player.getOnPos(), BrutalityModSounds.TOME_OPEN.get(), SoundSource.PLAYERS, 1, 1);
                    stopTriggeredAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "book_controller", "close");
                    if (categories.contains(CHANNELING))
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "main_controller", "cast_channelling");
                    else if (categories.contains(CONTINUOUS))
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "main_controller", "cast_continuous");
                }
                return InteractionResultHolder.pass(stack);
            }
            if (SpellCastingHandler.tryCastSingletonSpell(player, stack, spellEntry.spell(), spellEntry.level())) {
                if (level instanceof ServerLevel serverLevel) {
                    player.getCooldowns().addCooldown(stack.getItem(), 25);
                    serverLevel.playSound(null, player.getOnPos(), BrutalityModSounds.TOME_OPEN.get(), SoundSource.PLAYERS, 1, 1);
                    stopTriggeredAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "main_controller", "cast_channelling");
                    stopTriggeredAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "main_controller", "cast_continuous");
                    stopTriggeredAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "book_controller", "close");
                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "main_controller", "cast_instant");
                }
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
        SpellStorage.SpellEntry entry = SpellStorage.getCurrentSpellEntry(pStack);
        if (entry == null || !(pLivingEntity instanceof Player player)) return;
        IBrutalitySpell spell = entry.spell();
        if (SpellCooldownTracker.isOnCooldown(player, spell)) return;
        if (!(spell.getCategories().contains(CHANNELING) || spell.getCategories().contains(CONTINUOUS)))
            return;

        if (spell.getCategories().contains(CONTINUOUS)) {
            SpellCooldownTracker.setCooldown(player, spell, IBrutalitySpell.getActualSpellLevel(player, spell, entry.level()));
            if (pLevel instanceof ServerLevel serverLevel) {
                serverLevel.playSound(null, player.getOnPos(), BrutalityModSounds.TOME_CLOSE.get(), SoundSource.PLAYERS, 1, 1);

                stopTriggeredAnim(player, GeoItem.getOrAssignId(pStack, serverLevel), "main_controller", "cast_continuous");
                triggerAnim(player, GeoItem.getOrAssignId(pStack, serverLevel), "book_controller", "close");

            }
        }
        player.getCooldowns().addCooldown(pStack.getItem(), 25);

    }


    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        SpellStorage.SpellEntry spellEntry = SpellStorage.getCurrentSpellEntry(pStack);
        int useTicks = this.getUseDuration(pStack) - pRemainingUseDuration;
        if (spellEntry == null) return;
        IBrutalitySpell spell = spellEntry.spell();
        if (!(pLivingEntity instanceof Player player)) return;
        if (SpellCooldownTracker.isOnCooldown(player, spell)) {
            player.stopUsingItem();
            return;
        }

        int actualSpellLevel = IBrutalitySpell.getActualSpellLevel(player, spell, spellEntry.level());
        int actualCastTime = IBrutalitySpell.getActualCastTime(player, spell, actualSpellLevel);
        List<IBrutalitySpell.SpellCategory> categories = spell.getCategories();

        if (categories.contains(CHANNELING)) {
            if (spellEntry.spell().interruptibleByDamage()) {
                if (player.isHurt()) {
                    player.getCooldowns().addCooldown(pStack.getItem(), 25);
                    int finalLevel = IBrutalitySpell.getActualSpellLevel(player, spell, spellEntry.level());
                    EntityCapabilities.PlayerManaCap manaHandler = player.getCapability(BrutalityCapabilities.PLAYER_MANA_CAP).orElse(null);
                    manaHandler.decrementMana(IBrutalitySpell.getActualManaCost(player, spell, finalLevel));
                    SpellCooldownTracker.setCooldown(player, spell, finalLevel);
                    player.displayClientMessage(Component.translatable("message." + Brutality.MOD_ID + ".cast_failed").withStyle(ChatFormatting.RED), true);
                    closeBook(player, pStack, pLevel);
                    player.stopUsingItem();
                    return;
                }
            }
            if (useTicks > actualCastTime) {
                player.stopUsingItem();
                if (pLevel instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, player.getOnPos(), BrutalityModSounds.TOME_CLOSE.get(), SoundSource.PLAYERS, 1, 1);
                    stopTriggeredAnim(player, GeoItem.getOrAssignId(pStack, serverLevel), "main_controller", "cast_channelling");
                    triggerAnim(player, GeoItem.getOrAssignId(pStack, serverLevel), "book_controller", "close");
                }
                SpellCastingHandler.tryCastSingletonSpell(player, pStack, spell, spellEntry.level());
                player.getCooldowns().addCooldown(pStack.getItem(), 25);
            }
        } else if (categories.contains(CONTINUOUS)) {
            if (useTicks % Math.max(actualCastTime, 0) == 0) {
                SpellCastingHandler.tryCastSingletonSpell(player, pStack, spell, actualSpellLevel);
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main_controller", (state) -> PlayState.STOP)
                .triggerableAnim("cast_instant", RawAnimation.begin().thenPlay("cast_instant"))
                .triggerableAnim("cast_channelling", RawAnimation.begin().thenPlayAndHold("cast_channelling"))
                .triggerableAnim("cast_continuous", RawAnimation.begin().thenPlayAndHold("cast_continuous"))
        );

        controllers.add(new AnimationController<>(this, "book_controller", (state) -> PlayState.STOP)
                .triggerableAnim("close", RawAnimation.begin().thenPlayAndHold("close"))
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (level != null && !level.isClientSide()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        List<SpellStorage.SpellEntry> spells = SpellStorage.getSpells(stack);
        if (spells.isEmpty()) {
            tooltip.add(Component.translatable("spell." + Brutality.MOD_ID + ".empty"));
            return;
        }

        if (InputConstants.isKeyDown(mc.getWindow().getWindow(), mc.options.keyShift.getKey().getValue())) {
            for (SpellStorage.SpellEntry entry : spells) {
                IBrutalitySpell spell = entry.spell();
                String spellName = spell.getSpellName();


                AttributeInstance manaCostAttr = mc.player.getAttribute(ModAttributes.MANA_COST.get());
                AttributeInstance spellCdAttr = mc.player.getAttribute(ModAttributes.SPELL_COOLDOWN_REDUCTION.get());
                AttributeInstance castTimeAttr = mc.player.getAttribute(ModAttributes.CAST_TIME_REDUCTION.get());
                float manaCostReduction = manaCostAttr != null ? (float) manaCostAttr.getValue() : 0F;
                float spellCdReduction = 1 - (spellCdAttr != null ? (float) spellCdAttr.getValue() : 0F);
                float castTimeReduction = 1 - (castTimeAttr != null ? (float) castTimeAttr.getValue() : 0F);

                boolean showManaReduct = manaCostReduction != 1F;
                boolean showCDReduct = spellCdReduction != 1F;
                boolean showCastReduct = castTimeReduction != 1F;

                // Constants for consistent formatting
                MutableComponent divider = Component.literal(" | ").withStyle(ChatFormatting.DARK_GRAY);
                int actualSpellLevel = IBrutalitySpell.getActualSpellLevel(mc.player, spell, entry.level());
                float manaCost = IBrutalitySpell.getActualManaCost(mc.player, spell, actualSpellLevel);
                int castTime = IBrutalitySpell.getActualCastTime(mc.player, spell, actualSpellLevel);
                castTime /= 20;
                float spellCooldown = IBrutalitySpell.getActualCooldown(mc.player, spell, actualSpellLevel);
                spellCooldown /= 20;

                int bonusLevel = actualSpellLevel - entry.level();
                // Spell name and actualSpellLevel

                MutableComponent spellType = Component.literal(" §8|§r ");

                spell.getCategories().forEach(category -> {
                    spellType.append(category.icon + " ");
                });

                tooltip.add(Component.translatable("spell." + Brutality.MOD_ID + "." + spellName).append(" §8|§r " + entry.level() +
                        (bonusLevel > 0 ? " + §l" + bonusLevel : "")).append(spellType));

                for (int i = 1; i <= spell.getDescriptionCount(); i++) {
                    tooltip.add(Component.translatable("spell." + Brutality.MOD_ID + "." + spellName + ".description." + i));
                }

                if (spell.getBaseDamage() > 0) {
                    String damageOperator = spell.getDamageLevelScaling() > 0 ? " + " : " - ";
                    tooltip.add(Component.literal("\uD83D\uDDE1 §8|§6 " + spell.getBaseDamage() + "§r ❤§6" + damageOperator + "(" +
                            Mth.abs(spell.getDamageLevelScaling()) + "§r ❤§6 * level) = §2" + spell.getFinalDamage(mc.player, actualSpellLevel) + "§r ❤"));

                }
                if (castTime > 0) {
                    String castTimeOperator = spell.getCastTimeLevelScaling() > 0 ? " + " : " - ";
                    tooltip.add(Component.literal("\uD83E\uDE84 §8|§6 " + (showCastReduct ? "(" : "") + spell.getBaseCastTime() / 20 + "s" + castTimeOperator + "(" +
                            Mth.abs(((float) spell.getCastTimeLevelScaling()) / 20) + "s * level)" + (showCastReduct ? ") * " + castTimeReduction : "") + " = §2" + castTime + "s"));
                }
                // Mana cost line
                String manaOperator = spell.getManaCostLevelScaling() > 0 ? " + " : " - ";
                tooltip.add(Component.literal("\uD83D\uDCA7 §8|§6 " + (showManaReduct ? "(" : "") + spell.getBaseManaCost() + "§r \uD83D\uDCA7§6" + manaOperator + "(" +
                        Mth.abs(spell.getManaCostLevelScaling()) + "§r \uD83D\uDCA7§6 * level)" + (showManaReduct ? ") * " + manaCostReduction : "") + " = §2" + manaCost + "§r \uD83D\uDCA7"));

                // Cooldown line
                String cdOperator = spell.getCooldownLevelScaling() > 0 ? " + " : " - ";
                tooltip.add(Component.literal("⌛ §8|§6 " + (showCDReduct ? "(" : "") + spell.getBaseCooldown() / 20 + "s" + cdOperator + "(" +
                        Mth.abs(((float) spell.getCooldownLevelScaling()) / 20) + "s * level)" + (showCDReduct ? ") * " + spellCdReduction : "") + " = §2" + spellCooldown + "s"));


                if (spell.getStatComponents() != null)
                    for (BrutalityTooltipHelper.SpellStatComponent component : spell.getStatComponents()) {
                        MutableComponent finalComponent = getMutableComponent(mc.player, entry, component);

                        if (component.min() != null) {
                            finalComponent.append(divider)
                                    .append(Component.literal("ᴍɪɴ " + computeUnit(component.min(), component.type())).withStyle(ChatFormatting.RED));
                        }

                        if (component.max() != null) {
                            finalComponent.append(divider)
                                    .append(Component.literal("ᴍᴀx " + computeUnit(component.max(), component.type())).withStyle(ChatFormatting.GREEN));
                        }

                        tooltip.add(finalComponent);

                    }


                tooltip.add(Component.empty());

            }
        } else {

            SpellStorage.SpellEntry entry = SpellStorage.getCurrentSpellEntry(stack);
            if (entry == null) return;
            IBrutalitySpell spell = entry.spell();
            String spellName = spell.getSpellName();


            AttributeInstance manaCostAttr = mc.player.getAttribute(ModAttributes.MANA_COST.get());
            AttributeInstance spellCdAttr = mc.player.getAttribute(ModAttributes.SPELL_COOLDOWN_REDUCTION.get());
            AttributeInstance castTimeAttr = mc.player.getAttribute(ModAttributes.CAST_TIME_REDUCTION.get());
            float manaCostReduction = manaCostAttr != null ? (float) manaCostAttr.getValue() : 0F;
            float spellCdReduction = 1 - (spellCdAttr != null ? (float) spellCdAttr.getValue() : 0F);
            float castTimeReduction = 1 - (castTimeAttr != null ? (float) castTimeAttr.getValue() : 0F);

            boolean showManaReduct = manaCostReduction != 1F;
            boolean showCDReduct = spellCdReduction != 1F;
            boolean showCastReduct = castTimeReduction != 1F;

            // Constants for consistent formatting
            MutableComponent divider = Component.literal(" | ").withStyle(ChatFormatting.DARK_GRAY);
            int actualSpellLevel = IBrutalitySpell.getActualSpellLevel(mc.player, spell, entry.level());
            float manaCost = IBrutalitySpell.getActualManaCost(mc.player, spell, actualSpellLevel);
            int castTime = IBrutalitySpell.getActualCastTime(mc.player, spell, actualSpellLevel);
            castTime /= 20;
            float spellCooldown = IBrutalitySpell.getActualCooldown(mc.player, spell, actualSpellLevel);
            spellCooldown /= 20;

            int bonusLevel = actualSpellLevel - entry.level();
            // Spell name and actualSpellLevel

            MutableComponent spellType = Component.literal(" §8|§r ");

            spell.getCategories().forEach(category -> {
                spellType.append(category.icon + " ");
            });

            tooltip.add(Component.translatable("spell." + Brutality.MOD_ID + "." + spellName).append(" §8|§r " + entry.level() +
                    (bonusLevel > 0 ? " + §l" + bonusLevel : "")).append(spellType));

            for (int i = 1; i <= spell.getDescriptionCount(); i++) {
                tooltip.add(Component.translatable("spell." + Brutality.MOD_ID + "." + spellName + ".description." + i));
            }

            if (spell.getBaseDamage() > 0) {
                String damageOperator = spell.getDamageLevelScaling() > 0 ? " + " : " - ";
                tooltip.add(Component.literal("\uD83D\uDDE1 §8|§6 " + spell.getBaseDamage() + "§r ❤§6" + damageOperator + "(" +
                        Mth.abs(spell.getDamageLevelScaling()) + "§r ❤§6 * level) = §2" + spell.getFinalDamage(mc.player, actualSpellLevel) + "§r ❤"));

            }
            if (castTime > 0) {
                String castTimeOperator = spell.getCastTimeLevelScaling() > 0 ? " + " : " - ";
                tooltip.add(Component.literal("\uD83E\uDE84 §8|§6 " + (showCastReduct ? "(" : "") + spell.getBaseCastTime() / 20 + "s" + castTimeOperator + "(" +
                        Mth.abs(((float) spell.getCastTimeLevelScaling()) / 20) + "s * level)" + (showCastReduct ? ") * " + castTimeReduction : "") + " = §2" + castTime + "s"));
            }
            // Mana cost line
            String manaOperator = spell.getManaCostLevelScaling() > 0 ? " + " : " - ";
            tooltip.add(Component.literal("\uD83D\uDCA7 §8|§6 " + (showManaReduct ? "(" : "") + spell.getBaseManaCost() + "§r \uD83D\uDCA7§6" + manaOperator + "(" +
                    Mth.abs(spell.getManaCostLevelScaling()) + "§r \uD83D\uDCA7§6 * level)" + (showManaReduct ? ") * " + manaCostReduction : "") + " = §2" + manaCost + "§r \uD83D\uDCA7"));

            // Cooldown line
            String cdOperator = spell.getCooldownLevelScaling() > 0 ? " + " : " - ";
            tooltip.add(Component.literal("⌛ §8|§6 " + (showCDReduct ? "(" : "") + spell.getBaseCooldown() / 20 + "s" + cdOperator + "(" +
                    Mth.abs(((float) spell.getCooldownLevelScaling()) / 20) + "s * level)" + (showCDReduct ? ") * " + spellCdReduction : "") + " = §2" + spellCooldown + "s"));



            if (spell.getStatComponents() != null)
                for (BrutalityTooltipHelper.SpellStatComponent component : spell.getStatComponents()) {
                    MutableComponent finalComponent = getMutableComponent(mc.player, entry, component);

                    if (component.min() != null) {
                        finalComponent.append(divider)
                                .append(Component.literal("ᴍɪɴ " + computeUnit(component.min(), component.type())).withStyle(ChatFormatting.RED));
                    }

                    if (component.max() != null) {
                        finalComponent.append(divider)
                                .append(Component.literal("ᴍᴀx " + computeUnit(component.max(), component.type())).withStyle(ChatFormatting.GREEN));
                    }

                    tooltip.add(finalComponent);

                }


            tooltip.add(Component.empty());

            tooltip.add(Component.literal("§8Spell " + (spells.indexOf(entry) + 1) + "/" + spells.size()));
            tooltip.add(Component.literal("§8Press ")
                    .append(mc.options.keyShift.getKey().getDisplayName()).withStyle(ChatFormatting.GRAY)
                    .append(" §8to view all Spells"));
        }
    }

    private static float computeUnit(float input, BrutalityTooltipHelper.SpellStatComponents type) {
        if (type.equals(DURATION)) {
            return input / 20;
        }
        if (type.unit.contains("❤")) {
            return input / 2;
        }
        return input;
    }

    private static @NotNull MutableComponent getMutableComponent(Player player, SpellStorage.SpellEntry entry, BrutalityTooltipHelper.SpellStatComponent component) {
        BrutalityTooltipHelper.SpellStatComponents type = component.type();
        String operand = component.levelDelta() > 0 ? " + " : " - ";
        float base = computeUnit(component.base(), type);
        float levelDelta = computeUnit(Mth.abs(component.levelDelta()), type);
        float finalValue = computeUnit(entry.spell().getFinalStat(IBrutalitySpell.getActualSpellLevel(player, entry.spell(), entry.level()), component), type);
        boolean shouldReset = !(type.equals(DURATION) || type.equals(CHANCE));

        if (component.max() != null && component.min() != null) {
            finalValue = Mth.clamp(finalValue, computeUnit(component.min(), type), computeUnit(component.max(), type));
        } else if (component.max() != null) {
            finalValue = Math.min(finalValue, computeUnit(component.max(), type));
        } else if (component.min() != null) {
            finalValue = Math.max(finalValue, computeUnit(component.min(), type));
        }


        return Component.literal(type.icon +
                " §8|§6 " + base +
                (shouldReset ? "§r" : "") + type.unit
                + "§6" + operand + "(" + levelDelta +
                (shouldReset ? "§r" : "") + type.unit
                + "§6" + " * level) = §2" + finalValue +
                (shouldReset ? "§r" : "") + type.unit);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    public void closeBook(Player player, ItemStack stack, Level level) {
        closeBook(player.getBlockX(), player.getBlockY(), player.getBlockZ(), player, stack, level);
    }

    public void closeBook(float x, float y, float z, Player player, ItemStack stack, Level level) {
        if (level instanceof ServerLevel serverLevel) {
            stopTriggeredAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "main_controller", "cast_channelling");
            triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "book_controller", "close");
            serverLevel.playSound(null, x, y, z, BrutalityModSounds.TOME_CLOSE.get(), SoundSource.PLAYERS, 1, 1);
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        closeBook(player, item, player.level());
        return super.onDroppedByPlayer(item, player);
    }
}