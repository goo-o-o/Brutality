package net.goo.brutality.event.forge.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.goo.brutality.Brutality;
import net.goo.brutality.config.BrutalityClientConfig;
import net.goo.brutality.event.mod.client.Keybindings;
import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.ServerboundActivateRagePacket;
import net.goo.brutality.network.ServerboundActiveAbilityPressPacket;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.ModTags;
import net.goo.brutality.util.ModUtils;
import net.mcreator.terramity.init.TerramityModKeyMappings;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.stream.StreamSupport;

import static net.goo.brutality.util.helpers.EnvironmentColorManager.*;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT)
public class ForgeClientPlayerStateHandler {
    private static boolean wasHoldingGpuAxe = false;
    private static int originalFps = -1;
    private static boolean originalVsync = false;
    private static boolean originalOcclusion = false;
    private static GraphicsStatus originalGfxMode;
    private static int originalRenderDist = -1;
    public static final ResourceLocation BIT_SHADER = ResourceLocation.fromNamespaceAndPath("minecraft", "shaders/post/bits.json");
    private static InteractionHand previousHand = InteractionHand.OFF_HAND;
//
//    @SubscribeEvent
//    public static void onLeftClick(InputEvent.MouseButton event) {
//        Minecraft mc = Minecraft.getInstance();
//        LocalPlayer player = mc.player;
//        ClientLevel level = mc.level;
//        if (player == null || level == null) return;
//
//
//        if (event.getAction() == InputConstants.PRESS && event.getButton() == GLFW.GLFW_MOUSE_BUTTON_1) {
//
//        }
//    }

    @SubscribeEvent
    public static void onRenderNametag(RenderNameTagEvent event) {
        if (event.getEntity() instanceof Player player)
            if (ModUtils.hasFullArmorSet(player, BrutalityArmorMaterials.NOIR)) {
                event.setResult(Event.Result.DENY);
            }
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if (level == null || player == null) return;

        if (event.getAction() == InputConstants.PRESS) {
            if (!player.hasEffect(BrutalityModMobEffects.ENRAGED.get()))
                if (Keybindings.RAGE_ACTIVATE_KEY.consumeClick()) {
                    CuriosApi.getCuriosInventory(player).ifPresent(
                            handler -> {
                                if (!handler.findCurios(stack -> stack.is(ModTags.Items.RAGE_ITEMS)).isEmpty()) {
                                    player.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP).ifPresent(cap -> {
                                        if (handler.findFirstCurio(BrutalityModItems.ANGER_MANAGEMENT.get()).isPresent()) {
                                            PacketHandler.sendToServer(new ServerboundActivateRagePacket());
                                        }
                                    });

                                }
                            });
                }

            if (TerramityModKeyMappings.ACTIVE_ABILITY.consumeClick()) {
                PacketHandler.sendToServer(new ServerboundActiveAbilityPressPacket());
            }
        }
    }


    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        resetAllColors();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if (level == null || player == null) return;

