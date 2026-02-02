package net.goo.brutality.client.gui.screen;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.components.TextWithIconImageButton;
import net.goo.brutality.client.renderers.block.TableOfWizardryRenderer;
import net.goo.brutality.common.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.common.magic.table_of_wizardry.ConjureRecipe;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.serverbound.ServerboundTableOfWizardryUpdatePacket;
import net.goo.brutality.common.registry.BrutalityRecipes;
import net.goo.brutality.common.registry.BrutalitySpells;
import net.goo.brutality.util.ModResources;
import net.goo.brutality.util.RenderUtils;
import net.goo.brutality.util.magic.ManaHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;

public class TableOfWizardryScreen extends Screen {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/bg.png");
    private static final ResourceLocation PEDESTALS = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/pedestals.png");
    private static final ResourceLocation PEDESTALS_SHADE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/pedestals_shade.png");
    private static final ResourceLocation PEDESTALS_OUTLINE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/pedestals_outline.png");
    private static final ResourceLocation BUTTON_NINE_SLICED = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/button_nine_sliced.png");
    private static final ResourceLocation BUTTON_NINE_SLICED_DARK = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/button_nine_sliced_dark.png");
    private static final ResourceLocation BACK_BUTTON = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/back_arrow.png");


    private static final int BG_WIDTH = 196;
    private static final int BG_HEIGHT = 220;
    private static final int LIST_WIDTH = 78;
    private static final int LIST_HEIGHT = 108; // 134 - 26

    // Colors
    public static final int WHITE = 0xFFE0E4E3;
    public static final int DARK_WHITE = 0xFFC0CBCC;
    public static final int LIGHTER_GRAY = 0xFF788492;
    public static final int LIGHT_GRAY = 0xFF596473;
    public static final int GRAY = 0xFF424B5B;
    public static final int DARK_GRAY = 0xFF35394A;
    public static final int BLUE = FastColor.ARGB32.color(255, 12, 111, 215);

    private final TableOfWizardryBlockEntity block;
    private final Map<EntityType<?>, LivingEntity> dummyEntities = new HashMap<>();
    private double schoolListScroll;

    public enum TableOfWizardryBookSection {
        CONJURE, SYNTHESISE, AUGMENT, INSCRIBE, EXPUNGE, SIPHON;
        final ResourceLocation bookmark;

        TableOfWizardryBookSection() {
            this.bookmark = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/" + this.name().toLowerCase(Locale.ROOT) + "_bookmark.png");
        }
    }

    public TableOfWizardryScreen(Component pTitle, TableOfWizardryBlockEntity block) {
        super(pTitle);
        this.block = block;
    }

    @Override
    protected void init() {
        initBookmarks();

        switch (this.block.currentState) {
            case SECTION_VIEW -> {
                if (this.block.currentSection == TableOfWizardryBookSection.CONJURE) initConjureView();
            }
            case SPELL_PAGE -> initSpellPageView();
        }
    }

    private void initBookmarks() {
        int startX = this.width / 2 - 84;
        int baseY = this.height / 2 - 107;

        TableOfWizardryBookSection[] sections = TableOfWizardryBookSection.values();
        for (int i = 0; i < sections.length; i++) {
            TableOfWizardryBookSection section = sections[i];
            boolean isSelected = (this.block.currentSection == section);

            int xPos = startX + (i * 28); // 26 width + 2 spacing
            int yPos = isSelected ? baseY : baseY + 6;
            int h = isSelected ? 22 : 16;

            ImageButton btn = new ImageButton(xPos, yPos, 26, h, 0, 0, 22, section.bookmark, 26, 66, b -> showSection(section)) {
                @Override
                public void playDownSound(SoundManager s) {
                    s.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
                }
            };

            btn.setTooltip(Tooltip.create(Component.translatable("tooltip." + Brutality.MOD_ID + "." + section.name().toLowerCase())));
            btn.active = !isSelected;
            this.addRenderableWidget(btn);
        }
    }

