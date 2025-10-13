package net.goo.brutality.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.goo.brutality.Brutality;
import net.goo.brutality.config.BrutalityClientConfig;
import net.goo.brutality.entity.capabilities.EntityCapabilities;
import net.goo.brutality.event.forge.client.ClientTickHandler;
import net.goo.brutality.item.weapon.tome.BaseMagicTome;
import net.goo.brutality.magic.IBrutalitySpell;
import net.goo.brutality.magic.SpellCastingHandler;
import net.goo.brutality.magic.SpellCooldownTracker;
import net.goo.brutality.magic.SpellStorage;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

import static net.goo.brutality.item.curios.charm.Sum.SUM_DAMAGE;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ModGui {

    private static final int SCREEN_COLOR = FastColor.ARGB32.color(255, 42, 45, 11);
    private static final int SCREEN_WIDTH = 86;
    private static final int SCREEN_HEIGHT = 37;
    private static final int CALC_PADDING_X = 7;
    private static final int CALC_PADDING_Y = 8;
    private static final int GRAPH_PADDING = 2;
    private static final int CALC_WIDTH = 100;
    private static final float RANGE = (float) (2 * Math.PI); // from -π to π
    private static final float AMPLITUDE_SCALE = 0.75F;
    private static final float SINE_AMPLITUDE = 5f * AMPLITUDE_SCALE;
    private static final float COSINE_AMPLITUDE = 1 * AMPLITUDE_SCALE;
    private static final int SINE_COLOR = FastColor.ARGB32.color(255, 67, 74, 172);
    private static final int COSINE_COLOR = FastColor.ARGB32.color(255, 182, 87, 103);
    private static final int GUI_WHITE = FastColor.ARGB32.color(255, 250, 252, 255);
    private static final int GUI_BLACK = FastColor.ARGB32.color(255, 25, 25, 25);
    private static int calcLeft;
    private static int calcTop;
    private static int screenLeft;
    private static int screenRight;
    private static int screenBottom;
    private static int graphLeft;
    private static int graphRight;
    private static int graphTop;
    private static int graphBottom;
    private static int graphHeight;
    private static int graphWidth;
    private static float currentPhase;

    // Cached window properties
    private static int cachedWindowWidth;
    private static int cachedWindowHeight;

    private static void computeVariables() {
        calcLeft = cachedWindowWidth - CALC_WIDTH - (int) (cachedWindowWidth * 0.05F); // 5% margin from right
        calcTop = cachedWindowHeight - (int) (cachedWindowHeight * 0.15F); // 15% margin from bottom

        screenLeft = calcLeft + CALC_PADDING_X;
        int screenTop = calcTop + CALC_PADDING_Y;
        screenRight = screenLeft + SCREEN_WIDTH;
        screenBottom = screenTop + SCREEN_HEIGHT;

        graphLeft = screenLeft + GRAPH_PADDING;
        graphRight = screenRight - GRAPH_PADDING;
        graphTop = screenTop + GRAPH_PADDING;
        graphBottom = screenBottom - GRAPH_PADDING;
        graphHeight = graphBottom - graphTop;
        graphWidth = graphRight - graphLeft;

        float currentTick = ClientTickHandler.getClientTick();
        currentPhase = currentTick * 0.025f;
    }

    private static Font FONT;

    @SubscribeEvent
    public static void onRender(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (FONT == null) FONT = mc.font;
        if (shouldSkipRender(player)) return;

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        GuiGraphics gui = event.getGuiGraphics();
        Window window = event.getWindow();

        // Cache window properties
        cachedWindowWidth = window.getGuiScaledWidth();
        cachedWindowHeight = window.getGuiScaledHeight();

        if (player.isHolding(itemStack -> itemStack.is(ModTags.Items.MAGIC_ITEMS))) {
            renderManaBar(gui, player);

            if (mainHand.getItem() instanceof BaseMagicTome) {
                renderSpellSelection(gui, player, mainHand);
                if (SpellCastingHandler.currentlyChannellingSpell(player, mainHand)) {
                    float channelProgress = SpellCastingHandler.getChannellingProgress(player, mainHand);
                    RadialProgressBarRenderer.renderProgressBar(mainHand, gui, getPixelX(BrutalityClientConfig.MANA_BAR_X.get()), getPixelY(BrutalityClientConfig.MANA_BAR_Y.get()), 0, channelProgress);
                }
            } else if (offHand.getItem() instanceof BaseMagicTome) {
                renderSpellSelection(gui, player, offHand);
                if (SpellCastingHandler.currentlyChannellingSpell(player, offHand)) {
                    float channelProgress = SpellCastingHandler.getChannellingProgress(player, offHand);
                    RadialProgressBarRenderer.renderProgressBar(offHand, gui, getPixelX(BrutalityClientConfig.MANA_BAR_X.get()), getPixelY(BrutalityClientConfig.MANA_BAR_Y.get()), 0, channelProgress);
                }
            }
        }

        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityModItems.SCIENTIFIC_CALCULATOR.get())) {
                computeVariables();
                renderAxesAndTexture(gui);
                handler.findFirstCurio(BrutalityModItems.SUM_CHARM.get()).ifPresent(slot -> renderSumDamageStored(gui, slot));
                handler.findFirstCurio(BrutalityModItems.EXPONENTIAL_CHARM.get()).ifPresent(slot -> renderComboCount(gui, player));
                handler.findFirstCurio(BrutalityModItems.SINE_CHARM.get()).ifPresent(slot -> renderSineWave(gui));
                handler.findFirstCurio(BrutalityModItems.COSINE_CHARM.get()).ifPresent(slot -> renderCosineWave(gui));
            }
            if (!handler.findCurios(stack -> stack.is(ModTags.Items.RAGE_ITEMS)).isEmpty()) {
                renderRageBar(gui, player);
            }
        });
    }

    private static final ResourceLocation MANA_BAR_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/mana_bar.png");
    private static final ResourceLocation MANA_BAR_FOREGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/mana_bar_fg.png");
    private static final ResourceLocation MANA_BAR_BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/mana_bar_bg.png");

    private static void renderManaBar(GuiGraphics gui, Player player) {
        float manaValue = player.getCapability(BrutalityCapabilities.PLAYER_MANA_CAP)
                .map(EntityCapabilities.PlayerManaCap::manaValue)
                .orElse(0F);
        AttributeInstance maxMana = player.getAttribute(ModAttributes.MAX_MANA.get());
        float manaPercent = maxMana != null ? manaValue / (float) maxMana.getValue() : 0;
        manaPercent = Mth.clamp(manaPercent, 0, 1);

        int diameter = 70;
        int radius = diameter / 2;
        int manaBarX = getPixelX(BrutalityClientConfig.MANA_BAR_X.get());
        int manaBarY = getPixelY(BrutalityClientConfig.MANA_BAR_Y.get());

        int visibleHeight = (int) (diameter * manaPercent);
        int textureY = diameter - visibleHeight;

        gui.blit(MANA_BAR_BACKGROUND_TEXTURE, manaBarX - radius, manaBarY - radius, 0, 0, diameter, diameter, diameter, diameter);
        if (visibleHeight > 0) {
            gui.blit(MANA_BAR_TEXTURE, manaBarX - radius, manaBarY - radius + (diameter - visibleHeight), 0, textureY, diameter, visibleHeight, diameter, diameter);
        }
        gui.blit(MANA_BAR_FOREGROUND_TEXTURE, manaBarX - radius, manaBarY - radius, 0, 0, diameter, diameter, diameter, diameter);
    }

    private static final ResourceLocation SPELL_CONTAINER_CD_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/spell_container_cd.png");

    private static void renderSpellSelection(GuiGraphics gui, Player player, ItemStack tome) {
        List<SpellStorage.SpellEntry> spellEntries = SpellStorage.getSpells(tome);
        float angleOffset = 0;
        int distanceFromCenter = 68; // Fixed pixel distance for spell orbit
        SpellStorage.SpellEntry selectedSpell = SpellStorage.getCurrentSpellEntry(tome);

        final float containerDiameter = 48F;
        final float iconDiameter = 36F;
        final float baseMiniDiameter = 16f;
        final float baseMiniRadius = baseMiniDiameter / 2f;
        final float baseContainerRadius = containerDiameter / 2f;
        final float baseIconRadius = iconDiameter / 2F;

        // Cache center position
        int centerX = getPixelX(BrutalityClientConfig.MANA_BAR_X.get());
        int centerY = getPixelY(BrutalityClientConfig.MANA_BAR_Y.get());

        for (SpellStorage.SpellEntry spellEntry : spellEntries) {
            boolean isSelected = selectedSpell != null &&
                    selectedSpell.spell() == spellEntry.spell() &&
                    selectedSpell.level() == spellEntry.level();
            int actualSpellLevel = IBrutalitySpell.getActualSpellLevel(player, spellEntry.spell(), spellEntry.level());
            String spellLevelString = String.valueOf(actualSpellLevel);

            int xComponent = Math.round(centerX + Mth.cos(angleOffset) * distanceFromCenter);
            int yComponent = Math.round(centerY + Mth.sin(angleOffset) * distanceFromCenter);

            gui.pose().pushPose();
            if (isSelected) {
                float scale = 1.5f;
                gui.pose().translate(xComponent, yComponent, 0);
                gui.pose().scale(scale, scale, 1);
                gui.pose().translate(-xComponent, -yComponent, 0);
            }

            IBrutalitySpell.MagicSchool school = spellEntry.spell().getSchool();

            gui.blit(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/spells/" + school + "/spell_container/bg.png"),
                    (int) (xComponent - baseContainerRadius), (int) (yComponent - baseContainerRadius),
                    0, 0, (int) containerDiameter, (int) containerDiameter, (int) containerDiameter, (int) containerDiameter);

            gui.blit(spellEntry.spell().getIcon(),
                    (int) (xComponent - baseIconRadius), (int) (yComponent - baseIconRadius),
                    0, 0, (int) iconDiameter, (int) iconDiameter, (int) iconDiameter, (int) iconDiameter);

            if (SpellCooldownTracker.isOnCooldown(player, spellEntry.spell())) {
                float progress = 1 - SpellCooldownTracker.getCooldownProgress(player, spellEntry.spell());
                int visibleHeight = Math.round(iconDiameter * progress);
                gui.blit(SPELL_CONTAINER_CD_TEXTURE,
                        (int) (xComponent - baseIconRadius),
                        (int) (yComponent + (iconDiameter - visibleHeight) - baseIconRadius),
                        0, (int) iconDiameter - visibleHeight,
                        (int) iconDiameter, visibleHeight,
                        (int) iconDiameter, (int) iconDiameter);
            }

            if (isSelected && SpellCastingHandler.currentlyCastingContinuousSpell(player, tome)) {
                gui.blit(SPELL_CONTAINER_CD_TEXTURE,
                        (int) (xComponent - baseIconRadius),
                        (int) (yComponent - baseIconRadius),
                        0F, iconDiameter,
                        (int) iconDiameter, (int) iconDiameter,
                        (int) iconDiameter, (int) iconDiameter);
            }

            gui.blit(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/spells/" + school + "/spell_container/fg.png"),
                    (int) (xComponent - baseContainerRadius), (int) (yComponent - baseContainerRadius),
                    0, 0, (int) containerDiameter, (int) containerDiameter, (int) containerDiameter, (int) containerDiameter);

            gui.blit(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/spells/" + school + "/spell_container/mini.png"),
                    (int) (xComponent - baseMiniRadius),
                    (int) (yComponent - baseIconRadius - baseMiniRadius / 2),
                    0, 0, (int) baseMiniDiameter, (int) baseMiniDiameter, (int) baseMiniDiameter, (int) baseMiniDiameter);

            int textWidth = FONT.width(spellLevelString);
            float targetWidth = 8;
            float textScale = Math.min(1f, targetWidth / textWidth);

            {
                float verticalOffset = (FONT.lineHeight - (FONT.lineHeight * textScale)) / 2f;
                gui.pose().pushPose();
                gui.pose().translate(xComponent + 0.5F, yComponent - baseIconRadius + verticalOffset, 0);
                gui.pose().scale(textScale, textScale, 1f);
                gui.drawString(FONT, spellLevelString,
                        -textWidth / 2f, 0,
                        school == IBrutalitySpell.MagicSchool.CELESTIA ? GUI_BLACK : GUI_WHITE, true);
                gui.pose().popPose();
            }

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            gui.pose().popPose();

            angleOffset += (float) (Math.PI / 4);
        }
    }

    private static final ResourceLocation RAGE_BAR_ICON = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/rage_bar_icon.png");
    private static final ResourceLocation RAGE_BAR = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/rage_bar.png");
    private static final ResourceLocation RAGE_BAR_METER = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/rage_bar_meter.png");

    private static void renderRageBar(GuiGraphics gui, Player player) {
        AttributeInstance maxRage = player.getAttribute(ModAttributes.MAX_RAGE.get());
        if (maxRage != null) {
            player.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP).ifPresent(cap -> {
                int x = getPixelX(0.05F); // 5% from left
                int y = getPixelY(0.95F); // 5% from bottom
                gui.blitNineSliced(RAGE_BAR, x, y, ((int) maxRage.getValue()) + 8, 20, 4, 10, 4, 10, 256, 256, 0, 0);
                if (cap.rageValue() > 0 && cap.rageValue() <= maxRage.getValue()) {
                    gui.blitNineSliced(RAGE_BAR_METER, x + 3, y + 3, ((int) cap.rageValue()) + 4, 8, 1, 4, 2, 4, 256, 256, 3, 3);
                }
                gui.blit(RAGE_BAR_ICON, x - 16, y - 18, 1, 0, 0, 32, 36, 32, 36);
            });
        }
    }

    private static final ResourceLocation calcSprite = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/scientific_calculator.png");

    private static void renderSumDamageStored(GuiGraphics gui, SlotResult slot) {
        float damageStored = slot.stack().getOrCreateTag().getFloat(SUM_DAMAGE);
        gui.drawCenteredString(FONT, String.format("%.2f", damageStored), screenLeft + FONT.width("15") + GRAPH_PADDING, screenBottom - FONT.lineHeight - GRAPH_PADDING, SCREEN_COLOR);
    }

    private static void renderComboCount(GuiGraphics gui, Player player) {
        player.getCapability(BrutalityCapabilities.PLAYER_COMBO_CAP).ifPresent(cap -> {
            gui.drawCenteredString(FONT, String.valueOf(cap.hitCount()), screenRight - FONT.width("15") - GRAPH_PADDING, screenBottom - FONT.lineHeight - GRAPH_PADDING, SCREEN_COLOR);
        });
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
            float value = Mth.sin(currentPhase + relativeX) * SINE_AMPLITUDE * AMPLITUDE_SCALE;
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
        return player == null || Minecraft.getInstance().options.hideGui || Minecraft.getInstance().screen != null;
    }

    // Helper methods for percentage-based positioning
    private static int getPixelX(double percentX) {
        return Math.round((float) (cachedWindowWidth * percentX));
    }

    private static int getPixelY(double percentY) {
        return Math.round((float) (cachedWindowHeight * percentY));
    }
}