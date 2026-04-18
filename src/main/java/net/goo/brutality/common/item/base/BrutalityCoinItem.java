package net.goo.brutality.common.item.base;

import com.github.stephengold.joltjni.BodyInterface;
import com.github.stephengold.joltjni.Quat;
import com.github.stephengold.joltjni.RVec3;
import com.github.stephengold.joltjni.enumerate.EActivation;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.common.registry.BrutalityPhysicsBodies;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.common.velthoric.bodies.CoinRigidBody;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.goo.brutality.util.tooltip.TooltipHelper;
import net.minecraft.ChatFormatting;
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
import net.xmx.velthoric.math.VxTransform;
import net.xmx.velthoric.physics.world.VxPhysicsWorld;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BrutalityCoinItem extends Item {
    public final int cooldownTime;
    protected List<ItemDescriptionComponent> descriptionComponents = List.of();

    public BrutalityCoinItem(Properties pProperties, int cooldownTime) {
        super(pProperties);
        this.cooldownTime = cooldownTime;
    }

    public BrutalityCoinItem(Properties pProperties, int cooldownTime, List<ItemDescriptionComponent> descriptionComponents) {
        super(pProperties);
        this.cooldownTime = cooldownTime;
        this.descriptionComponents = descriptionComponents;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable(Brutality.MOD_ID + ".description.type.on_right_click"));
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".coin_item.on_right_click.1"));
        pTooltipComponents.add(TooltipHelper.getCooldownComponent(this.cooldownTime).withStyle(ChatFormatting.DARK_AQUA));
        pTooltipComponents.add(Component.empty());

        TooltipHelper.handleItemDescriptions(pStack, pTooltipComponents, descriptionComponents);
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


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (pLevel instanceof ServerLevel serverLevel) {
            if (!pPlayer.getCooldowns().isOnCooldown(stack.getItem())) {
                VxPhysicsWorld world = VxPhysicsWorld.get(serverLevel.dimension());

                if (world != null && world.isRunning()) {
                    pPlayer.swing(pUsedHand, true);
                    playCoinTossSounds(pPlayer, serverLevel);

                    pPlayer.getCooldowns().addCooldown(stack.getItem(), cooldownTime);

                    spawnAndLaunchCoin(pPlayer, stack, world);


                    return InteractionResultHolder.sidedSuccess(stack, false);
                }
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    protected abstract float getBasePixelDiameter();

    protected float getDiameter(Player player, ItemStack coinStack) {
        return getBasePixelDiameter() * 0.03125F * getPhysicsAndRenderScale(player, coinStack);
    }
    public void spawnAndLaunchCoin(Player player, ItemStack stack, VxPhysicsWorld physicsWorld) {
        spawnAndLaunchCoin(player, stack, physicsWorld, 20);
    }

    public void spawnAndLaunchCoin(Player player, ItemStack stack, VxPhysicsWorld physicsWorld, float yawThreshold) {
        physicsWorld.execute(() -> actuallySpawnAndLaunchCoin(player, stack, physicsWorld, yawThreshold));


        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityItems.OVERDRAW_POUCH.get())) {
                List<ItemStack> coinItemsOffCooldown = player.getInventory().items.stream().filter(itemStack -> itemStack.getItem() instanceof BrutalityCoinItem coinItem && !player.getCooldowns().isOnCooldown(coinItem)).toList();

                if (!coinItemsOffCooldown.isEmpty()) {
                    ItemStack coinItemOffCooldown = coinItemsOffCooldown.get(player.getRandom().nextInt(coinItemsOffCooldown.size()));
                    physicsWorld.execute(() -> actuallySpawnAndLaunchCoin(player, coinItemOffCooldown, physicsWorld));
                    BrutalityCoinItem coinItem = (BrutalityCoinItem) coinItemOffCooldown.getItem();
                    player.getCooldowns().addCooldown(coinItem, coinItem.cooldownTime);
                }

            }

            if (handler.isEquipped(BrutalityItems.MIRRORED_MINT.get())) {
                physicsWorld.execute(() -> actuallySpawnAndLaunchCoin(player, stack, physicsWorld));
            }

        });
    }

    public float getPhysicsAndRenderScale(@Nullable Player player, ItemStack coinStack) {
        return 1;
    }

    public void actuallySpawnAndLaunchCoin(Player player, ItemStack coinStack, VxPhysicsWorld physicsWorld) {
        actuallySpawnAndLaunchCoin(player, coinStack, physicsWorld, 20);
    }

    public void actuallySpawnAndLaunchCoin(Player player, ItemStack coinStack, VxPhysicsWorld physicsWorld, float yawThreshold) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // 1. Calculate Spawn Position
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        Vec3 spawnPosMc = eyePos.add(lookVec.scale(0.8)); // Close to player for a 'toss' feel

        VxTransform transform = new VxTransform(
                new RVec3(spawnPosMc.x, spawnPosMc.y, spawnPosMc.z),
                Quat.sIdentity()
        );

        // 2. Create the Body (Forcing Activation)
        CoinRigidBody coinBody = physicsWorld.getBodyManager().createRigidBody(
                BrutalityPhysicsBodies.COIN,
                transform,
                EActivation.Activate,
                coin -> {
                    coin.setCoin(coinStack);
                    coin.setOwner(player);
                    coin.setServerData(CoinRigidBody.DATA_DIAMETER, this.getDiameter(player, coinStack));
                }
        );

        if (coinBody == null) return;

        BodyInterface bodyInterface = physicsWorld.getPhysicsSystem().getBodyInterface();
        int bodyId = coinBody.getBodyId();

        // 3. Calculate an Ideal Launch Pitch (Arc)
        // If the player is looking within a 'natural' toss range (approx -20 to -70 pitch), use theirs.
        // Otherwise, pick a random 'perfect arc' angle (e.g., -45 degrees).
        float playerPitch = player.getXRot();
        float launchPitch;
        if (playerPitch < -35F && playerPitch > -60F) {
            launchPitch = playerPitch;
        } else {
            launchPitch = -60F + random.nextInt(-10, 11); // Random arc between 30 and 60 degrees up
        }
        float yawRange = yawThreshold * 0.5F;
        // Convert pitch/yaw to a launch vector
        float f = -Mth.sin((player.getYRot() + random.nextFloat(-yawRange, yawRange)) * ((float) Math.PI / 180F)) * Mth.cos(launchPitch * ((float) Math.PI / 180F));
        float f1 = -Mth.sin(launchPitch * ((float) Math.PI / 180F));
        float f2 = Mth.cos((player.getYRot() + random.nextFloat(-yawRange, yawRange)) * ((float) Math.PI / 180F)) * Mth.cos(launchPitch * ((float) Math.PI / 180F));

        float strength = random.nextFloat(3, 6);
        com.github.stephengold.joltjni.Vec3 launchVelocity = new com.github.stephengold.joltjni.Vec3(f * strength, f1 * strength * 2, f2 * strength);

        double yawRad = player.getYRot() * (Math.PI / 180.0);
        Vec3 rightVec = new Vec3(-Math.cos(yawRad), 0, -Math.sin(yawRad)).normalize();

        float spinSpeed = random.nextInt(25, 50); // Rapid flipping
        spinSpeed = random.nextBoolean() ? spinSpeed : -spinSpeed;
        com.github.stephengold.joltjni.Vec3 angularVel = new com.github.stephengold.joltjni.Vec3(
                (float) rightVec.x * spinSpeed,
                random.nextFloat() * 2.0f, // Tiny bit of random wobble on Y
                (float) rightVec.z * spinSpeed
        );

        // 5. Apply Physics
        bodyInterface.setLinearAndAngularVelocity(bodyId, launchVelocity, angularVel);
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
     * Called when a coin lands on its edge after being flipped.
     * This method is meant to define the behavior when the improbable edge-land scenario occurs.
     *
     * @param player The {@link Player} who initiated the coin flip.
     * @param stack  The {@link ItemStack} of the coin being used.
     */
//    public abstract void onEdge(Player player, ItemStack stack);
}