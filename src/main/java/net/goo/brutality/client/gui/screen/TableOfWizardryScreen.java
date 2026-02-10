package net.goo.brutality.client.gui.screen;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.screen.table_of_wizardry.ConjureView;
import net.goo.brutality.client.gui.screen.table_of_wizardry.SpellPageView;
import net.goo.brutality.client.gui.screen.table_of_wizardry.TableOfWizardryBookSection;
import net.goo.brutality.client.gui.screen.table_of_wizardry.TableOfWizardryView;
import net.goo.brutality.common.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.serverbound.ServerboundTableOfWizardryUpdatePacket;
import net.goo.brutality.common.registry.BrutalitySpells;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TableOfWizardryScreen extends Screen {
    public static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/bg.png");
    public static final ResourceLocation PEDESTALS = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/pedestals.png");
    public static final ResourceLocation PEDESTALS_SHADE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/pedestals_shade.png");
    public static final ResourceLocation PEDESTALS_OUTLINE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/pedestals_outline.png");
    public static final ResourceLocation BUTTON_NINE_SLICED = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/button_nine_sliced.png");
    public static final ResourceLocation BUTTON_NINE_SLICED_DARK = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/button_nine_sliced_dark.png");
    public static final ResourceLocation BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/screens/table_of_wizardry/button.png");


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
    private TableOfWizardryView currentView;
    private final Map<EntityType<?>, LivingEntity> dummyEntities = new HashMap<>();

    public float schoolListScroll, spellListScroll;

    public TableOfWizardryScreen(Component pTitle, TableOfWizardryBlockEntity block) {
        super(pTitle);
        this.block = block;
    }

    public TableOfWizardryBlockEntity getBlockEntity() {
        return block;
    }

    @Override
    public void rebuildWidgets() {


        super.rebuildWidgets();
    }

    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> @NotNull T addRenderableWidget(T pWidget) {
        return super.addRenderableWidget(pWidget);
    }

    @Override
    protected void init() {
        initBookmarks();

        // Dynamic View Switching
        if (block.currentState == TableOfWizardryBlockEntity.GuiState.SECTION_VIEW) {
            if (block.currentSection == TableOfWizardryBookSection.CONJURE) {
                currentView = new ConjureView(this);
            }
            // else if (block.currentSection == TableOfWizardryBookSection.SYNTHESIS) currentView = new SynthesisView(this);
        } else if (block.currentState == TableOfWizardryBlockEntity.GuiState.SPELL_PAGE) {
             currentView = new SpellPageView(this);
        }

        if (currentView != null) currentView.init();
    }

    @Override
    public void render(GuiGraphics gui, int mx, int my, float partial) {
        this.renderBackground(gui);
        gui.blit(BACKGROUND, (this.width - BG_WIDTH) / 2, (this.height - BG_HEIGHT) / 2, 0, 0, BG_WIDTH, BG_HEIGHT, BG_WIDTH, BG_HEIGHT);

        super.render(gui, mx, my, partial); // Renders bookmarks and widgets (ModularEntryList)

        if (currentView != null) {
            currentView.render(gui, mx, my, partial);
        }
    }



    public void showSection(TableOfWizardryBookSection section) {
        this.block.currentSection = section;
        this.block.currentState = TableOfWizardryBlockEntity.GuiState.SECTION_VIEW;
        updateServerAndRefresh();
    }

    public void updateServerAndRefresh() {
        ResourceLocation id = block.currentSpell != null ? BrutalitySpells.getIdFromSpell(block.currentSpell) : null;
        PacketHandler.sendToServer(new ServerboundTableOfWizardryUpdatePacket(block.getBlockPos(), block.currentSection.name(), block.currentState.name(), id));
        this.rebuildWidgets();
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
}
