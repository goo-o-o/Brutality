package net.goo.brutality.client.gui.meters;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.config.BrutalityClientConfig;
import net.goo.brutality.client.renderers.BrutalityShaders;
import net.goo.brutality.util.CommonConstants;
import net.goo.brutality.util.magic.ManaHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Brutality.MOD_ID)

public class ManaMeter implements IGuiOverlay {

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        Player player = Minecraft.getInstance().player;
        if (player != null && shouldRender(player)) {
            Style.ORB.manaMeterRenderer.render(graphics, player);
        }
    }


    @FunctionalInterface
    public interface ManaMeterRenderer {
        void render(GuiGraphics gui, Player player);
    }

    public static class OrbManaMeterRenderer implements ManaMeterRenderer {
        private static final long startTime = System.currentTimeMillis();
        ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/mana_meter/orb/bg.png");
        ResourceLocation FG = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/mana_meter/orb/fg.png");


        public static final int bgW = 51, bgH = 51;

        private void renderOrb(GuiGraphics gui, int x, int y, int width, float percent) {
            int fireY = y - width;                // bottom-aligned, never moves

            var shader = BrutalityShaders.getManaOrbShader();
            if (shader != null) {
                float time = (System.currentTimeMillis() - startTime) / 1000f;
                shader.safeGetUniform("Time").set(time);
                shader.safeGetUniform("FillLevel").set(percent);  // intensity scales with rage
            }

            RenderSystem.setShader(BrutalityShaders::getManaOrbShader);
            var matrix = gui.pose().last().pose();

            BufferBuilder builder = Tesselator.getInstance().getBuilder();
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            builder.vertex(matrix, x, y, 0).uv(0.0f, 0.0F).endVertex();
            builder.vertex(matrix, x + width, y, 0).uv(1.0f, 0.0F).endVertex();
            builder.vertex(matrix, x + width, fireY, 0).uv(1.0f, 1.0F).endVertex();
            builder.vertex(matrix, x, fireY, 0).uv(0.0f, 1.0F).endVertex();

            Tesselator.getInstance().end();
        }

        float prevPercent;
        @Override
        public void render(GuiGraphics gui, Player player) {
            float percent = ManaHelper.getCurrentManaPercentage(player);
            float partialTick = Minecraft.getInstance().getPartialTick();

            float lerpedMana = Mth.lerp(partialTick, prevPercent, percent);
            lerpedMana = Math.min(1, lerpedMana);

            ManaMeter.Position position = BrutalityClientConfig.MANA_METER_POSITION.get();
            int bgX = position.getX(gui.guiWidth(), bgW);
            int bgY = position.getY(gui.guiHeight(), bgH);

            if (player.hasItemInSlot(EquipmentSlot.OFFHAND)) {
                if (position == ManaMeter.Position.HOTBAR_LEFT) {
                    bgX -= CommonConstants.offhandSlotSize + CommonConstants.bigPad; // Offhand Slot + padding
                }
            }

            bgX += BrutalityClientConfig.RAGE_METER_X_OFFSET.get();
            bgY += BrutalityClientConfig.RAGE_METER_Y_OFFSET.get();

            gui.blit(BG, bgX, bgY, 0, 0, bgW, bgH, bgW, bgH);
            renderOrb(gui, bgX, bgY + bgH, bgW, lerpedMana);
            gui.blit(FG, bgX, bgY, 0, 0, bgW, bgH, bgW, bgH);

            this.prevPercent = percent;
        }

    }

    public enum Position {
        BOTTOM_LEFT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return CommonConstants.bigPad;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - CommonConstants.smallPad - elementHeight;
            }
        },
        BOTTOM_RIGHT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth - CommonConstants.bigPad - elementWidth;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - CommonConstants.smallPad - elementHeight;
            }
        },
        TOP_LEFT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return CommonConstants.bigPad;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return CommonConstants.smallPad;
            }
        },
        TOP_RIGHT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth - CommonConstants.bigPad - elementWidth;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return CommonConstants.smallPad;
            }
        },
        HOTBAR_RIGHT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth / 2 + 91 + CommonConstants.bigPad;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - CommonConstants.mediumPad - elementHeight;
            }
        },
        HOTBAR_LEFT {
            @Override
            public int getX(int screenWidth, int elementWidth) {
                return screenWidth / 2 - 91 - CommonConstants.bigPad - elementWidth;
            }

            @Override
            public int getY(int screenHeight, int elementHeight) {
                return screenHeight - CommonConstants.mediumPad - elementHeight;
            }
        };

        public abstract int getX(int screenWidth, int elementWidth);

        public abstract int getY(int screenHeight, int elementHeight);
    }

    public enum Style {
        ORB(new OrbManaMeterRenderer());
        public final ManaMeterRenderer manaMeterRenderer;

        Style(ManaMeterRenderer manaMeterRenderer) {
            this.manaMeterRenderer = manaMeterRenderer;
        }
    }


    public static boolean shouldRender(Player player) {
        return true;
    }
}
