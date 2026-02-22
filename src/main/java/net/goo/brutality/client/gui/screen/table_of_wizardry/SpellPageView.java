package net.goo.brutality.client.gui.screen.table_of_wizardry;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.components.AbstractWidgetList;
import net.goo.brutality.client.gui.components.EnhancedStringWidget;
import net.goo.brutality.client.gui.screen.TableOfWizardryScreen;
import net.goo.brutality.client.renderers.block.TableOfWizardryRenderer;
import net.goo.brutality.common.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.common.recipe.ConjureRecipe;
import net.goo.brutality.common.registry.BrutalityRecipes;
import net.goo.brutality.util.ModResources;
import net.goo.brutality.util.magic.ManaHelper;
import net.goo.brutality.util.tooltip.SpellTooltipRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.goo.brutality.client.gui.screen.TableOfWizardryScreen.*;

public class SpellPageView extends TableOfWizardryView {
    public static final ResourceLocation BACK_BUTTON = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/back_arrow.png");
    public static final ResourceLocation BUTTON_NINE_SLICED = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/button_nine_sliced.png");
    public static final ResourceLocation BUTTON_NINE_SLICED_DARK = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/button_nine_sliced_dark.png");

    public SpellPageView(TableOfWizardryScreen screen) {
        super(screen);
    }

