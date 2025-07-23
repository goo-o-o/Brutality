package net.goo.brutality.event;

import net.goo.brutality.Brutality;
import net.goo.brutality.config.BrutalityClientConfig;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.goo.brutality.item.weapon.axe.RhittaAxe;
import net.goo.brutality.item.weapon.generic.CreaseOfCreationItem;
import net.goo.brutality.item.weapon.lance.EventHorizonLance;
import net.goo.brutality.item.weapon.sword.SupernovaSword;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.c2sActivateRagePacket;
import net.goo.brutality.registry.*;
import net.goo.brutality.util.ModTags;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static net.goo.brutality.util.helpers.EnvironmentColorManager.*;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class ForgePlayerStateHandler {
    public static final ResourceLocation BIT_SHADER = ResourceLocation.fromNamespaceAndPath("minecraft", "shaders/post/bits.json");
    public static final ResourceLocation PIXEL_SHADER = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "shaders/post/pixelate.json");
    public static final ResourceLocation DEPTH_SHADER = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "shaders/post/depth.json");

    @SubscribeEvent
    public static void onSwitchItemHands(LivingSwapItemsEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.isHolding(BrutalityModItems.LAST_PRISM_ITEM.get())) {
                if (player.isUsingItem()) {
                    event.setCanceled(true);
                }
            }
        }
    }


    @SubscribeEvent
    public static void onChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {

        Player player = event.getEntity();
        if (player.level() instanceof ServerLevel serverLevel) {
            SupernovaSword.clearAsteroids(player, serverLevel);
            CreaseOfCreationItem.handleCreaseOfCreation(player);
        }

        resetAllColors();
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {


        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (player.level() instanceof ServerLevel serverLevel) {
                SupernovaSword.clearAsteroids(player, serverLevel);
                CreaseOfCreationItem.handleCreaseOfCreation(player);
            }

            resetAllColors();
        }

    }


    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {


        Player player = event.getEntity();
        if (player.level() instanceof ServerLevel serverLevel) {
            SupernovaSword.clearAsteroids(player, serverLevel);
            CreaseOfCreationItem.handleCreaseOfCreation(player);
        }

    }

    @SubscribeEvent
    public static void onPlaceInItemFrame(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!(event.getLevel() instanceof ServerLevel)) return;

        Entity target = event.getTarget();
        Player player = event.getEntity();

        if (target instanceof ItemFrame itemFrame) {
            ItemStack heldItem = player.getMainHandItem();

            if (itemFrame.getItem().isEmpty() && !heldItem.isEmpty()) {
                if (heldItem.getItem() instanceof EventHorizonLance eventHorizonLance) {
                    eventHorizonLance.despawnBlackHole(player, heldItem);
                }
                if (heldItem.getItem() instanceof RhittaAxe rhittaAxe) {
                    rhittaAxe.despawnCruelSun(player, heldItem);
                }
            }
        }
    }


    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        resetAllColors();
    }


    private static final Map<UUID, Integer> lastSelectedSlot = new HashMap<>();

    private static final UUID CHOPSTICK_AS_UUID = UUID.fromString("cd9b5958-a78b-4391-af97-826d4379f7f0");
    private static final UUID CHOPSTICK_KB_UUID = UUID.fromString("3f0426a3-2e8a-4d29-b06e-7a470bf28015");


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


        AttributeInstance knockback = player.getAttribute(Attributes.ATTACK_KNOCKBACK);
        AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
        boolean hasASMult = attackSpeed != null && attackSpeed.getModifier(CHOPSTICK_AS_UUID) != null;
        boolean hasKBMult = knockback != null && knockback.getModifier(CHOPSTICK_KB_UUID) != null;

        if (player.getMainHandItem().is(BrutalityModItems.CHOPSTICK_STAFF.get()) && player.getOffhandItem().is(BrutalityModItems.CHOPSTICK_STAFF.get())) {
            if (attackSpeed != null & !hasASMult) {
                System.out.println("adding Attack Speed");
                attackSpeed.addTransientModifier(
                        new AttributeModifier(
                                CHOPSTICK_AS_UUID,
                                "Temporary AS Bonus",
                                0.5,
                                AttributeModifier.Operation.ADDITION
                        )
                );
            }

            if (knockback != null && !hasKBMult) {
                System.out.println("adding Knockback");

                knockback.addTransientModifier(
                        new AttributeModifier(
                                CHOPSTICK_KB_UUID,
                                "Temporary KB Bonus",
                                2,
                                AttributeModifier.Operation.ADDITION
                        )
                );
            }
        } else {

            if (attackSpeed != null) attackSpeed.removeModifier(CHOPSTICK_AS_UUID);
            if (knockback != null) knockback.removeModifier(CHOPSTICK_KB_UUID);
        }

    }

    private static boolean wasHoldingGpuAxe = false;
    private static int originalFps = -1;
    private static boolean originalVsync = false;
    private static boolean originalOcclusion = false;
    private static GraphicsStatus originalGfxMode;
    private static int originalRenderDist = -1;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (mc.level == null || player == null) return;
        ClientLevel level = mc.level;

        if (player.hasEffect(BrutalityModMobEffects.STUNNED.get())) {
            mc.mouseHandler.setIgnoreFirstMove();
        }

        boolean isHoldingGpuAxe = player.isHolding(BrutalityModItems.OLD_GPU_AXE.get());

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

        if (!player.hasEffect(BrutalityModMobEffects.ENRAGED.get()))
            if (Keybindings.RAGE_ACTIVATE_KEY.get().consumeClick()) {
                CuriosApi.getCuriosInventory(player).ifPresent(
                        handler -> {
                            if (!handler.findCurios(stack -> stack.is(ModTags.Items.RAGE_ITEMS)).isEmpty()) {
                                player.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP).ifPresent(cap -> {
                                    if (handler.findFirstCurio(BrutalityModItems.ANGER_MANAGEMENT.get()).isPresent()) {
                                        PacketHandler.sendToServer(new c2sActivateRagePacket());
                                    }
                                });

                            }
                        });
            }

        activeColorSources.clear();

        if (BrutalityClientConfig.BLACK_HOLE_SKY_COLOR.get()) {
            boolean blackHoleNearby = StreamSupport.stream(level.entitiesForRendering().spliterator(), false)
                    .anyMatch(e -> e.getType() == BrutalityModEntities.BLACK_HOLE_ENTITY.get() && e.distanceToSqr(player) <= 10 * 10);

            apply("black_hole", blackHoleNearby, new ProximityColorSet()
                    .setColorAutoReset(ColorType.SKY, new int[]{0, 0, 0})
                    .setColorAutoReset(ColorType.FOG, new int[]{0, 0, 0})
            );
        }


        boolean playerNearEntityWithBork = StreamSupport.stream(level.entitiesForRendering().spliterator(), false)
                .anyMatch(e -> e instanceof Player && ((Player) e).isHolding(BrutalityModItems.BLADE_OF_THE_RUINED_KING.get()) && e.distanceToSqr(player) <= 10 * 10);


        apply("bork", playerNearEntityWithBork,
                new ProximityColorSet().
                        setColorAutoReset(ColorType.FOG, new int[]{32, 92, 91}).
                        setColorAutoReset(ColorType.WATER, new int[]{32, 92, 91}).
                        setColorAutoReset(ColorType.GRASS, new int[]{32, 92, 91}).
                        setColorAutoReset(ColorType.FOLIAGE, new int[]{32, 92, 91}).
                        setColorAutoReset(ColorType.SKY, new int[]{0, 0, 0}));


        boolean rayNearby = StreamSupport.stream(level.entitiesForRendering().spliterator(), false)
                .anyMatch(e -> e.getType() == BrutalityModEntities.EXPLOSION_RAY.get() && e.distanceToSqr(player) <= 50 * 50);

        apply("explosion_ray", rayNearby, new ProximityColorSet()
                .setColorAutoReset(ColorType.SKY, new int[]{255, 140, 0})
                .setColorAutoReset(ColorType.FOG, new int[]{0, 0, 0})
        );

        resolveAndApplyColors();

    }


}