//package net.goo.brutality.item.weapon.sword;
//
//import net.goo.brutality.client.renderers.item.BrutalityItemRenderer;
//import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightNoDepthLayer;
//import net.goo.brutality.entity.projectile.beam.ExcaliburBeam;
//import net.goo.brutality.item.base.BrutalityGeoItem;
//import net.goo.brutality.item.base.BrutalitySwordItem;
//import net.goo.brutality.network.PacketHandler;
//import net.goo.brutality.network.c2sShootProjectilePacket;
//import net.goo.brutality.registry.BrutalityModEntities;
//import net.goo.brutality.util.helpers.AttributeHelper;
//import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
//import net.goo.brutality.util.helpers.ProjectileHelper;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.StringTag;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.effect.MobEffects;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.AttributeModifier;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Rarity;
//import net.minecraft.world.item.Tier;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.common.ForgeMod;
//import net.minecraftforge.fml.ModList;
//import software.bernie.geckolib.core.animation.AnimatableManager;
//
//import java.util.List;
//import java.util.UUID;
//
//import static net.goo.brutality.util.helpers.AttributeHelper.removeModifier;
//import static net.goo.brutality.util.helpers.AttributeHelper.setAttackDamageBonus;
//
//public class ExcaliburSword extends BrutalitySwordItem {
//    public static String AURA_ACTIVE = "auraActive";
//
//    private final UUID EXCALIBUR_INTERACTION_RANGE_UUID = UUID.fromString("a9505c16-a23b-4dd5-801f-e0a43ba23e38");
//
//    public ExcaliburSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
//        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
//    }
//
//
//    @Override
//    public ItemStack getDefaultInstance() {
//        ItemStack stack = new ItemStack(this);
//        stack.getOrCreateTag().putBoolean(AURA_ACTIVE, false);
//        stack.getOrCreateTag().putInt(CUSTOM_MODEL_DATA, 0);
//        return stack;
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//    }
//
//    @Override
//    public String model(ItemStack stack) {
//        if (stack.getOrCreateTag().getBoolean(AURA_ACTIVE)) {
//            return "excalibur_active";
//        }
//        return super.model(stack);
//    }
//
//    @Override
//    public String texture(ItemStack stack) {
//        return super.texture(stack);
//    }
//
//    @Override
//    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
//        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
//
//        if (pPlayer.getCooldowns().isOnCooldown(stack.getItem())) return InteractionResultHolder.fail(stack);
//
//        if (!pLevel.isClientSide() && pPlayer.isCrouching()) {
//            CompoundTag tag = stack.getOrCreateTag();
//            tag.putBoolean(AURA_ACTIVE, !tag.getBoolean(AURA_ACTIVE));
//            tag.putInt(CUSTOM_MODEL_DATA, tag.getBoolean(AURA_ACTIVE) ? 1 : 0);
//            if (tag.getBoolean(AURA_ACTIVE)) {
//
//                setAttackDamageBonus(stack, 8);
//
//                if (!ModList.get().isLoaded("bettercombat"))
//                    AttributeHelper.replaceOrAddModifier(stack, ForgeMod.ENTITY_REACH.get(), EXCALIBUR_INTERACTION_RANGE_UUID, 5, EquipmentSlot.MAINHAND, AttributeModifier.Operation.ADDITION);
//                else {
//                    CompoundTag jsonTag = new CompoundTag();
//                    jsonTag.putString("parent", "bettercombat:claymore");
//
//                    CompoundTag attributes = new CompoundTag();
//                    attributes.putInt("attack_range", 10);
//                    jsonTag.put("attributes", attributes);
//
//                    tag.put("weapon_attributes", StringTag.valueOf(jsonTag.toString()));
//                }
//            } else {
//                if (!ModList.get().isLoaded("bettercombat"))
//                    removeModifier(stack, EXCALIBUR_INTERACTION_RANGE_UUID);
//                else tag.remove("weapon_attributes");
//
//                setAttackDamageBonus(stack, 0);
//
//            }
//
//            return super.use(pLevel, pPlayer, pUsedHand);
//
//        }
//        return InteractionResultHolder.fail(stack);
//    }
//
//    @Override
//    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
//        if (entity instanceof Player player && !player.level().isClientSide && !ModList.get().isLoaded("bettercombat")) {
//            performExcaliburBeam(stack, player);
//        }
//        return super.onEntitySwing(stack, entity);
//    }
//
//    public void performExcaliburBeam(ItemStack stack, Player player) {
//        Level level = player.level();
//        Item item = stack.getItem();
//        if (!player.getCooldowns().isOnCooldown(item) && stack.getOrCreateTag().getBoolean(AURA_ACTIVE)) {
//            player.getCooldowns().addCooldown(item, 60);
//            if (level.isClientSide()) {
//                PacketHandler.sendToServer(new c2sShootProjectilePacket(BrutalityModEntities.EXCALIBUR_BEAM.getId(), 0.25F, false, 0F, 0));
//            } else {
//                ProjectileHelper.shootProjectile(() -> new ExcaliburBeam(BrutalityModEntities.EXCALIBUR_BEAM.get(), level), player, level, 0.25F, false, 0F, 0);
//            }
//        }
//    }
//
//
//
//    @Override
//    public <T extends Item & BrutalityGeoItem> void configureLayers(BrutalityItemRenderer<T> renderer) {
//        super.configureLayers(renderer);
//        renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
//    }
//
//    @Override
//    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
//        if (!pLevel.isClientSide()) {
//            if (pEntity instanceof Player player && pIsSelected) {
//                if (pStack.getOrCreateTag().getBoolean(AURA_ACTIVE)) {
//
//                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 4, true, false), player);
//                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 2, 3, true, false), player);
//
//                }
//            }
//        }
//    }
//
//    @Override
//    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
//        return false;
//    }
//
//}
