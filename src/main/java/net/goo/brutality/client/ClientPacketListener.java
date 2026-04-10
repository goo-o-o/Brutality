package net.goo.brutality.client;

import net.goo.brutality.common.item.ItemEquipUnequipTriggerable;
import net.goo.brutality.common.item.base.BrutalityAnkletItem;
import net.goo.brutality.common.network.clientbound.ClientboundChainLightningPacket;
import net.goo.brutality.common.network.clientbound.ClientboundDodgePacket;
import net.goo.brutality.common.network.clientbound.ClientboundEquipmentChangePacket;
import net.goo.brutality.event.LivingDodgeEvent;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.util.lightning.ChainLightningHelper;
import net.goo.brutality.util.math.PhysicsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.ModLoader;

public class ClientPacketListener {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void handleDodgeClient(ClientboundDodgePacket packet) {
        ClientLevel level = mc.level;
        if (level == null) return;
        Holder<DamageType> damageType = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, packet.damageTypeId));
        Entity directEntity = packet.hasDirectEntity && packet.directEntityId != null ? level.getEntity(packet.directEntityId) : null;
        Entity causingEntity = packet.hasCausingEntity && packet.causingEntityId != null ? level.getEntity(packet.causingEntityId) : null;
        DamageSource source = new DamageSource(damageType, directEntity, causingEntity);
        if (level.getEntity(packet.entityId) instanceof LivingEntity livingEntity) {
            LivingDodgeEvent.Client client = new LivingDodgeEvent.Client(livingEntity, source, packet.amount);
            ModLoader.get().postEvent(client);
            if (packet.anklet.getItem() instanceof BrutalityAnkletItem ankletItem) {
                ankletItem.onDodgeClient(livingEntity, source, packet.amount, packet.anklet);
            }
        }
    }

    public static void handleChainLightning(ClientboundChainLightningPacket packet) {
        if (mc.level != null) {
            for (int i = 0; i < packet.iterations; i++) {
                DelayedTaskScheduler.queueClientWork(mc.level, packet.delay * i, () ->
                        ChainLightningHelper.Client.shock(
                                mc.level,
                                packet.lightningType,
                                PhysicsHelper.fromVector3f(packet.start),
                                PhysicsHelper.fromVector3f(packet.end),
                                packet.size,
                                packet.lifespan));
            }
        }
    }

    public static void handleEquipmentChange(ClientboundEquipmentChangePacket packet) {
        ItemEquipUnequipTriggerable triggerable = (ItemEquipUnequipTriggerable) packet.itemStack.getItem();
        if (mc.level == null || !(mc.level.getEntity(packet.entityId) instanceof LivingEntity livingEntity)) return;
        if (packet.isUnequip)
            triggerable.onLeaveMainHand(livingEntity, packet.itemStack);
        else triggerable.onEnterMainHand(livingEntity, packet.itemStack);
    }


}
