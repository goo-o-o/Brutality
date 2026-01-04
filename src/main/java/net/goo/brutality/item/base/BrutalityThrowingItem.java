package net.goo.brutality.item.base;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.Brutality;
import net.goo.brutality.event.mod.client.BrutalityModItemRenderManager;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.SealUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.goo.brutality.util.helpers.ThrowableWeaponHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.List;
import java.util.function.Consumer;

public class BrutalityThrowingItem extends Item implements BrutalityGeoItem {
    protected Rarity rarity;
    protected List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public BrutalityThrowingItem(int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(new Item.Properties());

        this.rarity = rarity;
        this.descriptionComponents = descriptionComponents;
        float attackDamage = (float) pAttackDamageModifier;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", pAttackSpeedModifier, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();

    }

    public void handleThrowPacket(ItemStack stack, Player player) {
        EntityType<? extends Projectile> entityType = this.getThrownEntity();
        Level level = player.level();
        Projectile projectile = entityType.create(level);

        if (projectile != null) {
            projectile.setPos(player.getEyePosition());
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, getThrowVelocity(player), getThrowInaccuracy());
            projectile.setOwner(player);

            handleSealType(projectile, stack);

            level.addFreshEntity(projectile);
        }
    }

    protected void handleSealType(Projectile projectile, ItemStack stack) {
        SealUtils.SEAL_TYPE sealType = SealUtils.getSealType(stack);
        if (sealType != null) {
            projectile.getCapability(BrutalityCapabilities.SEAL_TYPE_CAP).ifPresent(cap -> cap.setSealType(sealType));
        }
    }

    public ResourceLocation getAnimationResourceLocation() {
        return THROW_ANIMATION.SWING.getAnimationResource();
    }

    protected enum THROW_ANIMATION {
        SWING(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "throw_swing")),
        DROP(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "throw_drop"));

        private final ResourceLocation animation;

        THROW_ANIMATION(ResourceLocation animation) {
            this.animation = animation;
        }

        public ResourceLocation getAnimationResource() {
            return animation;
        }
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack pStack) {
        return this.rarity;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return BrutalityModItemRenderManager.createRenderer(BrutalityThrowingItem.this);
            }
        });
    }


    @Override
    public BrutalityCategories.AttackType getAttackType() {
        return BrutalityCategories.AttackType.SLASH;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public @NotNull Component getName(ItemStack pStack) {
        return brutalityNameHandler(pStack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        brutalityTooltipHandler(pStack, pTooltipComponents, descriptionComponents, rarity);
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }


    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    public EntityType<? extends Projectile> getThrownEntity() {
        return EntityType.ARROW;
    }

    public float getInitialThrowVelocity() {
        return 1;
    }

    public float getThrowVelocity(Player player) {
        return (float) (getInitialThrowVelocity() * player.getAttributeValue(ModAttributes.THROW_STRENGTH.get()));
    }

    @Override
    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        return false;
    }


    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        return true;
    }

    public float getThrowInaccuracy() {
        return 0;
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.ItemType.THROWING;
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    /**
     * Should be called Clientside
     */
    public void handleAttributesAndAnimation(Player player, ItemStack stack, boolean isOffhand) {
        ThrowableWeaponHelper.handleAttributesAndAnimation(player, this, stack, isOffhand);
    }

    public void handleCooldownAndSound(Player player, ItemStack stack) {
        ThrowableWeaponHelper.handleCooldownAndSound(player, stack, this);
    }

}
