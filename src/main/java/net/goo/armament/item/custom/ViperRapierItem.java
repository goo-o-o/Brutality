package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.util.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.goo.armament.util.ModResources.FANTASY;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ViperRapierItem extends SwordItem implements GeoItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final ModItemCategories category;
    private final String ATTACKING = "attacking";

    public ViperRapierItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, ModItemCategories category) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        this.category = category;
    }

    int[] color1 = new int[]{128, 244, 58};
    int[] color2 = new int[]{93, 33, 0};

    public ModItemCategories getCategory() {
        return category;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.viper.desc.1", false, null, color2));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.viper.desc.2", false, null, color1));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.viper.desc.3", false, null, color2));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return ModUtils.tooltipHelper("item.armament.viper", false, FANTASY, color1, color2);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }


    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 2, 0, false, true));
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        int amtOfAttacks = 10;

            if (!level.isClientSide)
            {
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                for(int i = 0; i < amtOfAttacks; ++i) {
                    executor.schedule(() -> performBurstAttack(player), 50 * i, TimeUnit.MILLISECONDS);
                }
            }
            player.getCooldowns().addCooldown(player.getItemInHand(usedHand).getItem(), 20);
        return super.use(level, player, usedHand);
    }

    public void performBurstAttack(Player player) {
        Vec3 viewVector = player.getViewVector(1.0F).normalize();
        Vec3 eyePosition = player.getEyePosition();
        Vec3 targetPosition = eyePosition.add(viewVector.scale(3));
        AABB searchBox = new AABB(eyePosition, targetPosition).inflate(1.0F);

        List<Entity> entities = player.level().getEntities(player, searchBox, entity -> entity instanceof LivingEntity);

        if (!entities.isEmpty()) {
            for (Entity entity : entities) {
                if (player.hasLineOfSight(entity) && entity instanceof LivingEntity target) {
                    target.invulnerableTime = 0;
                    target.knockback(0.15F, -viewVector.x, -viewVector.z);
                    target.hurt(target.damageSources().generic(), 1);
                }
            }
        }
    }


}
