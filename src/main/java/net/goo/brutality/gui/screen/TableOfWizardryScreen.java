package net.goo.brutality.gui.screen;

import net.goo.brutality.Brutality;
import net.goo.brutality.gui.components.TextWithIconImageButton;
import net.goo.brutality.magic.BrutalityModSpells;
import net.goo.brutality.magic.IBrutalitySpell;
import net.goo.brutality.magic.table_of_wizardry.ConjureRecipe;
import net.goo.brutality.registry.BrutalityRecipes;
import net.goo.brutality.util.ModResources;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TableOfWizardryScreen extends Screen {
    private final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/bg.png");

    // Colors
    public static final int WHITE = FastColor.ARGB32.color(255, 224, 228, 227);
    public static final int DARK_WHITE = FastColor.ARGB32.color(255, 192, 203, 205);
    public static final int LIGHTER_GRAY = FastColor.ARGB32.color(255, 120, 132, 146);
    public static final int LIGHT_GRAY = FastColor.ARGB32.color(255, 89, 100, 115);
    public static final int GRAY = FastColor.ARGB32.color(255, 66, 75, 91);
    public static final int DARK_GRAY = FastColor.ARGB32.color(255, 53, 57, 74);

    // State Variables
    private GuiState currentState = GuiState.SECTION_VIEW;
    private TableOfWizardryBookSection currentSection = TableOfWizardryBookSection.CONJURE;
    private IBrutalitySpell.MagicSchool currentSchool = IBrutalitySpell.MagicSchool.COSMIC;
    private IBrutalitySpell currentSpell;
    private double schoolListScroll;

    private enum GuiState {
        SECTION_VIEW,
        SPELL_PAGE
    }

    public enum TableOfWizardryBookSection {
        CONJURE, SYNTHESISE, AUGMENT, INSCRIBE, EXPUNGE;
        final ResourceLocation bookmark;

        TableOfWizardryBookSection() {
            this.bookmark = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/" + this.name().toLowerCase(Locale.ROOT) + "_bookmark.png");
        }
    }

    public TableOfWizardryScreen(Component pTitle) {
        super(pTitle);
    }

    // --- STATE MACHINE METHODS ---

    public void showSection(TableOfWizardryBookSection section) {
        this.currentSection = section;
        this.currentState = GuiState.SECTION_VIEW;
        this.currentSpell = null;
        this.clearWidgets();
        this.rebuildWidgets();
    }

    public void showSpellPage(IBrutalitySpell spell) {
        this.currentSpell = spell;
        this.currentState = GuiState.SPELL_PAGE;
        this.clearWidgets();
        this.rebuildWidgets();
    }

    @Override
    protected void init() {
        initBookmarks();

        switch (this.currentState) {
            case SECTION_VIEW -> {
                if (this.currentSection == TableOfWizardryBookSection.CONJURE) initConjure();
                // Add other sections here...
            }
            case SPELL_PAGE -> initSpellPageView();
        }
    }

    private void initConjure() {
        int bgWidth = 196;
        int bgHeight = 220;
        int topPos = (this.height - bgHeight) / 2;
        int listWidth = 78;
        int top = topPos + 26;
        int bottom = topPos + 134;

        // Left Page: School List
        int leftPos = (this.width - bgWidth) / 2 + 16;
        EntryList schoolList = new EntryList(this.minecraft, listWidth, bottom - top, top, bottom, 18);
        schoolList.setLeftPos(leftPos);

        for (IBrutalitySpell.MagicSchool school : IBrutalitySpell.MagicSchool.values()) {
            String schoolName = school.name().toLowerCase(Locale.ROOT);
            EntryList.Entry entry = new EntryList.Entry(
                    Component.translatable("school." + Brutality.MOD_ID + "." + schoolName), 70, 18,
                    btn -> {
                        this.currentSchool = school;
                        this.schoolListScroll = schoolList.getScrollAmount();
                        this.rebuildWidgets();
                    },
                    ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/spells/" + schoolName + "/icon.png"));
            entry.button.active = school != currentSchool;
            schoolList.addEntry(entry);
        }


        schoolList.setScrollAmount(schoolListScroll);
        this.addRenderableWidget(schoolList);

        // Right Page: Spell List
        if (this.currentSchool != null) {
            int rightListLeft = this.width / 2 + 7;
            EntryList spellList = new EntryList(this.minecraft, listWidth, bottom - top, top, bottom, 18);
            spellList.setLeftPos(rightListLeft);

            List<IBrutalitySpell> spells = BrutalityModSpells.getSpellsFromSchool(currentSchool);
            for (IBrutalitySpell spell : spells) {
                spellList.addEntry(new EntryList.Entry(
                        spell.getTranslatedSpellName().copy(), 70, 18,
                        btn -> {
                            showSpellPage(spell);
                            this.schoolListScroll = schoolList.getScrollAmount();
                        },
                        null
                ));
            }
            this.addRenderableWidget(spellList);
        }
    }

    private void initSpellPageView() {
        if (this.currentSpell == null) return;

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Back Button to return to list
        this.addRenderableWidget(Button.builder(Component.literal("Back"), b -> {
                    showSection(TableOfWizardryBookSection.CONJURE);
                })
                .bounds(centerX + 10, centerY + 80, 40, 16).build());

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        renderBg(pGuiGraphics);

        if (this.currentState == GuiState.SPELL_PAGE && this.currentSpell != null) {
            renderSpellContent(pGuiGraphics, pMouseX, pMouseY);
        }

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    private void renderSpellContent(GuiGraphics gui, int mouseX, int mouseY) {
        // 1. Draw Spell Name
        int x = this.width / 2 + 15;
        int y = (this.height - 220) / 2 + 30;
        gui.drawString(this.font, this.currentSpell.getTranslatedSpellName(), x, y, GRAY, false);

        // 2. Fetch and Render Recipe
        if (this.minecraft != null && this.minecraft.level != null) {
            this.minecraft.level.getRecipeManager()
                    .getAllRecipesFor(BrutalityRecipes.CONJURE_TYPE.get())
                    .stream()
                    .filter(recipe -> recipe.spell() == this.currentSpell)
                    .findFirst()
                    .ifPresent(recipe -> renderPedestal(recipe, gui, mouseX, mouseY));
        }
    }

    private static final ResourceLocation PEDESTAL = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/pedestals.png");

    private void renderPedestal(ConjureRecipe recipe, GuiGraphics gui, int mouseX, int mouseY) {
        int halfWidth = this.width / 2;
        int halfHeight = this.height / 2;
        ItemStack hoveredStack = ItemStack.EMPTY;

        int pedestalX = halfWidth + 8;
        int pedestalY = halfHeight - 64;


        // let's just hardcode the actual positions
        gui.blit(PEDESTAL, pedestalX, pedestalY, 0, 0, 72, 72, 72, 72, 72);
        int[][] positions = {
                {36, -62}, {57, -57}, {62, -36}, {57, -15},
                {36, -10}, {15, -15}, {10, -36}, {15, -57}
        };

        for (int i = 0; i < positions.length && i < recipe.ingredients().size(); i++) {
            Ingredient ing = recipe.ingredients().get(i);
            if (ing.isEmpty()) continue;

            ItemStack stack = ing.getItems()[0];
            int x = halfWidth + positions[i][0];
            int y = halfHeight + positions[i][1];

            // 1. Render the item
            gui.renderFakeItem(stack, x, y);

            // 2. Check if mouse is over this specific 16x16 area
            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                hoveredStack = stack;
            }
        }

        // 3. Render the tooltip last so it's on top of everything
        if (!hoveredStack.isEmpty()) {
            gui.renderTooltip(this.font, hoveredStack, mouseX, mouseY);
        }

        List<EntityType<?>> entityTypes = recipe.requiredEntities();
        int count = entityTypes.size();
        if (count == 0) return;
        int totalWidth = 72; // The total area width for entities
        int startX = halfWidth + 6;
        int startY = halfHeight + 23;

        // Calculate spacing: space per entity
        int spacing = totalWidth / count;

        for (int i = 0; i < count; i++) {
            EntityType<?> type = entityTypes.get(i);
            Entity entity = getOrCreateDisplayEntity(type);

            if (entity instanceof LivingEntity living) {
                // Calculate X: Center the entity within its allocated "slot"
                int renderX = startX + (i * spacing) + (spacing / 2);

                InventoryScreen.renderEntityInInventoryFollowsMouse(
                        gui,
                        renderX,
                        startY,
                        7, // Scale: 5 was likely too small, 10-15 is usually better for small icons
                        (float) renderX - mouseX,
                        (float) (startY - 7) - mouseY, // Offset Y to make the entity "look" at the mouse correctly
                        living
                );
            }
        }
    }

    private final Map<EntityType<?>, LivingEntity> dummyEntities = new HashMap<>();

    private LivingEntity getOrCreateDisplayEntity(EntityType<?> type) {
        return dummyEntities.computeIfAbsent(type, t -> {
            Entity e = null;
            if (this.minecraft != null) {
                if (this.minecraft.level != null) {
                    e = t.create(this.minecraft.level);
                }
            }
            return e instanceof LivingEntity l ? l : null;
        });
    }

    protected void renderBg(GuiGraphics pGuiGraphics) {
        int bgWidth = 196;
        int bgHeight = 220;
        int i = (this.width - bgWidth) / 2;
        int j = (this.height - bgHeight) / 2;
        pGuiGraphics.blit(BACKGROUND, i, j, 0, 0, bgWidth, bgHeight, bgWidth, bgHeight);
    }

    private void initBookmarks() {
        final int bookmarkStartX = this.width / 2 - 84;
        final int bookmarkY = this.height / 2 - 107;
        final int bookmarkWidth = 26;
        final int bookmarkHeight = 22;
        final int bookmarkSpacing = 2;
        final int yDiff = 22;

        TableOfWizardryBookSection[] sections = TableOfWizardryBookSection.values();
        for (int i = 0; i < sections.length; i++) {
            TableOfWizardryBookSection section = sections[i];
            int offsetX = bookmarkStartX + (bookmarkWidth * i);
            if (i > 0) offsetX += bookmarkSpacing * i;
            if (i > 2) offsetX += bookmarkSpacing;

            boolean isSelected = (this.currentSection == section);
            int currentY = isSelected ? bookmarkY : bookmarkY + 6;
            int displayHeight = isSelected ? bookmarkHeight : bookmarkHeight - 6;

            ImageButton button = new ImageButton(
                    offsetX, currentY,
                    bookmarkWidth, displayHeight,
                    0, 0,
                    yDiff,
                    section.bookmark,
                    26, 66,
                    pButton -> {
                        this.currentSection = section;
                        this.rebuildWidgets();
                    }
            ) {
                @Override
                public void playDownSound(SoundManager pHandler) {
                    pHandler.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
                }
            };


            String translationKey = "tooltip." + Brutality.MOD_ID + "." + section.name().toLowerCase();
            button.setTooltip(Tooltip.create(Component.translatable(translationKey)));

            button.active = !isSelected;
            this.addRenderableWidget(button);
        }
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (this.minecraft != null && this.minecraft.options.keyInventory.matches(pKeyCode, pScanCode)) {
            this.onClose();
            return true;
        }

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    public static class EntryList extends ObjectSelectionList<EntryList.Entry> {
        public EntryList(Minecraft pMinecraft, int pWidth, int pHeight, int pY0, int pY1, int pItemHeight) {
            super(pMinecraft, pWidth, pHeight, pY0, pY1, pItemHeight);
            this.setRenderBackground(false);
            this.setRenderTopAndBottom(false);
            this.setRenderSelection(false);
        }

        @Override
        public int getMaxScroll() {
            return Math.max(0, this.getMaxPosition() - (this.y1 - this.y0));
        }

        @Override
        protected int addEntry(Entry pEntry) {
            return super.addEntry(pEntry);
        }

        @Override
        public int getRowWidth() {
            return this.width;
        }

        @Override
        public int getRowLeft() {
            return this.getLeft();
        } // Fixes alignment

        @Override
        protected int getRowTop(int pIndex) {
            return this.y0 - (int) this.getScrollAmount() + pIndex * this.itemHeight;
        }

        @Override
        protected int getScrollbarPosition() {
            return this.getLeft() + this.width - 6;
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            this.enableScissor(pGuiGraphics);
            this.renderList(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            pGuiGraphics.disableScissor();

            renderScrollbar(pGuiGraphics);
        }

        private void renderScrollbar(GuiGraphics pGuiGraphics) {
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
                // Apply font directly to component to preserve style
                this.button = new TextWithIconImageButton(0, 0, w, h, 0, 0, h,
                        ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/button.png"),
                        w, h * 3, onPress, name.withStyle(s -> s.withFont(ModResources.TABLE_OF_WIZARDRY_FONT)));

                this.button.notHoveredOrFocusedTextColor = LIGHT_GRAY;
                this.button.hoveredOrFocusedTextColor = LIGHTER_GRAY;
                this.button.inactiveTextColor = GRAY;
                this.button.dropShadow = false;
                this.button.icon = icon;
            }

            @Override
            public void render(GuiGraphics g, int i, int top, int left, int w, int h, int mx, int my, boolean ho, float p) {
                this.button.setX(left);
                this.button.setY(top);
                this.button.render(g, mx, my, p);
            }

            @Override
            public boolean mouseClicked(double mx, double my, int b) {
                return this.button.mouseClicked(mx, my, b);
            }

            @Override
            public @NotNull Component getNarration() {
                return button.getMessage();
            }
        }
    }
}