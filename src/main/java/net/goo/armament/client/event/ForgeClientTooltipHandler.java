package net.goo.armament.client.event;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.*;
import net.goo.armament.item.custom.unused.DivineRhittaAxeItem;
import net.goo.armament.item.custom.unused.QuantumDrillItem;
import net.goo.armament.item.custom.unused.ResonancePickaxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static net.goo.armament.util.ModResources.*;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientTooltipHandler {


    @SubscribeEvent
    public static void tooltipColorHandler(RenderTooltipEvent.Color event) {
        ItemStack stack = event.getItemStack();
        @NotNull Item item = event.getItemStack().getItem();
        // Create a map to hold the item class and their respective border colors
        Map<Class<?>, int[]> borderColorMap = new HashMap<>();

        // Populate the map with item-class to color pairs
        borderColorMap.put(DivineRhittaAxeItem.class, new int[]{
                toARGB(RHITTA_COLORS[0]),
                toARGB(RHITTA_COLORS[1])});
        borderColorMap.put(DoomfistGauntletItem.class, new int[]{
                toARGB(DOOMFIST_GAUNTLET_COLORS[0]),
                toARGB(DOOMFIST_GAUNTLET_COLORS[0])});
        borderColorMap.put(EventHorizonLanceItem.class, new int[]{
                toARGB(EVENT_HORIZON_COLORS[0]),
                toARGB(EVENT_HORIZON_COLORS[1])});
        borderColorMap.put(ExcaliburSword.class, new int[]{
                toARGB(EXCALIBUR_COLORS[0]),
                toARGB(EXCALIBUR_COLORS[1])});
        borderColorMap.put(FallenScytheItem.class, new int[]{
                toARGB(FALLEN_SCYTHE_COLORS[0]),
                toARGB(FALLEN_SCYTHE_COLORS[1])});
        borderColorMap.put(JackpotHammerItem.class, new int[]{
                toARGB(JACKPOT_COLORS[0]),
                toARGB(JACKPOT_COLORS[1])});
        borderColorMap.put(LeafBlowerItem.class, new int[]{
                toARGB(LEAF_BLOWER_COLORS[0]),
                toARGB(LEAF_BLOWER_COLORS[1])});
        borderColorMap.put(QuantumDrillItem.class, new int[]{
                toARGB(QUANTUM_DRILL_COLORS[0]),
                toARGB(QUANTUM_DRILL_COLORS[1])});
        borderColorMap.put(ResonancePickaxeItem.class, new int[]{
                toARGB(RESONANCE_PICKAXE_COLORS[0]),
                toARGB(RESONANCE_PICKAXE_COLORS[1])});
        borderColorMap.put(ShadowstepSword.class, new int[]{
                toARGB(SHADOWSTEP_COLORS[0]),
                toARGB(SHADOWSTEP_COLORS[1])});
        borderColorMap.put(SupernovaSword.class, new int[]{
                toARGB(SUPERNOVA_COLORS[0]),
                toARGB(SHADOWSTEP_COLORS[1])});
        borderColorMap.put(TerraBladeSword.class, new int[]{
                toARGB(TERRA_BLADE_COLORS[0]),
                toARGB(TERRA_BLADE_COLORS[1])});
        borderColorMap.put(TerratonHammerItem.class, new int[]{
                toARGB(TERRATON_HAMMER_COLORS[0]),
                toARGB(TERRATON_HAMMER_COLORS[1])});
        borderColorMap.put(TruthseekerSword.class, new int[]{
                toARGB(TRUTHSEEKER_COLORS[0]),
                toARGB(TRUTHSEEKER_COLORS[1])});
        borderColorMap.put(ThunderboltTrident.class, new int[]{
                toARGB(THUNDERBOLT_COLORS[0]),
                toARGB(THUNDERBOLT_COLORS[1])});

        for (Map.Entry<Class<?>, int[]> entry : borderColorMap.entrySet()) {
            if (entry.getKey().isInstance(item)) {
                int[] colors = entry.getValue();
                event.setBorderStart(colors[0]);
                event.setBorderEnd(colors[1]);
                break;
            }
        }

    }

    public static int toARGB(int[] colors, int... alpha) {
        // Ensure values are clamped between 0 and 255
        int a = 0;
        if (alpha.length == 0) a = 255;

        colors[0] = Math.max(0, Math.min(255, colors[0]));
        colors[1] = Math.max(0, Math.min(255, colors[1]));
        colors[2] = Math.max(0, Math.min(255, colors[2]));

        // Combine the components into a single ARGB integer
        return (a << 24) | (colors[0] << 16) | (colors[1] << 8) | colors[2];
    }

}
