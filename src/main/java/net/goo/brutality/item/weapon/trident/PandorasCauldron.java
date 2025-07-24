//package net.goo.brutality.item.weapon.trident;
//
//import net.goo.brutality.entity.base.BrutalityAbstractPhysicsProjectile;
//import net.goo.brutality.entity.projectile.trident.physics_projectile.*;
//import net.goo.brutality.item.base.BrutalityTridentItem;
//import net.goo.brutality.registry.BrutalityModEntities;
//import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
//import net.minecraft.util.RandomSource;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.entity.projectile.AbstractArrow;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Rarity;
//import net.minecraft.world.item.UseAnim;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.level.Level;
//import software.bernie.geckolib.core.animation.AnimatableManager;
//
//import java.util.List;
//import java.util.Set;
//
//import static net.goo.brutality.util.helpers.EnchantmentHelper.restrictEnchants;
//
//public class PandorasCauldron extends BrutalityTridentItem {
//    protected final RandomSource random = RandomSource.create();
//
//
//    private static final Set<Enchantment> ALLOWED_ENCHANTMENTS = Set.of(
//    );
//
//    public PandorasCauldron(float attackDamageModifier, float attackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
//        super(attackDamageModifier, attackSpeedModifier, rarity, descriptionComponents);
//    }
//
//    @Override
//    public UseAnim getUseAnimation(ItemStack pStack) {
//        return UseAnim.NONE;
//    }
//
//    @Override
//    public int getMaxDamage(ItemStack stack) {
//        return 999;
//    }
//
//    @Override
//    protected boolean isInnatelyInfinite() {
//        return true;
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//    }
//
//    @Override
//    public float getLaunchVel() {
//        return 1F;
//    }
//
//    @Override
//    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
//        return restrictEnchants(book, ALLOWED_ENCHANTMENTS);
//    }
//
//    @Override
//    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
//        return ALLOWED_ENCHANTMENTS.contains(enchantment);
//    }
//
//    @Override
//    public void launchProjectile(Level pLevel, Player player, ItemStack pStack) {
//
//        BrutalityAbstractPhysicsProjectile thrownEntity = getRandomProjectile(pLevel, player, pStack);
//        thrownEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, getLaunchVel(), 1.0F);
//        thrownEntity.pickup = AbstractArrow.Pickup.ALLOWED;
//        pLevel.addFreshEntity(thrownEntity);
//
//    }
//
////    @Override
////    public <T extends Item & BrutalityGeoItem> void configureLayers(BrutalityItemRenderer<T> renderer) {
////        super.configureLayers(renderer);
////        renderer.addRenderLayer(new BrutalityAutoEndPortalLayer<>(renderer));
////    }
//
//    private BrutalityAbstractPhysicsProjectile getRandomProjectile(Level level, Player player, ItemStack stack) {
//        List<BrutalityAbstractPhysicsProjectile> projectiles = List.of(
//                (new ThrownCabbage(level, player, stack, BrutalityModEntities.THROWN_CABBAGE_ENTITY.get())),
//                (new ThrownWintermelon(level, player, stack, BrutalityModEntities.THROWN_WINTERMELON_ENTITY.get())),
//                (new ThrownBanana(level, player, stack, BrutalityModEntities.THROWN_BANANA_ENTITY.get())),
//                (new ThrownApple(level, player, stack, BrutalityModEntities.THROWN_APPLE_ENTITY.get())),
//                (new ThrownButter(level, player, stack, BrutalityModEntities.THROWN_BUTTER_ENTITY.get())),
//                (new ThrownDurian(level, player, stack, BrutalityModEntities.THROWN_DURIAN_ENTITY.get()))
//                );
//
//        return projectiles.get(level.getRandom().nextInt(projectiles.size()));
//    }
//
//
//    @Override
//    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
//        ItemStack itemstack = pPlayer.getItemInHand(pHand);
//        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
//            return InteractionResultHolder.fail(itemstack);
//        } else {
//            pPlayer.startUsingItem(pHand);
//            return InteractionResultHolder.consume(itemstack);
//        }
//    }
//}
