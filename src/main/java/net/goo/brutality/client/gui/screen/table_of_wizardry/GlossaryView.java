package net.goo.brutality.client.gui.screen.table_of_wizardry;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.components.EnhancedStringWidget;
import net.goo.brutality.client.gui.components.SyncedWidgetList;
import net.goo.brutality.client.gui.screen.TableOfWizardryScreen;
import net.goo.brutality.common.block.IBrutalityMagicBlock;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Locale;

public class GlossaryView extends TableOfWizardryView {

    public GlossaryView(TableOfWizardryScreen screen) {
        super(screen);
    }


    @Override
    public void init() {
        int top = screen.height / 2 - 82;
        int listTop = top + 9;
        int leftColumnX = screen.width / 2 - 82;  // Leftmost (Names)
        int midColumnX = screen.width / 2 + 6;
        int rightColumnX = midColumnX + 34;       // Rightmost (Counts)


        int totalPoints = screen.getBlockEntity().getTotalPoints();
        screen.addRenderableWidget(new EnhancedStringWidget.Builder(76, null, Component.translatable("message." + Brutality.MOD_ID + ".table_of_wizardry.current_points", totalPoints).withStyle(style), mc.font, 0).relPos(leftColumnX, top).dropShadow(false).alignLeft().build());
        screen.addRenderableWidget(new EnhancedStringWidget.Builder(76, null, Component.translatable("message." + Brutality.MOD_ID + ".table_of_wizardry.ritual_duration", screen.getBlockEntity().getMaxProgress() * 0.05F).withStyle(style), mc.font, 0).relPos(midColumnX, top).dropShadow(false).alignLeft().build());


        int nameListWidth = 76, powerWidth = 30, countWidth = 34;
        SyncedWidgetList nameList = new SyncedWidgetList(mc, nameListWidth, 104 - 9, listTop, leftColumnX, 0, 0, 0);
        SyncedWidgetList powerList = new SyncedWidgetList(mc, powerWidth, 104 - 9, listTop, midColumnX, 0, 0, 0);
        SyncedWidgetList countList = new SyncedWidgetList(mc, countWidth + 6, 104 - 9, listTop, rightColumnX, 0, 0, 5);

        // 2. Cross-link them for synced scrolling
        nameList.setPartners(powerList, countList);
        powerList.setPartners(nameList, countList);
        countList.setPartners(nameList, powerList);


        for (int i = 0; i < IBrutalityMagicBlock.MagicBlockGroup.values().length; i++) {
            IBrutalityMagicBlock.MagicBlockGroup group = IBrutalityMagicBlock.MagicBlockGroup.values()[i];


            nameList.add(new EnhancedStringWidget.Builder(nameListWidth, null, Component.literal(group.name().toUpperCase(Locale.ROOT)).withStyle(style),
                    mc.font, 0).alignLeft().dropShadow(false).build());

            powerList.add(new EnhancedStringWidget.Builder(powerWidth, null, Component.literal("MAX:" + group.maximumBlockCount).withStyle(style), mc.font, 0)
                    .alignLeft().dropShadow(false).build());

            countList.add(new StringWidget(Component.empty(), mc.font));

            for (BlockState state : group.getBlockStates()) {
                MutableComponent name = state.getBlock().getName();
                for (Property<?> property : state.getProperties()) {
                    if (property.getValueClass().equals(Integer.class))
                        name.append("[" + state.getValue(property) + "]");
                }
                nameList.add(new EnhancedStringWidget.Builder(nameListWidth, null, name.withStyle(style), mc.font, 0).alignLeft().dropShadow(false).build());

                if (state.getBlock() instanceof IBrutalityMagicBlock magicBlock) {
                    powerList.add(new EnhancedStringWidget.Builder(powerWidth, null, Component.literal("[" + magicBlock.getMagicPower(state) + "pts.]").withStyle(style), mc.font, 0).alignLeft().dropShadow(false).build());
                }

                countList.add(new EnhancedStringWidget.Builder(countWidth, null, Component.literal("[" + screen.getBlockEntity().counts.getInt(state) + "x]").withStyle(style), mc.font, 0).alignRight().dropShadow(false).build());
            }

            if (i < IBrutalityMagicBlock.MagicBlockGroup.values().length - 1) {
                nameList.add(new StringWidget(Component.empty(), mc.font));
                powerList.add(new StringWidget(Component.empty(), mc.font));
                countList.add(new StringWidget(Component.empty(), mc.font));
            }

        }


        screen.addRenderableWidget(nameList);
        screen.addRenderableWidget(powerList);
        screen.addRenderableWidget(countList);
    }


    @Override
    public void setFocused(boolean pFocused) {

    }

    @Override
    public boolean isFocused() {
        return true;
    }

}