    private void initConjureView() {
        int top = this.height / 2 - 84;
        int leftPos = this.width / 2 - 82;

        // Left Page: Schools
        EntryList schoolList = new EntryList(this.minecraft, LIST_WIDTH, LIST_HEIGHT, top, top + LIST_HEIGHT, 18);
        schoolList.setLeftPos(leftPos);

        for (IBrutalitySpell.MagicSchool school : IBrutalitySpell.MagicSchool.values()) {
            String schoolName = school.name().toLowerCase(Locale.ROOT);
            schoolList.addEntry(new EntryList.Entry(
                    Component.translatable("school." + Brutality.MOD_ID + "." + schoolName), 70, 18,
                    btn -> {
                        this.block.currentSchool = school;
                        this.schoolListScroll = schoolList.getScrollAmount();
                        this.rebuildWidgets();
                    },
                    ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/spells/" + schoolName + "/icon.png")
            ).setActive(school != this.block.currentSchool));
        }

        schoolList.setScrollAmount(schoolListScroll);
        this.addRenderableWidget(schoolList);

        // Right Page: Spells
        if (this.block.currentSchool != null) {
            EntryList spellList = new EntryList(this.minecraft, LIST_WIDTH, LIST_HEIGHT, top, top + LIST_HEIGHT, 18);
            spellList.setLeftPos(this.width / 2 + 7);

            BrutalitySpells.getSpellsFromSchool(this.block.currentSchool).forEach(spell ->
                    spellList.addEntry(new EntryList.Entry(spell.getTranslatedSpellName().copy(), 70, 18,
                            btn -> showSpellPage(spell), null))
            );
            this.addRenderableWidget(spellList);
        }
    }

    private void initSpellPageView() {
        if (this.block.currentSpell == null) return;

        ImageButton btn = new ImageButton(this.width / 2 + 70, this.height / 2 - 83, 12, 10, 0, 0, 10, BACK_BUTTON, 12, 30, b -> showSection(TableOfWizardryBookSection.CONJURE));

        this.addRenderableWidget(btn);
    }

// --- RENDERING ---

    @Override
    public void render(GuiGraphics gui, int mx, int my, float partial) {
        this.renderBackground(gui);
        gui.blit(BACKGROUND, (this.width - BG_WIDTH) / 2, (this.height - BG_HEIGHT) / 2, 0, 0, BG_WIDTH, BG_HEIGHT, BG_WIDTH, BG_HEIGHT);

        if (this.block.currentState == TableOfWizardryBlockEntity.GuiState.SPELL_PAGE && this.block.currentSpell != null) {
            renderSpellDetails(gui, mx, my);
        }
        super.render(gui, mx, my, partial);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double pDelta) {
        if (this.block.currentState == TableOfWizardryBlockEntity.GuiState.SPELL_PAGE) {
            int areaX = this.width / 2 - 76;
            int areaY = this.height / 2 - 62; // Y + 18 from the name
            if (mouseX >= areaX && mouseX <= areaX + 64 && mouseY >= areaY && mouseY <= areaY + 80) {
                // Sensitivity of 10 pixels per notch is usually comfortable
                this.descriptionScroll -= pDelta * 10;
                return true;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, pDelta);
    }

    private double descriptionScroll = 0;

    private void renderSpellDetails(GuiGraphics gui, int mx, int my) {
        int x = this.width / 2 - 76;
        int viewportY = this.height / 2 - 80; // The fixed top of our "window"
        int padding = 5;
        int viewportWidth = 64;
        int paddedWidth = viewportWidth - padding - padding;
        int viewportHeight = 110 - padding - padding;

        // 1. Prepare Content
        MutableComponent spellName = this.block.currentSpell.getTranslatedSpellName()
                .withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT));

        StringBuilder catBuilder = new StringBuilder();
        this.block.currentSpell.getCategories().forEach(c -> catBuilder.append(c.icon));
        Component categories = Component.literal(catBuilder.toString());

        MutableComponent desc = Component.empty();
        IntStream.rangeClosed(1, block.currentSpell.getDescriptionCount()).forEach(i ->
                desc.append(block.currentSpell.getSpellDescription(i)).append(". ")
        );
        var formattedDesc = desc.withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT));

        // 2. Calculate Heights (using a small constant padding instead of lineHeight)
        int h1 = font.wordWrapHeight(spellName, paddedWidth) + padding + padding;
        int w1 = RenderUtils.wordWrapWidth(font, spellName, paddedWidth) + padding + padding;
        int h2 = font.wordWrapHeight(categories, paddedWidth) + padding + padding;
        int w2 = RenderUtils.wordWrapWidth(font, categories, paddedWidth) + padding + padding;
        int h3 = font.wordWrapHeight(formattedDesc, paddedWidth) + padding + padding;
        int totalContentHeight = padding + h1 + padding + h2 + padding + h3;

        // 3. Update Scroll Limits
        int maxScroll = Math.max(0, totalContentHeight - viewportHeight);
        descriptionScroll = Mth.clamp(descriptionScroll, 0, maxScroll);

        // 4. Render with Scissoring
        gui.enableScissor(x, viewportY, x + viewportWidth, viewportY + viewportHeight);
        gui.pose().pushPose();

        // Everything inside here is moved by the scroll
        gui.pose().translate(0, -descriptionScroll, 0);

        // Draw Name


        // we need to emulate the word wrap centered logic, and then nine sliced blit around it with some padding (specifiable)
        gui.blitNineSlicedSized(BUTTON_NINE_SLICED, x + (viewportWidth - w1) / 2, viewportY, w1, h1, 5, 5, 11, 11, 0, 0, 11, 11);
        RenderUtils.drawWordWrapCentered(gui, font, spellName, x + padding, viewportY + padding, viewportWidth - padding - padding, DARK_GRAY);
