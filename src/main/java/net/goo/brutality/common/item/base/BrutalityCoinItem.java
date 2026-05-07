package net.goo.brutality.common.item.base;

import com.github.stephengold.joltjni.BodyCreationSettings;
import com.github.stephengold.joltjni.enumerate.EMotionType;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.common.velthoric.CoinContactListener;
import net.goo.brutality.common.velthoric.bodies.CoinRigidBody;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.build_archetypes.CoinHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.goo.brutality.util.tooltip.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xmx.velthoric.physics.VxPhysicsLayers;
import net.xmx.velthoric.physics.body.type.VxBody;
import net.xmx.velthoric.physics.world.VxPhysicsWorld;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BrutalityCoinItem extends Item {
    protected final int cooldownTime;
    protected List<ItemDescriptionComponent> descriptionComponents = List.of();
    public BodyCreationSettings bcs = new BodyCreationSettings();

    public BrutalityCoinItem(Properties pProperties, int cooldownTime) {
        super(pProperties);
        this.cooldownTime = cooldownTime;
        bcs.setMotionType(EMotionType.Dynamic);
        bcs.setObjectLayer(VxPhysicsLayers.MOVING); // Makes it collide with other dynamic objects and terrain.
        bcs.setRestitution(0.2f); // Bounciness
        bcs.setFriction(0.5f);
        bcs.setGravityFactor(2);
    }

    public BrutalityCoinItem(Properties pProperties, int cooldownTime, List<ItemDescriptionComponent> descriptionComponents) {
        this(pProperties, cooldownTime);
        this.descriptionComponents = descriptionComponents;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {



        TooltipHelper.handleItemDescriptions(pStack, pTooltipComponents, descriptionComponents);
        pTooltipComponents.add(Component.empty());

        pTooltipComponents.add(Component.translatable(Brutality.MOD_ID + ".description.type.on_right_click"));
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".coin_item.on_right_click.1"));
        if (Minecraft.getInstance().player != null) {
            pTooltipComponents.add(TooltipHelper.getCooldownComponent(getCooldownTime(Minecraft.getInstance().player)).withStyle(ChatFormatting.DARK_AQUA));
        } else {
            pTooltipComponents.add(TooltipHelper.getCooldownComponent(cooldownTime).withStyle(ChatFormatting.DARK_AQUA));
        }
    }

    /**
     * @return True to spawn {@link net.goo.brutality.client.particle.custom.CoinflipParticle}, false to not
     */
    public boolean spawnParticles() {
        return true;
    }


    protected void playBuffSounds(Player player) {
        player.level().playSound(null, player.getX(), player.getY(0.5), player.getZ(), ModUtils.getRandomSound(BrutalitySounds.RETRO_POSITIVE), SoundSource.PLAYERS, 1, Mth.nextFloat(player.getRandom(), 0.8F, 1.2F));
    }

    protected void playDebuffSounds(Player player) {
        player.level().playSound(null, player.getX(), player.getY(0.5), player.getZ(), ModUtils.getRandomSound(BrutalitySounds.RETRO_NEGATIVE), SoundSource.PLAYERS, 1, Mth.nextFloat(player.getRandom(), 0.8F, 1.2F));
    }

    public boolean shouldPlayImpactSounds() {
        return true;
    }

    public void playCoinTossSounds(Player pPlayer, ServerLevel serverLevel) {
        if (shouldPlayCoinTossSounds()) {
            serverLevel.playSound(null, pPlayer.getX(), pPlayer.getY(0.5F), pPlayer.getZ(), ModUtils.getRandomSound(BrutalitySounds.COIN_FLIP), SoundSource.PLAYERS, 1, Mth.nextFloat(pPlayer.getRandom(), 0.8F, 1.2F));
        }
    }

    /**
     * @return True if should play sounds when throwing coins
     */
    public boolean shouldPlayCoinTossSounds() {
        return true;
    }


    public int getCooldownTime(Player player) {
        return (int) (cooldownTime * player.getAttributeValue(BrutalityAttributes.COIN_COOLDOWN.get()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (pLevel instanceof ServerLevel serverLevel) {
            if (!pPlayer.getCooldowns().isOnCooldown(stack.getItem())) {
                VxPhysicsWorld world = VxPhysicsWorld.get(serverLevel.dimension());

                if (world != null && world.isRunning()) {
                    pPlayer.swing(pUsedHand, true);
                    playCoinTossSounds(pPlayer, serverLevel);

                    pPlayer.getCooldowns().addCooldown(stack.getItem(), getCooldownTime(pPlayer));

                    CoinHelper.spawnAndLaunchCoin(this, pPlayer, stack, world);


                    return InteractionResultHolder.sidedSuccess(stack, false);
                }
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    protected abstract float getBasePixelDiameter();

    public float getDiameter(Player player, ItemStack coinStack) {
        return getBasePixelDiameter() * 0.03125F * getPhysicsAndRenderScale(player, coinStack);
    }

    public float getPhysicsAndRenderScale(@Nullable Player player, ItemStack coinStack) {
        return 1;
    }


    /**
     * Triggered when a coin flip lands on heads. Provides the player and the item stack involved in the flip.
     *
     * @param player   The {@link Player} who initiated the coin flip. This provides information such as player state and context.
     * @param stack    The {@link ItemStack} representing the coin used in the flip. Contains details like the item's properties and state.
     * @param location The location at which the coin landed
     */
    public abstract void onHeads(Player player, ItemStack stack, Vec3 location);

    /**
     * Triggered when the Brutality Coin lands on tails after being used.
     * This method is abstract and must be implemented to define the specific behavior
     * that occurs when the tails side is the result of a coin flip.
     *
     * @param player   The {@link Player} who initiated the coin flip.
     * @param stack    The {@link ItemStack} representing the Brutality Coin item being used.
     * @param location The location at which the coin landed
     */
    public abstract void onTails(Player player, ItemStack stack, Vec3 location);

    /**
     * Triggered when the coin item collides with another object in the physics world.
     * Handles collision-specific behavior depending on collision type and interacting entities.
     *
     * @param coinBody The {@link CoinRigidBody} representing the physical body of the coin
     *                 in the physics simulation. Provides details about the coin's state
     *                 and properties during the collision.
     * @param stack    The {@link ItemStack} representing the coin item. Includes data
     *                 on the item's state, such as enchantments or metadata.
     * @param other    The {@link VxBody} that the coin collided with. Represents the
     *                 other physical object in the simulation and can provide additional
     *                 details about what the coin interacted with.
     * @param type     The {@link CoinContactListener.CollisionType} indicating the type
     *                 of collision that occurred, such as surface type or impact category.
     *                 Use this to determine specific responses to different collision scenarios.
     */
    public void onCollide(CoinRigidBody coinBody, ItemStack stack, @Nullable VxBody other, CoinContactListener.CollisionType type) {
    }


    /**
     * Called when a coin lands on its edge after being flipped.
     * This method is meant to define the behavior when the improbable edge-land scenario occurs.
     *
     * @param player The {@link Player} who initiated the coin flip.
     * @param stack  The {@link ItemStack} of the coin being used.
     */
//    public abstract void onEdge(Player player, ItemStack stack);
}