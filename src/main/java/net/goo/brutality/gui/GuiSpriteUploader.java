package net.goo.brutality.gui;

import net.goo.brutality.Brutality;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class GuiSpriteUploader extends TextureAtlasHolder {

    public static final ResourceLocation ATLAS_LOCATION =
        ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/atlas/gui.png");

    private static final ResourceLocation RELOAD_ID =
        ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "gui");

    private static GuiSpriteUploader instance;

    private GuiSpriteUploader(TextureManager textureManager) {
        super(textureManager, ATLAS_LOCATION, RELOAD_ID);
    }

    public static void init() {
        Minecraft.getInstance().execute(() -> instance = new GuiSpriteUploader(Minecraft.getInstance().getTextureManager()));
    }

    public static GuiSpriteUploader instance() {
        return instance;
    }

    public TextureAtlasSprite getSprite(String path) {
        return this.getSprite(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, path));
    }

    public @NotNull TextureAtlasSprite getSprite(@NotNull ResourceLocation location) {
        return super.getSprite(location);
    }
}