//        RenderUtils.drawBoxedWordWrapCentered(gui, BUTTON_NINE_SLICED, font, spellName, x, viewportY, 3, viewportWidth, GRAY, 3, 1, 11, 11, 11, 11);


        int currentY = viewportY + h1 + padding;
        gui.blitNineSlicedSized(BUTTON_NINE_SLICED_DARK, x + (viewportWidth - w2) / 2, currentY, w2 + 1, h2, 5, 5, 11, 11, 0, 0, 11, 11);
        RenderUtils.drawWordWrapCentered(gui, font, categories, x + 1, currentY + padding + 1, viewportWidth, WHITE);

        // Draw Description
        currentY += h2 + padding;
        gui.drawWordWrap(font, formattedDesc, x, currentY, viewportWidth, GRAY);

        gui.pose().popPose();
        gui.disableScissor();

        // Recipe
        if (minecraft != null && minecraft.level != null) {
            minecraft.level.getRecipeManager().getAllRecipesFor(BrutalityRecipes.CONJURE_TYPE.get()).stream()
                    .filter(r -> r.spell() == block.currentSpell)
                    .findFirst()
                    .ifPresent(recipe -> renderRecipeRequirements(recipe, gui, mx, my));
        }

    }

    private void renderRecipeRequirements(ConjureRecipe recipe, GuiGraphics gui, int mx, int my) {
        int hX = this.width / 2;
        int hY = this.height / 2;
        Player player = Minecraft.getInstance().player;
        // Mana
        float requiredMana = recipe.mana();
        float currentMana = ManaHelper.getMana(player);
        Component comp = Component.literal(currentMana + "/" + requiredMana).withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT));
        gui.drawString(font, Component.literal("💧"), hX + 7, hY - 81, WHITE, false);
        gui.drawString(font, comp, hX + 7 + font.width("💧") + 1, hY - 81, currentMana >= requiredMana ? BLUE : TableOfWizardryRenderer.RED.getRGB(), false);

        // Pedestals Bg
        gui.blit(PEDESTALS_SHADE, hX + 8, hY - 64, 0, 0, 72, 72, 72, 72);
        gui.blit(recipe.spell().getIcon(), hX + 36, hY - 36, 0, 0, 16, 16, 16, 16);

        // 1. Get the actual items currently on the world pedestals in order
        List<ItemStack> actualPedestalItems = block.getPedestalItems(); // Returns 8 items in order
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

        gui.blit(this.block.currentSpell.getIcon(), pos[0][0], pos[2][1], 0, 0, 16, 16, 16, 16);
        if (!hovered.isEmpty()) gui.renderTooltip(font, hovered, mx, my);

        // Entity Requirements
        renderEntities(recipe.requiredEntities(), gui, mx, my);
    }

    private void renderEntities(List<EntityType<?>> types, GuiGraphics gui, int mx, int my) {
        if (types.isEmpty()) return;
        int startX = this.width / 2 + 5;
        int spacing = 78 / types.size();

        for (int i = 0; i < types.size(); i++) {
            LivingEntity entity = getOrCreateDisplayEntity(types.get(i));
            if (entity != null) {
                int rX = startX + (i * spacing) + (spacing / 2);
                InventoryScreen.renderEntityInInventoryFollowsMouse(gui, rX, this.height / 2 + 23, 7, rX - mx, ((float) this.height / 2 + 16) - my, entity);
            }
        }
    }

