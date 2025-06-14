package net.goo.brutality.event;

import net.goo.brutality.Brutality;
import net.goo.brutality.config.BrutalityClientConfig;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.goo.brutality.item.weapon.custom.CreaseOfCreationItem;
import net.goo.brutality.item.weapon.custom.SupernovaSword;
import net.goo.brutality.registry.ModEntities;
import net.goo.brutality.util.helpers.EnvironmentColorManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static net.goo.brutality.util.helpers.EnvironmentColorManager.*;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class ForgePlayerStateHandler {

    @SubscribeEvent
    public static void onChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {

        Player player = event.getEntity();
        if (player.level() instanceof ServerLevel serverLevel) {
            SupernovaSword.clearAsteroids(player, serverLevel);
            CreaseOfCreationItem.handleCreaseOfCreation(player);
        }

        EnvironmentColorManager.resetAllColors();
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {


        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (player.level() instanceof ServerLevel serverLevel) {
                SupernovaSword.clearAsteroids(player, serverLevel);
                CreaseOfCreationItem.handleCreaseOfCreation(player);
            }

            EnvironmentColorManager.resetAllColors();
        }

    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {


        Player player = event.getEntity();
        if (player.level() instanceof ServerLevel serverLevel) {
            SupernovaSword.clearAsteroids(player, serverLevel);
            CreaseOfCreationItem.handleCreaseOfCreation(player);
        }

        EnvironmentColorManager.resetAllColors();
    }

    private static final Map<UUID, Integer> lastSelectedSlot = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Player player = event.player;
        UUID uuid = player.getUUID();
        int currentSlot = player.getInventory().selected;
        int lastSlot = lastSelectedSlot.getOrDefault(uuid, -1);

        if (currentSlot != lastSlot) {
            ItemStack deselected = lastSlot >= 0 && lastSlot < 9 ? player.getInventory().getItem(lastSlot) : ItemStack.EMPTY;

            if (deselected.getItem() instanceof BrutalityGeoItem item) {
                item.onDeselected(player, deselected);
            }
        }

        lastSelectedSlot.put(uuid, currentSlot);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        ClientLevel level = mc.level;
        // Clear previous frame's color requests
        activeColorSources.clear();


        if (BrutalityClientConfig.BLACK_HOLE_SKY_COLOR.get()) {
            boolean blackHoleNearby = StreamSupport.stream(level.entitiesForRendering().spliterator(), false)
                    .anyMatch(e -> e.getType() == ModEntities.BLACK_HOLE_ENTITY.get() && e.distanceToSqr(mc.player) <= 10 * 10);


            apply("black_hole", blackHoleNearby,
                    new EnvironmentColorManager.ProximityColorSet()
                            .setColorAutoReset(EnvironmentColorManager.ColorType.SKY, new int[]{0, 0, 0})
                            .setColorAutoReset(EnvironmentColorManager.ColorType.FOG, new int[]{0, 0, 0})
            );
        }

        boolean rayNearby = StreamSupport.stream(level.entitiesForRendering().spliterator(), false)
                .anyMatch(e -> e.getType() == ModEntities.EXPLOSION_RAY.get() && e.distanceToSqr(mc.player) <= 50 * 50);

        apply("explosion_ray", rayNearby,
                new EnvironmentColorManager.ProximityColorSet()
                        .setColorAutoReset(EnvironmentColorManager.ColorType.SKY, new int[]{255, 140, 0})
                        .setColorAutoReset(EnvironmentColorManager.ColorType.FOG, new int[]{0, 0, 0})
        );

        resolveAndApplyColors();
    }


}