    @Override
    public void init() {
        if (this.screen.getBlockEntity().currentSpell == null) return;

        ImageButton btn = new ImageButton(screen.width / 2 + 70, screen.height / 2 - 83,
                12, 10, 0, 0, 10, BACK_BUTTON,
                12, 30, b -> {
            screen.showSection(TableOfWizardryBookSection.CONJURE);
            screen.getBlockEntity().currentSpell = null;
            screen.updateServerAndRefresh();
        });

        screen.addRenderableWidget(btn);

        int top = screen.height / 2 - 82;
        int leftX = screen.width / 2 - 82;

        TableOfWizardryBlockEntity blockEntity = screen.getBlockEntity();

        MutableComponent spellName = blockEntity.currentSpell.getTranslatedSpellName()
                .withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT));

        AbstractWidgetList leftList = new AbstractWidgetList(mc, 76, 104, top, leftX, 4, 2, 5);

        EnhancedStringWidget title = new EnhancedStringWidget.Builder(null, null, spellName, mc.font, 4).lineHeight(6).alignCenter()
                .withTextureNineSliced(BUTTON_NINE_SLICED_DARK, 0, 0, 0, 11, 11, 5, 5).build();

        EnhancedStringWidget categoriesWidget = new EnhancedStringWidget.Builder(null, null, SpellTooltipRenderer.getSpellCategoriesTooltip(blockEntity.currentSpell, false), mc.font, 4).lineHeight(8).alignCenter().textOffset(0, 1)
                .withTextureNineSliced(BUTTON_NINE_SLICED_DARK, 0, 0, 0, 11, 11, 5, 5).build();


        EnhancedStringWidget description = new EnhancedStringWidget.Builder(
                70 - 8, null, SpellTooltipRenderer.getSpellDescriptionsTooltip(blockEntity.currentSpell).withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT).withColor(ChatFormatting.DARK_GRAY)), mc.font,
                4).wordWrap().alignCenter().lineHeight(8).dropShadow(false)
                .withTextureNineSliced(BUTTON_NINE_SLICED, 0, 0, 0, 11, 11, 5, 5).build();

        leftList.add(title);
        leftList.add(categoriesWidget);
        leftList.add(description);
        screen.addRenderableWidget(leftList);

    }

    @Override
    public void render(GuiGraphics gui, int mx, int my, float partial) {
        if (mc.level != null) {
            mc.level.getRecipeManager().getAllRecipesFor(BrutalityRecipes.CONJURE_TYPE.get()).stream()
                    .filter(r -> r.spell() == screen.getBlockEntity().currentSpell)
                    .findFirst()
                    .ifPresent(recipe -> renderRecipeRequirements(mc.level, recipe, gui, mx, my));
        }
    }

    private void renderRecipeRequirements(Level level, ConjureRecipe recipe, GuiGraphics gui, int mx, int my) {
        int hX = screen.width / 2;
        int hY = screen.height / 2;
        Player player = Minecraft.getInstance().player;
        // Mana
        float requiredMana = recipe.mana();
        float currentMana = 0;
        if (player != null) {
            currentMana = ManaHelper.getMana(player);
        }

        Component manaIcon = Component.literal("💧").withStyle(s -> s.withFont(ModResources.ICON_FONT));
        Component comp = Component.literal(currentMana + "/" + requiredMana).withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT));
        gui.drawString(mc.font, manaIcon, hX + 7, hY - 81, TableOfWizardryScreen.WHITE, false);
        gui.drawString(mc.font, comp, hX + 7 + mc.font.width(manaIcon) + 1, hY - 81, currentMana >= requiredMana ? TableOfWizardryScreen.BLUE : TableOfWizardryRenderer.RED.getRGB(), false);

        // Pedestals Bg
        gui.blit(PEDESTALS_SHADE, hX + 8, hY - 64, 0, 0, 72, 72, 72, 72);
        gui.blit(recipe.spell().getIcon(), hX + 36, hY - 36, 0, 0, 16, 16, 16, 16);

        // 1. Get the actual items currently on the world pedestals in order
        List<ItemStack> actualPedestalItems = screen.getBlockEntity().getPedestalItems(); // Returns 8 items in order
        int[][] pos = {{36, -62}, {57, -57}, {62, -36}, {57, -15}, {36, -10}, {15, -15}, {10, -36}, {15, -57}};
        ItemStack hovered = ItemStack.EMPTY;

        // Iterate through the 8 visual slots
        // --- LAYER 1: Outlines (Bottom) ---
        for (int i = 0; i < pos.length; i++) {
            Ingredient ing = (i < recipe.ingredients().size()) ? recipe.ingredients().get(i) : Ingredient.EMPTY;
            if (ing.isEmpty()) continue;

            int drawX = hX + pos[i][0];
            int drawY = hY + pos[i][1];

            boolean isSlotCorrect = ing.test(actualPedestalItems.get(i));

            // Draw the selection/status outline first
            gui.blit(PEDESTALS_OUTLINE, drawX - 2, drawY - 2, 0, isSlotCorrect ? 0 : 20, 20, 20, 20, 40);
        }

        // --- LAYER 2: The Pedestal Base (Middle) ---
        // This renders once, on top of outlines but behind items
        gui.blit(PEDESTALS, hX + 8, hY - 64, 0, 0, 72, 72, 72, 72);

        // --- LAYER 3: Items & Tooltip Logic (Top) ---
        for (int i = 0; i < pos.length; i++) {
            Ingredient ing = (i < recipe.ingredients().size()) ? recipe.ingredients().get(i) : Ingredient.EMPTY;
            if (ing.isEmpty()) continue;

            int drawX = hX + pos[i][0];
            int drawY = hY + pos[i][1];
            ItemStack recipeStack = ing.getItems()[0];

            // Render the item on top of the pedestal texture
            gui.renderFakeItem(recipeStack, drawX, drawY);

            if (mx >= drawX && mx < drawX + 16 && my >= drawY && my < drawY + 16) {
                hovered = recipeStack;
            }
        }

        int resultX = hX + pos[0][0], resultY = hY + pos[2][1];
        gui.blit(screen.getBlockEntity().currentSpell.getIcon(), resultX, resultY, 0, 0, 16, 16, 16, 16);
        ItemStack result = recipe.getResultItem(level.registryAccess());
        if (result.getCount() != 1) {
            String s = String.valueOf(result.getCount());
            gui.pose().translate(0.0F, 0.0F, 200.0F);
            gui.drawString(mc.font, s, resultX + 19 - 2 - mc.font.width(s), resultY + 6 + 3, 16777215, true);
        }
        if (!hovered.isEmpty()) gui.renderTooltip(mc.font, hovered, mx, my);

        // Entity Requirements
        renderEntities(recipe.requiredEntities(), gui, mx, my);
    }

    private final Map<EntityType<?>, LivingEntity> dummyEntities = new HashMap<>();

    private LivingEntity getOrCreateDisplayEntity(EntityType<?> type) {
        return dummyEntities.computeIfAbsent(type, t -> mc.level != null ? (LivingEntity) t.create(mc.level) : null);
    }

    private void renderEntities(List<EntityType<?>> types, GuiGraphics gui, int mx, int my) {
        if (types.isEmpty()) return;
        int startX = screen.width / 2 + 5;
        int spacing = 78 / types.size();

        for (int i = 0; i < types.size(); i++) {
            LivingEntity entity = getOrCreateDisplayEntity(types.get(i));
            if (entity != null) {
                int rX = startX + (i * spacing) + (spacing / 2);
                InventoryScreen.renderEntityInInventoryFollowsMouse(gui, rX, screen.height / 2 + 23, 7, rX - mx, ((float) screen.height / 2 + 16) - my, entity);
            }
        }
    }

    @Override
    public void setFocused(boolean pFocused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