// --- STATE MANAGEMENT ---

    private void showSection(TableOfWizardryBookSection section) {
        this.block.currentSection = section;
        this.block.currentState = TableOfWizardryBlockEntity.GuiState.SECTION_VIEW;
        this.block.currentSpell = null;
        updateServerAndRefresh();
    }

    private void showSpellPage(BrutalitySpell spell) {
        this.block.currentSpell = spell;
        this.descriptionScroll = 0;
        this.block.currentState = TableOfWizardryBlockEntity.GuiState.SPELL_PAGE;
        updateServerAndRefresh();
    }

    private void updateServerAndRefresh() {
        ResourceLocation id = block.currentSpell != null ? BrutalitySpells.getIdFromSpell(block.currentSpell) : null;
        PacketHandler.sendToServer(new ServerboundTableOfWizardryUpdatePacket(block.getBlockPos(), block.currentSection.name(), block.currentState.name(), id));
        this.rebuildWidgets();
    }

    private LivingEntity getOrCreateDisplayEntity(EntityType<?> type) {
        return dummyEntities.computeIfAbsent(type, t -> (minecraft != null && minecraft.level != null) ? (LivingEntity) t.create(minecraft.level) : null);
    }

    @Override
    public boolean keyPressed(int key, int scan, int mod) {
        if (minecraft != null && minecraft.options.keyInventory.matches(key, scan)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(key, scan, mod);
    }

// --- INNER CLASSES ---

    public static class EntryList extends ObjectSelectionList<EntryList.Entry> {
        public EntryList(Minecraft mc, int w, int h, int y0, int y1, int ih) {
            super(mc, w, h, y0, y1, ih);
            this.setRenderBackground(false);
            this.setRenderTopAndBottom(false);
            this.setRenderSelection(false);
        }

        @Override
        public int addEntry(@NotNull Entry pEntry) {
            return super.addEntry(pEntry);
        }

        @Override
        public int getRowLeft() {
            return getLeft();
        }

        @Override
        public int getMaxScroll() {
            return Math.max(0, this.getMaxPosition() - (this.y1 - this.y0));
        }

        @Override
        protected int getRowTop(int pIndex) {
            return this.y0 - (int) this.getScrollAmount() + pIndex * this.itemHeight;
        }

        @Override
        public int getRowWidth() {
            return this.width;
        }

        @Override
        protected int getScrollbarPosition() {
            return this.getLeft() + this.width - 6;
        }

        @Override
        public void render(GuiGraphics gui, int mx, int my, float pt) {
            this.enableScissor(gui);
            this.renderList(gui, mx, my, pt);
            gui.disableScissor();
            renderCustomScrollbar(gui);
        }

        private void renderCustomScrollbar(GuiGraphics pGuiGraphics) {
            int scrollbarX = this.getScrollbarPosition();
            int scrollbarThickness = 5;
            int scrollbarEndX = scrollbarX + scrollbarThickness;
            int maxScroll = this.getMaxScroll();
            if (maxScroll > 0) {
                // Calculate the height of the scrollbar "thumb"
                int scrollbarHeight = (int) ((float) ((this.y1 - this.y0) * (this.y1 - this.y0)) / (float) this.getMaxPosition());
                scrollbarHeight = Mth.clamp(scrollbarHeight, 32, this.y1 - this.y0 - 8);

                // Calculate the Y position of the thumb
                int scrollbarY = (int) this.getScrollAmount() * (this.y1 - this.y0 - scrollbarHeight) / maxScroll + this.y0;
                if (scrollbarY < this.y0) {
                    scrollbarY = this.y0;
                }
                pGuiGraphics.fill(scrollbarX, this.y0, scrollbarEndX, this.y1, GRAY);
                pGuiGraphics.renderOutline(scrollbarX, this.y0, scrollbarThickness, this.height, DARK_GRAY);
                // top left part
                pGuiGraphics.fill(scrollbarX, scrollbarY, scrollbarX + scrollbarThickness, scrollbarY + scrollbarHeight, LIGHTER_GRAY);
                // bottom right part
                pGuiGraphics.fill(scrollbarX + 1, scrollbarY + 1, scrollbarX + scrollbarThickness - 1, scrollbarY + scrollbarHeight - 1, LIGHT_GRAY);
                pGuiGraphics.renderOutline(scrollbarX + 1, scrollbarY + 1, scrollbarThickness - 2, scrollbarHeight - 2, WHITE);
                pGuiGraphics.fill(scrollbarX + 2, scrollbarY + 2, scrollbarX + scrollbarThickness - 2, scrollbarY + scrollbarHeight - 2, DARK_WHITE);
            }
        }


        public static class Entry extends ObjectSelectionList.Entry<Entry> {
            public final TextWithIconImageButton button;

            public Entry(MutableComponent name, int w, int h, Button.OnPress onPress, @Nullable ResourceLocation icon) {
                this.button = new TextWithIconImageButton(0, 0, w, h, 0, 0, h,
                        ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/button.png"),
                        w, h * 3, onPress, name.withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT)));
                this.button.hoveredOrFocusedTextColor = LIGHT_GRAY;
                this.button.notHoveredOrFocusedTextColor = GRAY;
                this.button.inactiveTextColor = DARK_GRAY;
                this.button.icon = icon;
                this.button.dropShadow = false;
            }

            public Entry setActive(boolean active) {
                this.button.active = active;
                return this;
            }

            @Override
            public void render(GuiGraphics g, int i, int top, int left, int w, int h, int mx, int my, boolean ho, float p) {
                button.setX(left);
                button.setY(top);
                button.render(g, mx, my, p);
            }

            @Override
            public boolean mouseClicked(double mx, double my, int b) {
                return button.mouseClicked(mx, my, b);
            }

            @Override
            public @NotNull Component getNarration() {
                return button.getMessage();
            }
        }
    }
}