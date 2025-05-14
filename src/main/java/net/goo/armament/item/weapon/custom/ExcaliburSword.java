package net.goo.armament.item.weapon.custom;

import net.goo.armament.client.renderers.item.ArmaAutoFullbrightItemRenderer;
import net.goo.armament.entity.custom.beam.ExcaliburBeam;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.weapon.base.ArmaSwordItem;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.network.c2sSwordBeamPacket;
import net.goo.armament.registry.ModEntities;
import net.goo.armament.util.helpers.AttributeHelper;
import net.goo.armament.util.helpers.ModTooltipHelper;
import net.goo.armament.util.helpers.ProjectileHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.function.Consumer;

import static net.goo.armament.util.helpers.AttributeHelper.removeModifier;
import static net.goo.armament.util.helpers.AttributeHelper.setAttackDamageBonus;

public class ExcaliburSword extends ArmaSwordItem {
    public static String AURA_ACTIVE = "auraActive";
    public String CMD = "CustomModelData";

    public ExcaliburSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
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
    public String model(ItemStack stack) {
        if (stack.getOrCreateTag().getBoolean(AURA_ACTIVE)) {
            return "excalibur_active";
        }
        return super.model(stack);
    }

    @Override
    public String texture(ItemStack stack) {
        return this.identifier;
    }

    @Override
    public Component getName(ItemStack pStack) {
        Level pLevel = Minecraft.getInstance().level;
        String name;
        if (pStack.getOrCreateTag().getBoolean(AURA_ACTIVE)) name = "excalibur_active";
        else name = "excalibur";

        return ModTooltipHelper.tooltipHelper("item.armament." + name, false, getFontFromCategory(category), pLevel.getGameTime(), 0.5F, 2, BASE_COLOR_MAP.get(this.getClass()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (pPlayer.getCooldowns().isOnCooldown(stack.getItem())) return InteractionResultHolder.fail(stack);

        if (!pLevel.isClientSide() && pPlayer.isCrouching()) {
            CompoundTag tag = stack.getOrCreateTag();
            tag.putBoolean(AURA_ACTIVE, !tag.getBoolean(AURA_ACTIVE));
            tag.putInt(CMD, tag.getBoolean(AURA_ACTIVE) ? 1 : 0);
            if (tag.getBoolean(AURA_ACTIVE)) {

                setAttackDamageBonus(stack, 8);

                if (!BETTER_COMBAT_LOADED)
                    AttributeHelper.replaceOrAddModifier(stack, ForgeMod.ENTITY_REACH.get(), BASE_ENTITY_INTERACTION_RANGE_UUID, 5, EquipmentSlot.MAINHAND, AttributeModifier.Operation.ADDITION);
                else {
                    CompoundTag jsonTag = new CompoundTag();
                    jsonTag.putString("parent", "bettercombat:claymore");

                    CompoundTag attributes = new CompoundTag();
                    attributes.putInt("attack_range", 10);
                    jsonTag.put("attributes", attributes);

                    tag.put("weapon_attributes", StringTag.valueOf(jsonTag.toString()));
                }
            } else {
                if (!BETTER_COMBAT_LOADED)
                    removeModifier(stack, BASE_ENTITY_INTERACTION_RANGE_UUID);
                else tag.remove("weapon_attributes");

                setAttackDamageBonus(stack, 0);

            }

            return super.use(pLevel, pPlayer, pUsedHand);

        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && !player.level().isClientSide && !BETTER_COMBAT_LOADED) {
            performExcaliburBeam(stack, player);
        }
        return super.onEntitySwing(stack, entity);
    }

    public static void performExcaliburBeam(ItemStack stack, Player player) {
        Level level = player.level();
        Item item = stack.getItem();
        if (!player.getCooldowns().isOnCooldown(item) && stack.getOrCreateTag().getBoolean(AURA_ACTIVE)) {
            player.getCooldowns().addCooldown(item, 60);
            if (level.isClientSide()) {
                PacketHandler.sendToServer(new c2sSwordBeamPacket(BEAM_TYPES.EXCALIBUR));
            } else {
                ProjectileHelper.shootProjectile(() -> new ExcaliburBeam(ModEntities.EXCALIBUR_BEAM.get(), level), player, level, 0.25F);
            }
        }
    }

    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, ArmaAutoFullbrightItemRenderer.class);
    }


    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide()) {
            if (pEntity instanceof Player player && pIsSelected) {
                if (pStack.getOrCreateTag().getBoolean(AURA_ACTIVE)) {

                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 4, true, false), player);
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 2, 3, true, false), player);

//                    List<Entity> nearbyEntities = pLevel.getEntities(pEntity, pEntity.getBoundingBox().inflate(5),
//                            e -> e instanceof LivingEntity && !(e instanceof Player && (e.isSpectator() || ((Player) e).isCreative())));
//
//                    if (!nearbyEntities.isEmpty()) {
//                        for (Entity entity : nearbyEntities) {
//                            if (entity != pEntity) {
//                                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 1, false, false));
//                            }
//                        }
//                    }
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

}
