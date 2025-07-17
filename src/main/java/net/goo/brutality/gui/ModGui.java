package net.goo.brutality.gui;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.platform.Window;
import net.goo.brutality.Brutality;
import net.goo.brutality.event.LivingEntityEventHandler;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.ModTags;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import static net.goo.brutality.item.curios.SumCharm.SUM_DAMAGE;


@Mod.EventBusSubscriber(Dist.CLIENT)
public class ModGui {
    private static final int SCREEN_COLOR = BrutalityTooltipHelper.argbToInt(new int[]{42, 45, 11});
    private static final int SCREEN_WIDTH = 86;
    private static final int SCREEN_HEIGHT = 37;
    private static final int CALC_PADDING_X = 7;
    private static final int CALC_PADDING_Y = 8;
    private static final int GRAPH_PADDING = 2;
    private static final int MARGIN_Y = 65;
    private static final int MARGIN_X = 20;
    private static final int CALC_WIDTH = 100;
    private static final float RANGE = (float) (2 * Math.PI); // from -π to π
    private static final float AMPLITUDE_SCALE = 0.75F;
    private static final float SINE_AMPLITUDE = 5f * AMPLITUDE_SCALE;
    private static final float COSINE_AMPLITUDE = 1 * AMPLITUDE_SCALE;
    private static final int SINE_COLOR = BrutalityTooltipHelper.argbToInt(67, 74, 172, 255);
    private static final int COSINE_COLOR = BrutalityTooltipHelper.argbToInt(182, 87, 103, 255);
    private static int calcLeft;
    private static int calcTop;

    private static int screenLeft;
    private static int screenTop;

    private static int screenRight;
    private static int screenBottom;

    private static int graphLeft;
    private static int graphRight;
    private static int graphTop;
    private static int graphBottom;
    private static int graphHeight;
    private static int graphWidth;

    private static int windowWidth;
    private static int windowHeight;

    private static float currentTick = (float) (Blaze3D.getTime() * 20d);
    private static float currentPhase = currentTick * 0.1f;

    private static void computeVariables(int scaledWidth, int scaledHeight) {
        calcLeft = scaledWidth - CALC_WIDTH - MARGIN_X;
        calcTop = scaledHeight - MARGIN_Y;

        screenLeft = calcLeft + CALC_PADDING_X;
        screenTop = calcTop + CALC_PADDING_Y;

        screenRight = screenLeft + SCREEN_WIDTH;
        screenBottom = screenTop + SCREEN_HEIGHT;

        graphLeft = screenLeft + GRAPH_PADDING;
        graphRight = screenRight - GRAPH_PADDING;
        graphTop = screenTop + GRAPH_PADDING;
        graphBottom = screenBottom - GRAPH_PADDING;
        graphHeight = graphBottom - graphTop;
        graphWidth = graphRight - graphLeft;

        currentTick = (float) (Blaze3D.getTime() * 20d);
        currentPhase = currentTick * 0.1f;
    }

