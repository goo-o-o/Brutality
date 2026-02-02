package net.goo.brutality.common.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.particle.providers.WaveParticleData;
import net.goo.brutality.common.entity.explosion.BloodExplosion;
import net.goo.brutality.common.item.BrutalityArmorMaterials;
import net.goo.brutality.common.item.base.BrutalityArmorItem;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.clientbound.ClientboundParticlePacket;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.mixin.accessors.MobEffectInstanceSourceAccessor;
import net.goo.brutality.util.ModExplosionHelper;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.build_archetypes.BloodHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class VampireLordArmorItem extends BrutalityArmorItem {

    public VampireLordArmorItem(ArmorMaterial pMaterial, Type pType, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pMaterial, pType, rarity, descriptionComponents);
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot pEquipmentSlot) {
        if (pEquipmentSlot == type.getSlot()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            UUID modifierUUID = UUID.nameUUIDFromBytes((this.getType().getName() + this.getMaterial() + pEquipmentSlot).getBytes());
            builder.putAll(super.getDefaultAttributeModifiers(pEquipmentSlot));
            builder.put(BrutalityAttributes.OMNIVAMP.get(), new AttributeModifier(modifierUUID, "Omnivamp buff", 0.1F, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    public static void performBloodWaveAttack(Player player) {
        WaveParticleData<?> waveParticleData = new WaveParticleData<>(BrutalityParticles.BLOOD_WAVE.get(), 5, 10);

        float playerX = (float) player.getX();
        float playerY = (float) player.getY(0.5);
        float playerZ = (float) player.getZ(0.5);

        PacketHandler.sendToNearbyClients((ServerLevel) player.level(), playerX, playerY, playerZ, 5, new ClientboundParticlePacket(
                waveParticleData, true, playerX, playerY, playerZ, 0, 0, 0,
                0, 0, 0, 1
        ));

        ModUtils.applyWaveEffect(((ServerLevel) player.level()), playerX, playerY, playerZ, Entity.class, waveParticleData, e -> (e instanceof Projectile || e instanceof LivingEntity) && e != player,
                e -> {
                    e.hurt(e.damageSources().indirectMagic(player, player), 10);
                });
    }


    public static void performBloodDrainAttack(Player player) {
        ServerLevel serverLevel = (ServerLevel) player.level();

        serverLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), player, player.getBoundingBox().inflate(5)).forEach(e -> {
            MobEffectInstance instance = new MobEffectInstance(BrutalityEffects.SIPHONED.get(), 20, 2);
            ((MobEffectInstanceSourceAccessor) instance).brutality$setSourceID(player.getId());
            e.addEffect(instance);

            serverLevel.playSound(null, e.getX(), e.getY(0.5), e.getZ(), BrutalitySounds.BLOOD_SPLATTER.get(), SoundSource.PLAYERS, 1,
                    Mth.nextFloat(player.getRandom(), 0.9F, 1.1F));
            serverLevel.sendParticles(BrutalityParticles.BLOOD_PARTICLE.get(), e.getX(), e.getY(0.5), e.getZ(), 10, 0.1, 0.1, 0.1, 0);
        });
    }

    public static void performBloodExplosionAttack(Player player) {
        player.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), player, player.getBoundingBox().inflate(5)).forEach(e -> {
            BloodExplosion explosion = new BloodExplosion(player.level(), player, null, null, e.getX(), e.getY(0.5), e.getZ(), 1, false, Level.ExplosionInteraction.NONE);
            explosion.damageScale = 7F;
            explosion.knockbackScale = 0;
            ModExplosionHelper.Server.explode(explosion, player.level(), true);
        });
    }

    public static void handleArmorSetAbility(Player player) {
        if (player.hasEffect(TerramityModMobEffects.ARMOR_SET_ABILITY_COOLDOWN.get())) return;

        float percentage = BloodHelper.getCurrentBloodPercentage(player);
        boolean success = false;
        if (percentage > 0.99f) {
            performBloodExplosionAttack(player);
            BloodHelper.modifyBloodValue(player, -99);
            success = true;
        } else if (percentage > 0.66f) {
            performBloodWaveAttack(player);
            BloodHelper.modifyBloodValue(player, -66);
            success = true;
        } else if (percentage > 0.33f) {
            performBloodDrainAttack(player);
            BloodHelper.modifyBloodValue(player, -33);
            success = true;
        }
        if (success)
            player.addEffect(new MobEffectInstance(TerramityModMobEffects.ARMOR_SET_ABILITY_COOLDOWN.get(), 40, 0));

    }

    public static void resetBloodMeterOnUnequip(LivingEquipmentChangeEvent event) {
        if ((!(event.getTo().getItem() instanceof VampireLordArmorItem) && event.getFrom().getItem() instanceof ArmorItem) && event.getEntity() instanceof Player player) {
            BloodHelper.setBloodValue(player, 0);
        }
    }

    public static void preventEating(LivingEntityUseItemEvent.Start event) {
        if (event.getEntity() instanceof Player player) {
            if (ModUtils.hasFullArmorSet(player, BrutalityArmorMaterials.VAMPIRE_LORD) && event.getItem().isEdible()) {
                event.setCanceled(true);
            }
        }
    }

}
