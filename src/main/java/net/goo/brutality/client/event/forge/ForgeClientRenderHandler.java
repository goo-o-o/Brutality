package net.goo.brutality.client.event.forge;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.goo.brutality.common.item.weapon.sword.RoyalGuardianSword;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.goo.brutality.util.EnvironmentColorManager.customSkyLight;
import static net.goo.brutality.util.EnvironmentColorManager.skyLightColor;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeClientRenderHandler {
    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            if (customSkyLight) {
                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                RenderSystem.setShaderColor(skyLightColor.x(), skyLightColor.y(), skyLightColor.z(), 1f);

                // Draw fullscreen quad
                BufferBuilder buffer = Tesselator.getInstance().getBuilder();
                buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
                buffer.vertex(-100, -100, -100).endVertex();
                buffer.vertex(-100, 100, -100).endVertex();
                buffer.vertex(100, 100, -100).endVertex();
                buffer.vertex(100, -100, -100).endVertex();
                BufferUploader.drawWithShader(buffer.end());

                RenderSystem.setShader(() -> {
                    ShaderInstance shader = GameRenderer.getRendertypeEntityTranslucentShader();
                    if (shader != null) {
                        shader.safeGetUniform("Brightness").set(-0.5f); // Negative = darker
                    }
                    return shader;
                });
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

            }

        }


        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;

            // Loop through every player the client can see
            for (Entity entity : level.entitiesForRendering()) {
                if (entity instanceof Player player)
                    if (player.isUsingItem() && player.getUseItem().is(BrutalityItems.ROYAL_GUARDIAN_SWORD.get())) {
                        RoyalGuardianSword.renderDisplayHitbox(player, event.getPoseStack());
                    }
            }
        }
    }

}