    @SubscribeEvent
    public static void onRender(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (shouldSkipRender(player)) return;
        GuiGraphics gui = event.getGuiGraphics();

        Window window = event.getWindow();
        windowWidth = window.getGuiScaledWidth();
        windowHeight = window.getGuiScaledHeight();

        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityModItems.SCIENTIFIC_CALCULATOR_BELT.get())) {
                computeVariables(windowWidth, windowHeight);
                renderAxesAndTexture(gui);

                handler.findFirstCurio(BrutalityModItems.SUM_CHARM.get()).ifPresent(slot -> {
                    renderSumDamageStored(gui, slot);
                });

                handler.findFirstCurio(BrutalityModItems.EXPONENTIAL_CHARM.get()).ifPresent(slot -> {
                    renderComboCount(gui, player);
                });

                handler.findFirstCurio(BrutalityModItems.SINE_CHARM.get()).ifPresent(slot -> {
                    renderSineWave(gui);
                });

                handler.findFirstCurio(BrutalityModItems.COSINE_CHARM.get()).ifPresent(slot -> {
                    renderCosineWave(gui);
                });


            }
            if (!handler.findCurios(stack -> stack.is(ModTags.Items.RAGE_ITEMS)).isEmpty()) {
                renderRageBar(gui, player);
            }
        });

    }


    private static final ResourceLocation RAGE_BAR_ICON = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/rage_bar_icon.png");
    private static final ResourceLocation RAGE_BAR = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/rage_bar.png");
    private static final ResourceLocation RAGE_BAR_METER = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/rage_bar_meter.png");


    private static void renderRageBar(GuiGraphics gui, Player player) {
        AttributeInstance maxRage = player.getAttribute(ModAttributes.MAX_RAGE.get());
        if (maxRage != null) {
            player.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP).ifPresent(cap -> {
                int x = 32;
                int y = windowHeight - 32;
                gui.blitNineSliced(
                        RAGE_BAR,
                        x, y,
                        ((int) maxRage.getValue()) + 8, 20,
                        4, 10, 4, 10,
                        256, 256,
                        0, 0
                );

                if (cap.rageValue() > 0 && cap.rageValue() < maxRage.getValue()) {
                    gui.blitNineSliced(
                            RAGE_BAR_METER,
                            x + 3, y + 3,
                            ((int) cap.rageValue()) + 3, 8,
                            1, 4, 2, 4,
                            256, 256,
                            3, 3
                    );
                }

                gui.blit(RAGE_BAR_ICON, x - 16, y - 18, 1, 0, 0, 32, 36, 16, 18);


            });


        }
    }


    private static void renderSumDamageStored(GuiGraphics gui, SlotResult slot) {
        Font font = Minecraft.getInstance().font;
        float damageStored = slot.stack().getOrCreateTag().getFloat(SUM_DAMAGE);

        gui.drawCenteredString(font, String.valueOf(damageStored).formatted(".2f"), screenLeft + font.width("15") + GRAPH_PADDING, screenBottom - font.lineHeight - GRAPH_PADDING, SCREEN_COLOR);
    }

    private static final ResourceLocation calcSprite = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/scientific_calculator.png");

    private static void renderComboCount(GuiGraphics gui, Player player) {
        LivingEntityEventHandler.AttackCombo attackCombo = LivingEntityEventHandler.attackCombos.get(player.getUUID());
        Font font = Minecraft.getInstance().font;

        int currentCombo = 0;
        if (attackCombo != null) {
            currentCombo = attackCombo.hitCount;
            if (System.currentTimeMillis() - attackCombo.lastHitTime > 5000) {
                currentCombo = 0;
            }
        }

        gui.drawCenteredString(font, String.valueOf(currentCombo), screenRight - font.width("15") - GRAPH_PADDING, screenBottom - font.lineHeight - GRAPH_PADDING, SCREEN_COLOR);
    }

    private static void renderAxesAndTexture(GuiGraphics gui) {

        gui.blit(calcSprite, calcLeft, calcTop, 0, 0, 100, 144, 100, 144);

        int centerX = graphLeft + graphWidth / 2;

        gui.hLine(graphLeft, graphRight, graphTop + graphHeight / 2, SCREEN_COLOR);
        gui.vLine(centerX, graphTop - 1, graphBottom, SCREEN_COLOR);

    }

    private static void renderSineWave(GuiGraphics gui) {
        for (int i = 0; i < graphWidth; i++) {
            float relativeX = ((i - graphWidth / 2f) / (float) graphWidth) * RANGE;
            float value = Mth.sin(currentPhase + relativeX) * SINE_AMPLITUDE * AMPLITUDE_SCALE; // Use same amplitude (5f)

            float mappedY = mapValueToY(value + 2.5F * AMPLITUDE_SCALE, -5f, 5f, graphHeight);
            int y = graphBottom - (int) mappedY - 1;

            gui.fill(graphLeft + i, y, graphLeft + i + 1, y + 1, SINE_COLOR);
        }
    }

    private static void renderCosineWave(GuiGraphics gui) {

        for (int i = 0; i < graphWidth; i++) {
            float relativeX = ((i - graphWidth / 2f) / (float) graphWidth) * RANGE;
            float value = Mth.cos(currentPhase + relativeX) * COSINE_AMPLITUDE * AMPLITUDE_SCALE;

            float mappedY = mapValueToY(value + 0.5F * AMPLITUDE_SCALE, -2.5F, 2.5F, graphHeight);
            int y = graphBottom - (int) mappedY;

            gui.fill(graphLeft + i, y, graphLeft + i + 1, y + 1, COSINE_COLOR);
        }
    }


    private static float mapValueToY(float value, float min, float max, int height) {
        return ((value - min) / (max - min)) * height;
    }


    private static boolean shouldSkipRender(Player player) {
        return player == null ||
                Minecraft.getInstance().options.hideGui ||
                Minecraft.getInstance().screen != null;

    }
}
