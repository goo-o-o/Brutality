package net.goo.brutality.common.item.weapon.tome;

import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.common.item.base.BrutalityMagicItem;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.util.magic.SpellCastingHandler;
import net.goo.brutality.util.magic.SpellCooldownTracker;
import net.goo.brutality.util.magic.SpellStorage;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;

public abstract class BaseMagicTome extends BrutalityMagicItem {


    public BaseMagicTome(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents, int baseSpellSlots, int baseAugmentSlots) {
        super(rarity, descriptionComponents, baseSpellSlots, baseAugmentSlots);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.type = MagicItemType.TOME;
    }

    public BaseMagicTome(Rarity rarity, int baseSpellSlots, int baseAugmentSlots) {
        this(rarity, List.of(), baseSpellSlots, baseAugmentSlots);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.getOrCreateTag().putBoolean("closed", true);
        return stack;
    }


    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.ItemType.TOME;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return 72000;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        SpellStorage.SpellEntry spellEntry = SpellStorage.getCurrentSpellEntry(stack);
        if (spellEntry == null) {
            return InteractionResultHolder.pass(stack);
        }

        BrutalitySpell spell = spellEntry.spell();
        int actualSpellLevel = spell.getActualSpellLevel(player, spellEntry.level());
        List<IBrutalitySpell.SpellCategory> categories = spell.getCategories();


        if (SpellCastingHandler.hasEnoughManaToCast(player, spell, spellEntry.level()) && !SpellCooldownTracker.isOnCooldown(player, spell)) {
            if (categories.contains(IBrutalitySpell.SpellCategory.CHANNELLING)) {
                player.startUsingItem(hand);
                // Redirect to useTick
                if (level instanceof ServerLevel serverLevel) {
                    stack.getOrCreateTag().putBoolean("closed", false);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), BrutalitySounds.TOME_OPEN.get(), SoundSource.PLAYERS, 1, 1);
                    stopTriggeredAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "book_controller", "close");
                    triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "main_controller", "cast_channelling");
                }
                return InteractionResultHolder.pass(stack);
            } else if (categories.contains(IBrutalitySpell.SpellCategory.CONTINUOUS)) {
                if (SpellCastingHandler.startContinuousCast(player, stack, spell, actualSpellLevel)) {
                    player.startUsingItem(hand);
                    if (level instanceof ServerLevel serverLevel) {
                        stack.getOrCreateTag().putBoolean("closed", false);
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), BrutalitySounds.TOME_OPEN.get(), SoundSource.PLAYERS, 1, 1);
                        stopTriggeredAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "book_controller", "close");
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "main_controller", "cast_continuous");
                    }
                    return InteractionResultHolder.pass(stack);
                }
            } else if (categories.contains(IBrutalitySpell.SpellCategory.INSTANT)) {
                if (SpellCastingHandler.castInstantSpell(player, stack, spell, actualSpellLevel)) {
                    if (level instanceof ServerLevel serverLevel) {
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), TerramityModSounds.TOMEUSE.get(), SoundSource.PLAYERS, 1, 1);
                        stopTriggeredAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "book_controller", "close");
                        triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "main_controller", "cast_instant");
                        player.getCooldowns().addCooldown(this, 30);
                    }
                    return InteractionResultHolder.pass(stack);
                }
            }
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity livingEntity, @NotNull ItemStack stack, int remainingTicks) {
        if (!(livingEntity instanceof Player player)) return;
        SpellStorage.SpellEntry spellEntry = SpellStorage.getCurrentSpellEntry(stack);
        if (spellEntry == null) return;

        BrutalitySpell spell = spellEntry.spell();
        int spellLevel = spell.getActualSpellLevel(player, spellEntry.level());

        if (spell.getCategories().contains(IBrutalitySpell.SpellCategory.CHANNELLING)) {
            SpellCastingHandler.castChannellingSpell(player, stack, spell, spellLevel, remainingTicks);

        } else if (spell.getCategories().contains(IBrutalitySpell.SpellCategory.CONTINUOUS)) {
            SpellCastingHandler.tickContinuousCast(player, stack, spell, spellLevel);
        }
    }


    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity, int ticksUsed) {
        if (!(livingEntity instanceof Player player)) return;
        SpellStorage.SpellEntry spellEntry = SpellStorage.getCurrentSpellEntry(stack);
        if (spellEntry == null) return;

        BrutalitySpell spell = spellEntry.spell();
        int spellLevel = spell.getActualSpellLevel(player, spellEntry.level());

        if (spell.getCategories().contains(IBrutalitySpell.SpellCategory.CONTINUOUS)) {
            SpellCastingHandler.endContinuousCast(player, stack, spell, spellLevel);
        }
        if (!level.isClientSide()) {
            closeBook(player, stack);
            player.getCooldowns().addCooldown(this, 30);
        }
    }


    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean isSelected) {
        if (!isSelected && entity instanceof Player player)
            tryCloseBook(player, stack);
    }


    @Override
    public boolean onDroppedByPlayer(ItemStack stack, Player player) {
        tryCloseBook(player, stack);
        return super.onDroppedByPlayer(stack, player);
    }

    public void closeBook(Player player, ItemStack stack) {
        Level level = player.level();
        SpellStorage.SpellEntry spellEntry = SpellStorage.getCurrentSpellEntry(stack);
        if (spellEntry != null) {
            spellEntry.spell().onEndCast(player, stack, spellEntry.spell().getActualSpellLevel(player, spellEntry.level()));
        }
        if (!level.isClientSide()) {
            stack.getOrCreateTag().putBoolean("closed", true);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), BrutalitySounds.TOME_CLOSE.get(), SoundSource.PLAYERS, 1, 1);
            stopTriggeredAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel) level), "main_controller", "cast_channelling");
            stopTriggeredAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel) level), "main_controller", "cast_continuous");
            triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel) level), "book_controller", "close");
        }
    }

    public void tryCloseBook(Player player, ItemStack stack) {
        if (!stack.getOrCreateTag().getBoolean("closed")) {
            closeBook(player, stack);
        }
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main_controller", state -> PlayState.STOP)
                .triggerableAnim("cast_instant", RawAnimation.begin().thenPlay("cast_instant"))
                .triggerableAnim("cast_channelling", RawAnimation.begin().thenPlayAndHold("cast_channelling"))
                .triggerableAnim("cast_continuous", RawAnimation.begin().thenPlayAndHold("cast_continuous")));
        controllers.add(new AnimationController<>(this, "book_controller", state -> PlayState.STOP)
                .triggerableAnim("close", RawAnimation.begin().thenPlayAndHold("close")));
    }

}