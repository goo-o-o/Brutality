package net.goo.brutality.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.BrutalityShaders;
import net.goo.brutality.config.BrutalityClientConfig;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.Constants;
import net.goo.brutality.util.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.awt.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Brutality.MOD_ID)

public class RageMeter implements IGuiOverlay {
    private static final float[] outerColor = new float[3];
    private static final float[] innerColor = new float[3];

    public static void updateConfig() {
        Color outer = Color.decode(BrutalityClientConfig.RAGE_METER_FIRE_OUTER.get());
        outerColor[0] = outer.getRed() / 255F;
        outerColor[1] = outer.getGreen() / 255F;
        outerColor[2] = outer.getBlue() / 255F;

        Color inner = Color.decode(BrutalityClientConfig.RAGE_METER_FIRE_INNER.get());
        innerColor[0] = inner.getRed() / 255F;
        innerColor[1] = inner.getGreen() / 255F;
        innerColor[2] = inner.getBlue() / 255F;
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Player player = Minecraft.getInstance().player;
        if (shouldRender(player)) {
            BrutalityClientConfig.RAGE_METER_STYLE.get().rageMeterRenderer.render(guiGraphics, player);
        }
    }

    @FunctionalInterface
    public interface RageMeterRenderer {
        void render(GuiGraphics gui, Player player);
    }

    public static class ClassicRageMeterRenderer implements RageMeterRenderer {
        private static final long startTime = System.currentTimeMillis();

        ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/rage_meter/classic/bg.png");
        ResourceLocation FG = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/rage_meter/classic/fg.png");
        ResourceLocation ICON = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/rage_meter/classic/icon.png");

        public static final int bgW = 83, bgH = 16, fgW = 75, fgH = 5, iconW = 17, iconH = 18;

        private void renderFire(GuiGraphics gui, int x, int bottomY, int width, float percent) {
            int fireY = bottomY - width;                // bottom-aligned, never moves

            var shader = BrutalityShaders.getFireShader();
            if (shader != null) {
                float time = (System.currentTimeMillis() - startTime) / 1000f;
                shader.safeGetUniform("time").set(time);
                shader.safeGetUniform("intensity").set((float) (percent * 3 * BrutalityClientConfig.RAGE_METER_FIRE_INTENSITY.get()));  // intensity scales with rage
                shader.safeGetUniform("colour_1").set(innerColor[0], innerColor[1], innerColor[2], 1.0F);
                shader.safeGetUniform("colour_2").set(outerColor[0], outerColor[1], outerColor[2], 1.0F);
//                shader.safeGetUniform("colour_1").set(OUTER_COLOR[0], OUTER_COLOR[1], OUTER_COLOR[2], 1.0F);
//                shader.safeGetUniform("colour_2").set(INNER_COLOR[0], INNER_COLOR[1], INNER_COLOR[2], 1.0F);
            }

            RenderSystem.setShader(BrutalityShaders::getFireShader);
            var matrix = gui.pose().last().pose();

            BufferBuilder builder = Tesselator.getInstance().getBuilder();
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            float vTop = 0.0f;   // top of texture

            builder.vertex(matrix, x, bottomY, 0).uv(0.0f, 1.0F).endVertex();
            builder.vertex(matrix, x + width, bottomY, 0).uv(1.0f, 1.0F).endVertex();
            builder.vertex(matrix, x + width, fireY, 0).uv(1.0f, vTop).endVertex();
            builder.vertex(matrix, x, fireY, 0).uv(0.0f, vTop).endVertex();

            Tesselator.getInstance().end();
        }

        private static final ImprovedNoise NOISE_X = new ImprovedNoise(RandomSource.create());
        private static final ImprovedNoise NOISE_Y = new ImprovedNoise(RandomSource.createNewThreadLocalInstance());

        @Override
        public void render(GuiGraphics gui, Player player) {
            float percent = player.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP)
                    .resolve()
                    .map(cap -> cap.getRagePercentage(player))
                    .orElse(0F);

            percent = Math.min(1, percent);

            int visibleWidth = (int) (fgW * percent);

            Position position = BrutalityClientConfig.RAGE_METER_POSITION.get();
            int bgX = position.getX(gui.guiWidth(), bgW);
            int bgY = position.getY(gui.guiHeight(), bgH);
            int SHAKE_INTENSITY = BrutalityClientConfig.RAGE_METER_SHAKE_INTENSITY.get();

            if (player.hasEffect(BrutalityModMobEffects.ENRAGED.get())) {
                float time = player.tickCount;

                float offsetX = (float) NOISE_X.noise(time, 0.0, 0.0) * SHAKE_INTENSITY;
                float offsetY = (float) NOISE_Y.noise(time + 1000.0, 0.0, 0.0) * SHAKE_INTENSITY;

                bgX += (int) offsetX;
                bgY += (int) offsetY;
            }

            if (player.hasItemInSlot(EquipmentSlot.OFFHAND)) {
                if (position == Position.HOTBAR_LEFT) {
                    bgX -= Constants.offhandSlotSize + Constants.bigPad; // Offhand Slot + padding
                }
            }

            bgX += BrutalityClientConfig.RAGE_METER_X_OFFSET.get();
            bgY += BrutalityClientConfig.RAGE_METER_Y_OFFSET.get();

            int fgX = bgX + 4;
            int fgY = bgY + 8;
            int iconX = bgX + 33;
            int iconY = bgY - 8;

            renderFire(gui, bgX, bgY + 8, bgW, percent);

            gui.blit(BG, bgX, bgY, 0, 0, bgW, bgH, bgW, bgH);
            gui.blit(FG, fgX, fgY, 0, 0, visibleWidth, fgH, fgW, fgH);
            gui.blit(ICON, iconX, iconY, 0, 0, iconW, iconH, iconW, iconH);
        }
    }

    public enum Position {
        BOTTOM_LEFT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return Constants.bigPad;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - Constants.smallPad - elementHeight;
            }
        },
        BOTTOM_RIGHT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth - Constants.bigPad - elementWidth;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - Constants.smallPad - elementHeight;
            }
        },
        HOTBAR_TOP_RIGHT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth / 2 + 8;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - 41 - elementHeight;
            }
        },
        HOTBAR_TOP_LEFT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth / 2 - elementWidth - 8;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - 41 - elementHeight;
            }
        },
        HOTBAR_RIGHT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth / 2 + 91 + Constants.bigPad;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - Constants.mediumPad - elementHeight;
            }
        },
        HOTBAR_LEFT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth / 2 - 91 - Constants.bigPad - elementWidth;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - Constants.mediumPad - elementHeight;
            }
        };

        public abstract int getX(int screenWidth, int elementWidth);

        public abstract int getY(int screenHeight, int elementHeight);
    }

    public enum Style {
        CLASSIC(new ClassicRageMeterRenderer());
        public final RageMeterRenderer rageMeterRenderer;

        Style(RageMeterRenderer rageMeterRenderer) {
            this.rageMeterRenderer = rageMeterRenderer;
        }
    }


    public static boolean shouldRender(Player player) {
        if (player == null) return false;
        return CuriosApi.getCuriosInventory(player).resolve()
                .map(handler -> handler.isEquipped(stack -> stack.is(ModTags.Items.RAGE_ITEMS)))
                .orElse(false);
    }
}