//        if (player.hasEffect(BrutalityModMobEffects.STUNNED.get())) {
//            mc.mouseHandler.setIgnoreFirstMove();
//        }

        boolean isHoldingGpuAxe = player.isHolding(BrutalityModItems.OLD_GPU.get());

        if (isHoldingGpuAxe) {
            if (!wasHoldingGpuAxe) {
                originalFps = mc.options.framerateLimit().get();
                originalVsync = mc.options.enableVsync().get();
                originalRenderDist = mc.options.renderDistance().get();
                originalGfxMode = mc.options.graphicsMode().get();
                originalOcclusion = mc.options.ambientOcclusion().get();
                mc.options.enableVsync().set(false);
            }

//            ForgeClientShaderHandler.loadBitShader(mc);
            mc.gameRenderer.loadEffect(BIT_SHADER);

            if (!mc.options.graphicsMode().get().equals(GraphicsStatus.FAST)) {
                mc.options.graphicsMode().set(GraphicsStatus.FAST);
            }
            if (mc.options.framerateLimit().get() != 10) {
                mc.options.framerateLimit().set(10);
            }
            if (mc.options.renderDistance().get() != 2) {
                mc.options.renderDistance().set(2);
            }
            if (mc.options.enableVsync().get()) {
                mc.options.enableVsync().set(false);
            }
            if (mc.options.ambientOcclusion().get()) {
                mc.options.ambientOcclusion().set(false);
            }


        } else if (wasHoldingGpuAxe) {
            mc.options.framerateLimit().set(originalFps);
            mc.options.enableVsync().set(originalVsync);
            mc.options.renderDistance().set(originalRenderDist);
            mc.options.graphicsMode().set(originalGfxMode);
            mc.options.ambientOcclusion().set(originalOcclusion);
//            ForgeClientShaderHandler.stopBitShader();
            mc.gameRenderer.shutdownEffect();
        }

        wasHoldingGpuAxe = isHoldingGpuAxe;


        activeColorSources.clear();

        if (BrutalityClientConfig.BLACK_HOLE_SKY_COLOR.get()) {
            boolean blackHoleNearby = StreamSupport.stream(level.entitiesForRendering().spliterator(), false)
                    .anyMatch(e -> e.getType() == BrutalityModEntities.BLACK_HOLE_ENTITY.get() && e.distanceToSqr(player) <= 10 * 10);

            apply("black_hole", blackHoleNearby, new ProximityColorSet()
                    .setColorAutoReset(ColorType.SKY, FastColor.ARGB32.color(255,0, 0, 0))
                    .setColorAutoReset(ColorType.FOG, FastColor.ARGB32.color(255,0, 0, 0))
            );
        }


        if (BrutalityClientConfig.BORK_SKY_COLOR.get()) {

            boolean playerNearEntityWithBork = StreamSupport.stream(level.entitiesForRendering().spliterator(), false)
                    .anyMatch(e -> e instanceof Player && ((Player) e).isHolding(BrutalityModItems.BLADE_OF_THE_RUINED_KING.get()) && e.distanceToSqr(player) <= 10 * 10);


            apply("bork", playerNearEntityWithBork,
                    new ProximityColorSet().
                            setColorAutoReset(ColorType.FOG, FastColor.ARGB32.color(255,32, 92, 91)).
//                            setColorAutoReset(ColorType.WATER, FastColor.ARGB32.color(255,32, 92, 91)).
//                            setColorAutoReset(ColorType.GRASS, FastColor.ARGB32.color(255,32, 92, 91)).
//                            setColorAutoReset(ColorType.FOLIAGE, FastColor.ARGB32.color(255,32, 92, 91)).
                            setColorAutoReset(ColorType.SKY, FastColor.ARGB32.color(255,0, 0, 0)));
        }

        boolean rayNearby = StreamSupport.stream(level.entitiesForRendering().spliterator(), false)
                .anyMatch(e -> e.getType() == BrutalityModEntities.EXPLOSION_RAY.get() && e.distanceToSqr(player) <= 50 * 50);

        apply("explosion_ray", rayNearby, new ProximityColorSet()
                .setColorAutoReset(ColorType.SKY, FastColor.ARGB32.color(255,255, 140, 0))
                .setColorAutoReset(ColorType.FOG, FastColor.ARGB32.color(255,0, 0, 0))
        );

        resolveAndApplyColors();

        // Ok let's go through this slowly
        // First I want to check if Player has ThrowingItems in both hands
        // If so, check if Main Hand on cooldown, if not, throw main hand
        // If main hand on cooldown, check for offhand cooldown, if no cooldown, throw offhand
        // Now check if only mainHand, if only MainHand then check for MainHand cooldown then throw
        // If only in Offhand, then don't throw, I don't want to interfere with regular vanilla sword swinging and tool actions
        // Edit: Whatever I wrote above is now incorrect

        if (ModList.get().isLoaded("bettercombat")) return;
        if (mc.options.keyAttack.isDown()) {
            ItemStack mainHand = player.getMainHandItem();
            ItemStack offHand = player.getOffhandItem();
            ItemCooldowns cooldowns = player.getCooldowns();

            if (mainHand.getItem() instanceof BrutalityThrowingItem mainHandThrowingItem && offHand.getItem() instanceof BrutalityThrowingItem offHandThrowingItem) { // Dual wielding Throwing Items
                if (cooldowns.isOnCooldown(mainHandThrowingItem)) return;
                if (cooldowns.isOnCooldown(offHandThrowingItem)) return;
                if (previousHand == InteractionHand.OFF_HAND) { // Throw Main Hand
                    mainHandThrowingItem.handleAttributesAndAnimation(player, mainHand, false);
                    player.resetAttackStrengthTicker();
                    previousHand = InteractionHand.MAIN_HAND;

                } else {
                    offHandThrowingItem.handleAttributesAndAnimation(player, offHand, true);
                    player.resetAttackStrengthTicker();
                    previousHand = InteractionHand.OFF_HAND;
                }
            } else if (mainHand.getItem() instanceof BrutalityThrowingItem mainHandThrowingItem) {
                if (cooldowns.isOnCooldown(mainHandThrowingItem)) return;

                mainHandThrowingItem.handleAttributesAndAnimation(player, mainHand, false);
                player.resetAttackStrengthTicker();
                previousHand = InteractionHand.OFF_HAND;
            }


        }
    }
}
