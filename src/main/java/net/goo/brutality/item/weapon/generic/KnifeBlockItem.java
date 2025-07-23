package net.goo.brutality.item.weapon.generic;

import net.goo.brutality.entity.projectile.trident.ThrownKnife;
import net.goo.brutality.item.base.BrutalityGenericItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.*;

import static net.goo.brutality.util.helpers.EnchantmentHelper.restrictEnchants;

public class KnifeBlockItem extends BrutalityGenericItem {
    private final String CHEF = "chef", CARVING = "carving", BREAD = "bread", CLEAVER = "cleaver", FORK = "fork", AXE = "axe";

    public KnifeBlockItem(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }


    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);

        stack.getOrCreateTag().putByte(CHEF, (byte) 1);
        stack.getOrCreateTag().putByte(CARVING, (byte) 1);
        stack.getOrCreateTag().putByte(BREAD, (byte) 1);
        stack.getOrCreateTag().putByte(CLEAVER, (byte) 1);
        stack.getOrCreateTag().putByte(FORK, (byte) 1);
        stack.getOrCreateTag().putByte(AXE, (byte) 1);

        return stack;

    }

    private static final Set<Enchantment> ALLOWED_ENCHANTMENTS = Set.of(
            Enchantments.KNOCKBACK
    );

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return restrictEnchants(book, ALLOWED_ENCHANTMENTS);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return ALLOWED_ENCHANTMENTS.contains(enchantment);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        Set<String> allKnives = new HashSet<>(Arrays.asList(CHEF, CARVING, BREAD, CLEAVER, FORK, AXE));
        List<String> availableKnives = new ArrayList<>();

        for (String knife : allKnives) {
            if (stack.getOrCreateTag().contains(knife)) {
                availableKnives.add(knife);
            }
        }

        if (pLevel instanceof ServerLevel serverLevel) {
            if (!availableKnives.isEmpty()) {
                String randomKnife = availableKnives.get(pLevel.getRandom().nextInt(availableKnives.size()));

                triggerAnim(pPlayer, GeoItem.getOrAssignId(stack, serverLevel), "no_" + randomKnife, "no_" + randomKnife);
                stack.getOrCreateTag().remove(randomKnife);
                launchProjectile(pLevel, pPlayer, stack, randomKnife);
                pLevel.playSound(null, pPlayer.getOnPos(), BrutalityModSounds.KNIFE_BLOCK.get(), SoundSource.PLAYERS);
            } else {
                pPlayer.getCooldowns().addCooldown(stack.getItem(), 30);
                pLevel.playSound(null, pPlayer.getOnPos(), SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS);
                for (String knife : allKnives) {
                    stopTriggeredAnim(pPlayer, GeoItem.getOrAssignId(stack, serverLevel), "no_" + knife, "no_" + knife);
                    stack.getOrCreateTag().putByte(knife, ((byte) 1));
                }
            }
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }


    public void launchProjectile(Level pLevel, Player player, ItemStack pStack, String knifeType) {

        ThrownKnife thrownKnife = new ThrownKnife(pLevel, player, pStack, BrutalityModEntities.THROWN_KNIFE_ENTITY.get(), knifeType);
        thrownKnife.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3, 1.0F);
        thrownKnife.pickup = AbstractArrow.Pickup.DISALLOWED;

        pLevel.addFreshEntity(thrownKnife);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

        controllers.add(new AnimationController<>(this, "no_chef", state -> null).
                triggerableAnim("no_chef", RawAnimation.begin().thenPlay("no_chef")));
        controllers.add(new AnimationController<>(this, "no_carving", state -> null).
                triggerableAnim("no_carving", RawAnimation.begin().thenPlay("no_carving")));
        controllers.add(new AnimationController<>(this, "no_bread", state -> null).
                triggerableAnim("no_bread", RawAnimation.begin().thenPlay("no_bread")));
        controllers.add(new AnimationController<>(this, "no_cleaver", state -> null).
                triggerableAnim("no_cleaver", RawAnimation.begin().thenPlay("no_cleaver")));
        controllers.add(new AnimationController<>(this, "no_fork", state -> null).
                triggerableAnim("no_fork", RawAnimation.begin().thenPlay("no_fork")));
        controllers.add(new AnimationController<>(this, "no_axe", state -> null).
                triggerableAnim("no_axe", RawAnimation.begin().thenPlay("no_axe")));

    }


}


