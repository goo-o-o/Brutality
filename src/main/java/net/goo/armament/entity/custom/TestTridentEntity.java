package net.goo.armament.entity.custom;

import net.goo.armament.entity.ModEntities;
import net.goo.armament.item.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class TestTridentEntity extends AbstractArrow {

    private ItemStack tridentItem = new ItemStack(ModItems.ZEUS_THUNDERBOLT_TRIDENT.get());
    private boolean dealtDamage;

    public TestTridentEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public TestTridentEntity(Level pLevel, LivingEntity pShooter, ItemStack pStack) {
        super(ModEntities.TEST_TRIDENT_ENTITY.get(), pShooter, pLevel);
        this.tridentItem = pStack.copy();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.tridentItem.copy();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        float damage = 8.0F;
        if (entity instanceof LivingEntity) {
            damage += EnchantmentHelper.getDamageBonus(this.tridentItem, ((LivingEntity) entity).getMobType());
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, (Entity)(entity1 == null ? this : entity1));
        this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;

        if (entity.hurt(damagesource, damage)) {
            if (entity instanceof LivingEntity livingEntity) {
                this.doPostHurtEffects(livingEntity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
    }

    @Override
    public SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public boolean tryPickup(Player pPlayer) {
        return super.tryPickup(pPlayer);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return super.getAddEntityPacket();
    }
}
