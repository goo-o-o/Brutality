package net.goo.brutality.mixin.mixins;

import com.mojang.authlib.GameProfile;
import net.goo.brutality.util.CooldownUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }
//
//    // Courtesy of MomLove
//    @Redirect(method = "restoreFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
//    private boolean redirect$restoreFrom(GameRules instance, GameRules.Key<GameRules.BooleanValue> v) {
//        ServerPlayer player = (((ServerPlayer) (Object) this));
//        CombatTracker tracker = player.getCombatTracker();
//        System.out.println(tracker.entries);
//        if (!tracker.entries.isEmpty()) {
//            CombatEntry lastEntry = tracker.entries.get(tracker.entries.size() - 1);
//            System.out.println(lastEntry);
//            if (lastEntry.source().is(BrutalityDamageTypes.DEMATERIALIZE)) {
//                tracker.entries.clear();
//                return true;
//            }
//        }
//        return instance.getBoolean(v); // Default behavior
//    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveCooldowns(CompoundTag tag, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        ItemCooldowns itemCooldowns = player.getCooldowns();
        CompoundTag cooldownTag = new CompoundTag();
        cooldownTag.putInt("tickCount", itemCooldowns.tickCount);
        CompoundTag mapTag = new CompoundTag();
        for (Map.Entry<Item, ItemCooldowns.CooldownInstance> entry : itemCooldowns.cooldowns.entrySet()) {
            if (CooldownUtils.CD_PERSIST_ITEMS.get().contains(entry.getKey())) {
                CompoundTag cooldownInstanceTag = new CompoundTag();
                cooldownInstanceTag.putInt("startTime", entry.getValue().startTime);
                cooldownInstanceTag.putInt("endTime", entry.getValue().endTime);
                mapTag.put(String.valueOf(ForgeRegistries.ITEMS.getKey(entry.getKey())), cooldownInstanceTag);
            }
        }
        cooldownTag.put("cooldowns", mapTag);
        tag.put("BrutalityCooldowns", cooldownTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void loadCooldowns(CompoundTag tag, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        ItemCooldowns itemCooldowns = player.getCooldowns();
        if (tag.contains("BrutalityCooldowns")) {
            CompoundTag cooldownTag = tag.getCompound("BrutalityCooldowns");
            itemCooldowns.tickCount = cooldownTag.getInt("tickCount");
            CompoundTag mapTag = cooldownTag.getCompound("cooldowns");
            itemCooldowns.cooldowns.clear();

            for (String itemKey : mapTag.getAllKeys()) {
                Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(itemKey));
                if (item != null) {
                    CompoundTag cooldownInstanceTag = mapTag.getCompound(itemKey);
                    int startTime = cooldownInstanceTag.getInt("startTime");
                    int endTime = cooldownInstanceTag.getInt("endTime");
                    itemCooldowns.cooldowns.put(item, new ItemCooldowns.CooldownInstance(startTime, endTime));


                }
            }

        }

    }

}