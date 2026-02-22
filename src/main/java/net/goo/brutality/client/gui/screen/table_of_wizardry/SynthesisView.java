package net.goo.brutality.client.gui.screen.table_of_wizardry;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.components.AbstractWidgetList;
import net.goo.brutality.client.gui.components.EnhancedStringWidget;
import net.goo.brutality.client.gui.screen.TableOfWizardryScreen;
import net.goo.brutality.util.ModResources;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class SynthesisView extends TableOfWizardryView {
    public SynthesisView(TableOfWizardryScreen screen) {
        super(screen);
    }

    @Override
    public void init() {
        int top = screen.height / 2 - 82;
        int leftColumnX = screen.width / 2 - 82;
        int midColumnX = screen.width / 2 + 6;

        AbstractWidgetList descriptionList = new AbstractWidgetList(mc, 76, 104, top, leftColumnX, 0, 0, 5);

        descriptionList.add(new EnhancedStringWidget.Builder(70, null,
                Component.translatable("message." + Brutality.MOD_ID + ".table_of_wizardry.synthesis.description", Component.literal("💧").withStyle(s -> s.withFont(ModResources.ICON_FONT).withColor(ChatFormatting.WHITE))).withStyle(style),
                mc.font, 0).alignCenter().wordWrap().dropShadow(false).build());

        screen.addRenderableWidget(descriptionList);

        int points = screen.getBlockEntity().getTotalPoints();
        List<SynthesisResult> chances = SynthesisResult.calculateProbabilities(points, 0.0025);

        AbstractWidgetList chanceList = new AbstractWidgetList(mc, 76, 104, top, midColumnX, 0, 0, 5);
        for (SynthesisResult res : chances) {
            String formattedChance = String.format("%.2f", res.chance * 100);

            chanceList.add(new EnhancedStringWidget.Builder(70, null,
                    Component.translatable("message." + Brutality.MOD_ID + ".table_of_wizardry.synthesis.chance", res.levelGain, formattedChance).withStyle(style), mc.font, 0).alignCenter().wordWrap().dropShadow(false).build());


        }
        screen.addRenderableWidget(chanceList);

    }


    @Override
    public void setFocused(boolean pFocused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }


    public static class SynthesisResult {
        public final int levelGain;
        public final double chance;

        public SynthesisResult(int levelGain, double chance) {
            this.levelGain = levelGain;
            this.chance = chance;
        }

        public static int getLevelBonus(double points, double threshold) {
            // 1. Calculate the 'Stay' probability (P)
            double p = points / (points + 100.0);

            if (p <= 0) return 0;

            // 2. Calculate the "Theoretical Max Level"
            // We use (1-P) because that is the probability of stopping at Level 0.
            double failChance = 1.0 - p;
            int maxPossible = (int) (Math.log(threshold / failChance) / Math.log(p));

            double roll = Math.random();

            // 4. Solve for n: P^n = roll -> n = ln(roll) / ln(P)
            // We use Math.max(0, ...) to ensure we don't get negative levels.
            int result = (int) Math.floor(Math.log(roll) / Math.log(p));

            // 5. Clamp to the threshold limit to match your UI's 100% normalization
            return Math.min(result, maxPossible);
        }

        public static List<SynthesisResult> calculateProbabilities(double points, double threshold) {
            List<SynthesisResult> rawOutcomes = new ArrayList<>();

            // P is the chance to "ascend" to the next level.
            double p = points / (points + 100.0);
            double failChance = 1.0 - p;

            double totalWeightSeen = 0;
            int levelGain = 0;

            // Step 1: Gather all outcomes above the threshold
            while (true) {
                double rawChance = Math.pow(p, levelGain) * failChance;

                if (rawChance < threshold) break;

                rawOutcomes.add(new SynthesisResult(levelGain, rawChance));
                totalWeightSeen += rawChance;
                levelGain++;

                if (levelGain > 100) break;
            }

            // Step 2: Normalize so the list sums to 1.0 (100%)
            List<SynthesisResult> normalizedOutcomes = new ArrayList<>();
            for (SynthesisResult raw : rawOutcomes) {
                // By dividing by totalWeightSeen, we redistribute the
                // "missing" infinitesimal % back into the visible levels.
                normalizedOutcomes.add(new SynthesisResult(raw.levelGain, raw.chance / totalWeightSeen));
            }

            return normalizedOutcomes;
        }
    }
}
