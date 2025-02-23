package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.client.item.ArmaGeoGlowingWeaponRenderer;
import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.entity.custom.ThrownThunderbolt;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaTridentItem;
import net.goo.armament.registry.ModItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.lang.Math.PI;
import static net.goo.armament.util.ModResources.THUNDERBOLT_COLORS;
import static net.goo.armament.util.ModUtils.hasInfinity;
import static net.goo.armament.util.ModUtils.nextFloatBetweenInclusive;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ThunderboltTrident extends ArmaTridentItem implements Vanishable, ArmaGeoItem {
    protected final RandomSource random = RandomSource.create();

    public ThunderboltTrident(Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(new Item.Properties().durability(1500), identifier, category, rarity, abilityCount);
        this.colors = THUNDERBOLT_COLORS;
    }


    public ModItemCategories getCategory() {
        return category;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        EnchantmentHelper.setEnchantments(Map.of(Enchantments.LOYALTY, 5, Enchantments.INFINITY_ARROWS, 1), stack);
        return stack;
    }

    // CHANGE THESE
    //==================================================================//

    @Override
    public float getLaunchVel() {
        return 3.5F;
    }

    @Override
    public void launchProjectile(Level pLevel, Player player, ItemStack pStack) {
        int j = EnchantmentHelper.getRiptide(pStack);

        if (j == 0) {
            ThrownThunderbolt thrownEntity = new ThrownThunderbolt(pLevel, player, pStack);
            thrownEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, getLaunchVel() + (float) j * 0.5F, 1.0F);
            if (player.getAbilities().instabuild) {
                thrownEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            pLevel.addFreshEntity(thrownEntity);
            pLevel.playSound(null, thrownEntity, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild && !hasInfinity(pStack)) {
                player.getInventory().removeItem(pStack);
            }
        }
    }

    //==================================================================//


    @SubscribeEvent
    public static void onGrindstoneUse(GrindstoneEvent.OnPlaceItem event) {
        ItemStack bottomItem = event.getBottomItem();
        ItemStack topItem = event.getTopItem();
        ItemStack targetStack = bottomItem.isEmpty() ? topItem : bottomItem;

        if (targetStack.getItem() == ModItems.THUNDERBOLT_TRIDENT.get()) {
            ItemStack resultStack = targetStack.copy();

            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(resultStack);
            enchantments = enchantments.entrySet().stream().filter(entry ->
                            entry.getKey().isCurse() || entry.getKey() == Enchantments.LOYALTY || entry.getKey() == Enchantments.INFINITY_ARROWS)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            enchantments.put(Enchantments.LOYALTY, 5);
            enchantments.put(Enchantments.INFINITY_ARROWS, 1);
            EnchantmentHelper.setEnchantments(enchantments, resultStack);
            event.setXp(0);
            event.setOutput(resultStack);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (pPlayer.isCrouching()) {
            performZeusWrathAttack(pPlayer, pLevel);
            pPlayer.getCooldowns().addCooldown(itemstack.getItem(), 80);
            return InteractionResultHolder.fail(itemstack);
        } else {
            if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
                return InteractionResultHolder.fail(itemstack);
            } else if (EnchantmentHelper.getRiptide(itemstack) > 0 && !pPlayer.isInWaterOrRain()) {
                return InteractionResultHolder.fail(itemstack);
            } else {
                pPlayer.startUsingItem(pHand);
                return InteractionResultHolder.consume(itemstack);
            }
        }
    }

    public void performZeusWrathAttack(Player pPlayer, Level pLevel) {
        for (int i = 0; i < random.nextInt(5,11); i++) {
            float distance = nextFloatBetweenInclusive(random, 5F, 10F);
            float angle = random.nextFloat() * 2 * ((float) PI);

            float xOffset = distance * ((float) Math.cos(angle));
            float zOffset = distance * ((float) Math.sin(angle));

            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(pLevel);
            lightningBolt.setPos(pPlayer.getX() + xOffset, pPlayer.getY(), pPlayer.getZ() + zOffset);
            lightningBolt.setCause(pPlayer instanceof ServerPlayer ? (ServerPlayer) pPlayer : null);
            pLevel.addFreshEntity(lightningBolt);

        }
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
        );
    }

    @Override
    public <T extends Item & ArmaGeoItem, R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, ArmaGeoGlowingWeaponRenderer.class);
    }

}
