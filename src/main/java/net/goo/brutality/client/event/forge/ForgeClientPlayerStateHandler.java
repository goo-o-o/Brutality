package net.goo.brutality.client.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.config.BrutalityClientConfig;
import net.goo.brutality.client.renderers.shaders.outline.MaxSwordOutlineShader;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.item.ThrowableWeaponUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static net.goo.brutality.util.EnvironmentColorManager.*;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT)
public class ForgeClientPlayerStateHandler {

    public static boolean ERROR_404_EQUIPPED = false;

    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        resetAllColors();
    }

    @SubscribeEvent
    public static void onClientPlayerLogout(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof LocalPlayer localPlayer) {
            MaxSwordOutlineShader.START_TIMES.removeLong(localPlayer.getUUID());
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if (level == null || player == null) return;


        activeColorSources.clear();

        if (BrutalityClientConfig.BLACK_HOLE_SKY_COLOR.get()) {
            boolean blackHoleNearby = StreamSupport.stream(level.entitiesForRendering().spliterator(), false)
                    .anyMatch(e -> e.getType() == BrutalityEntities.BLACK_HOLE_ENTITY.get() && e.distanceToSqr(player) <= 10 * 10);

            apply("black_hole", blackHoleNearby, new ProximityColorSet()
                    .setColorAutoReset(ColorType.SKY, FastColor.ARGB32.color(255, 0, 0, 0))
                    .setColorAutoReset(ColorType.FOG, FastColor.ARGB32.color(255, 0, 0, 0))
            );
        }


        if (BrutalityClientConfig.BORK_SKY_COLOR.get()) {

            boolean playerNearEntityWithBork = StreamSupport.stream(level.entitiesForRendering().spliterator(), false)
                    .anyMatch(e -> e instanceof Player && ((Player) e).isHolding(BrutalityItems.BLADE_OF_THE_RUINED_KING.get()) && e.distanceToSqr(player) <= 10 * 10);


            apply("bork", playerNearEntityWithBork,
                    new ProximityColorSet().
                            setColorAutoReset(ColorType.FOG, FastColor.ARGB32.color(255, 32, 92, 91)).
//                            setColorAutoReset(ColorType.WATER, FastColor.ARGB32.color(255,32, 92, 91)).
//                            setColorAutoReset(ColorType.GRASS, FastColor.ARGB32.color(255,32, 92, 91)).
//                            setColorAutoReset(ColorType.FOLIAGE, FastColor.ARGB32.color(255,32, 92, 91)).
        setColorAutoReset(ColorType.SKY, FastColor.ARGB32.color(255, 0, 0, 0)));
        }

//        boolean rayNearby = StreamSupport.stream(levels.entitiesForRendering().spliterator(), false)
//                .anyMatch(e -> e.getType() == BrutalityEntities.EXPLOSION_RAY.get() && e.distanceToSqr(player) <= 50 * 50);
//
//        apply("explosion_ray", rayNearby, new ProximityColorSet()
//                .setColorAutoReset(ColorType.SKY, FastColor.ARGB32.color(255, 255, 140, 0))
//                .setColorAutoReset(ColorType.FOG, FastColor.ARGB32.color(255, 0, 0, 0))
//        );

        resolveAndApplyColors();

        ThrowableWeaponUtils.handleAttacksWithoutBetterCombat(player);

        if (player.tickCount % 4 == 0) {
            Optional<ICuriosItemHandler> handlerOpt = CuriosApi.getCuriosInventory(player).resolve();
            handlerOpt.ifPresent(itemHandler -> ERROR_404_EQUIPPED = itemHandler.isEquipped(BrutalityItems.ERROR_404.get()));
        }

    }
}
