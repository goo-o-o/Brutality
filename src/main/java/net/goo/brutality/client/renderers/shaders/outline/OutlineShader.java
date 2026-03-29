package net.goo.brutality.client.renderers.shaders.outline;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.config.BrutalityClientConfig;
import net.goo.brutality.client.event.forge.ShaderRenderEvents;
import net.goo.brutality.client.renderers.BrutalityRenderTypes;
import net.goo.brutality.client.renderers.shaders.FixedColorVertexConsumer;
import net.goo.brutality.client.renderers.shaders.PostShaderInstance;
import net.goo.brutality.util.render.ShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)

public class OutlineShader extends PostShaderInstance {
    @Override
    public ResourceLocation getShaderLocation() {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "shaders/post/item_outline_post.json");
    }

    @Override
    public void setUniforms(PostPass instance) {
        super.setUniforms(instance);


        instance.getEffect().setSampler("OutlineSampler", getSilhouetteTarget()::getColorTextureId);
//        Brutality.LOGGER.info("setSampler called with tex id: {}", getSilhouetteTarget().getColorTextureId());


        instance.getEffect().safeGetUniform("OutSize").set(
                (float) Minecraft.getInstance().getMainRenderTarget().width,
                (float) Minecraft.getInstance().getMainRenderTarget().height
        );
        instance.getEffect().safeGetUniform("Thickness").set(
                (float) ShaderRenderEvents.currentThickness
        );
    }


    public static void mixin(ItemDisplayContext context, ItemStack stack, ItemRenderer renderer, BakedModel model, int light, int overlay, PoseStack ps) {
        if (!BrutalityClientConfig.RENDER_OUTLINES.get()) return;
        if (!ShaderHelper.isDrawRenderTarget(Minecraft.getInstance().getMainRenderTarget())) return;
        if (context == ItemDisplayContext.GUI) return;

        boolean isFirstPerson = context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;

        Player player = isFirstPerson ? Minecraft.getInstance().player : OutlineStyles.getPlayer();

        OutlineStyle style = OutlineStyles.ITEM_STYLES.get(stack.getItem());


        if (style == null || !style.shouldRender(stack, player)) {
            OutlineStyles.clear();
            return;
        }

        int color = style.getColor(stack, player);
        if (FastColor.ARGB32.alpha(color) == 0) return;
        ShaderRenderEvents.currentThickness = style.getThickness(stack, player);

        VertexConsumer outlineConsumer;
        if (isFirstPerson) {
            ShaderRenderEvents.pendingHandShaders.add(style.shader);
            ShaderRenderEvents.capturedFirstPersonHandModelViewMat = new Matrix4f(RenderSystem.getModelViewMatrix());
            ShaderRenderEvents.capturedFirstPersonHandProjMat = new Matrix4f(RenderSystem.getProjectionMatrix());
            outlineConsumer = new FixedColorVertexConsumer(ShaderRenderEvents.getHandBuffer(style.shader).getBuffer(BrutalityRenderTypes.ITEM_OUTLINE), color);
        } else {
            ShaderRenderEvents.pendingWorldShaders.add(style.shader);
            outlineConsumer = new FixedColorVertexConsumer(ShaderRenderEvents.getWorldBuffer(style.shader).getBuffer(BrutalityRenderTypes.ITEM_OUTLINE), color);
        }

        renderer.renderModelLists(model, stack, light, overlay, ps, outlineConsumer);
        OutlineStyles.clear();
    }


}