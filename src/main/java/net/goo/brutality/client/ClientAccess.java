package net.goo.brutality.client;

import net.goo.brutality.network.s2cEnhancedExactParticlePacket;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientAccess {

    public static void spawnParticle(s2cEnhancedExactParticlePacket packet) {
        if (Minecraft.getInstance().level != null)
            Minecraft.getInstance().level.addParticle(packet.particle, true, packet.x, packet.y, packet.z, packet.xSpeed, packet.ySpeed, packet.zSpeed);
    }

    public static void syncCapabilities(int entityId, Map<String, CompoundTag> data) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        Entity entity = level.getEntity(entityId);
        if (entity == null) return;

        for (Map.Entry<String, CompoundTag> entry : data.entrySet()) {
            String key = entry.getKey();
            CompoundTag tag = entry.getValue();

            Capability<?> cap = BrutalityCapabilities.CapabilitySyncRegistry.get(key);
            if (cap != null) {
                entity.getCapability(cap).ifPresent(inst -> {
                    ((INBTSerializable<CompoundTag>) inst).deserializeNBT(tag);
                });
            }
        }
    }
}

