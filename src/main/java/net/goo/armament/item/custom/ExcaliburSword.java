package net.goo.armament.item.custom;

import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.client.renderers.item.GlowEntityTranslucentCull;
import net.goo.armament.entity.custom.ExcaliburBeam;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaSwordItem;
import net.goo.armament.registry.ModEntities;
import net.goo.armament.registry.ModSounds;
import net.goo.armament.util.ModResources;
import net.goo.armament.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.function.Consumer;

public class ExcaliburSword extends ArmaSwordItem {
    public String AURA_ACTIVE = "auraActive", CMD = "CustomModelData";

    public ExcaliburSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
        this.colors = EXCALIBUR_COLORS;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putBoolean(AURA_ACTIVE, false);
        stack.getOrCreateTag().putInt(CMD, 0);
        return stack;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public String texture(ItemStack stack) {
        if (stack.getOrCreateTag().getBoolean(AURA_ACTIVE)) {
            return "excalibur_active";
        }
        return super.texture(stack);
    }

    @Override
    public Component getName(ItemStack pStack) {
        Level pLevel = Minecraft.getInstance().level;
        String name;
        if (pStack.getOrCreateTag().getBoolean(AURA_ACTIVE)) name = "excalibur_active";
        else name = "excalibur";

        return ModUtils.tooltipHelper("item.armament." + name, false, getFontFromCategory(category), pLevel.getGameTime(), 0.5F, 2, colors);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("rarity.armament." + rarity).withStyle(Style.EMPTY.withFont(ModResources.RARITY)));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.excalibur.lore", false, null, colors[1]));
        pTooltipComponents.add(Component.literal(""));

        for (int i = 1; i <= abilityCount; i++) {
            pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.excalibur.ability.name." + i, false, null, colors[0]));
            pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.excalibur.ability.desc." + i, false, null, colors[1]));
            pTooltipComponents.add(Component.literal(""));
        }
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide && pPlayer.isCrouching()) {
            ItemStack stack = pPlayer.getItemInHand(pUsedHand);
            CompoundTag tag = stack.getOrCreateTag();
            tag.putBoolean(AURA_ACTIVE, !tag.getBoolean(AURA_ACTIVE));
            tag.putInt(CMD, tag.getBoolean(AURA_ACTIVE) ? 1 : 0);
            if (tag.getBoolean(AURA_ACTIVE)) {
                ModUtils.replaceOrAddModifier(stack, Attributes.ATTACK_DAMAGE, BASE_ATTACK_DAMAGE_UUID, 11, EquipmentSlot.MAINHAND);
                ModUtils.replaceOrAddModifier(stack, ForgeMod.ENTITY_REACH.get(), BASE_ENTITY_INTERACTION_RANGE_UUID, 5, EquipmentSlot.MAINHAND);
            } else {
                ModUtils.removeModifier(stack, BASE_ATTACK_DAMAGE_UUID);
                ModUtils.removeModifier(stack, BASE_ENTITY_INTERACTION_RANGE_UUID);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && !player.level().isClientSide) {
            Level level = player.level();
            Item item = stack.getItem();
            if (!player.getCooldowns().isOnCooldown(item) && stack.getOrCreateTag().getBoolean(AURA_ACTIVE)) {
                player.getCooldowns().addCooldown(item, 60);
                level.playSound(player, player.getOnPos(), ModSounds.TERRA_BLADE_USE.get(), SoundSource.PLAYERS);
                shootProjectile(() -> new ExcaliburBeam(ModEntities.EXCALIBUR_BEAM.get(), level), player, level, 0.25F);
                stack.hurtAndBreak(1, player, pPlayer -> player.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
        }
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public <T extends Item & ArmaGeoItem, R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, GlowEntityTranslucentCull.class);
    }

    int tickCount;

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pIsSelected && pStack.getOrCreateTag().getBoolean(AURA_ACTIVE) && pEntity instanceof Player player) {
            tickCount++;

            List<Entity> nearbyEntities = pLevel.getEntities(pEntity, pEntity.getBoundingBox().inflate(5),
                    e -> e instanceof LivingEntity && !(e instanceof Player && (e.isSpectator() || ((Player) e).isCreative()) ));

            if (!nearbyEntities.isEmpty()) {
                for (Entity entity : nearbyEntities) {
                    if (entity != pEntity) {
                        ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 2, false, false));
                    }
                }
            }

            if (tickCount % 3 == 0) {
                pStack.hurtAndBreak(1, player, pPlayer -> player.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
        } else tickCount = 0;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}
