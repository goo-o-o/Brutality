package net.goo.armament.client.event;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientTooltipHandler {


    @SubscribeEvent
    public static void tooltipColorHandler(RenderTooltipEvent.Color event) {
        @NotNull Item stack = event.getItemStack().getItem();
        // Create a map to hold the item class and their respective border colors
        Map<Class<?>, int[]> borderColorMap = new HashMap<>();

        // Populate the map with item-class to color pairs
        borderColorMap.put(DivineRhittaAxeItem.class, new int[]{
                toARGB(255, 255, 253, 112),
                toARGB(255, 86, 73, 191)});
        borderColorMap.put(DoomfistGauntletItem.class, new int[]{
                toARGB(255, 237, 205, 140),
                toARGB(255, 118, 118, 118)});
        borderColorMap.put(EventHorizonLanceItem.class, new int[]{
                toARGB(255, 250, 140, 20),
                toARGB(255, 50, 50, 50)});
        borderColorMap.put(JackpotHammerItem.class, new int[]{
                toARGB(255, 255, 200, 50),
                toARGB(255, 38, 234, 239)});
        borderColorMap.put(LeafBlowerItem.class, new int[]{
                toARGB(255, 212, 6, 6),
                toARGB(255, 255, 255, 255)});
        borderColorMap.put(QuantumDrillItem.class, new int[]{
                toARGB(255, 255, 255, 255),
                toARGB(255, 0, 120, 190)});
        borderColorMap.put(ResonancePickaxeItem.class, new int[]{
                toARGB(255, 255, 0, 0),
                toARGB(255, 0, 0, 255)});
        borderColorMap.put(ShadowstepSwordItem.class, new int[]{
                toARGB(255, 65, 0, 125),
                toARGB(255, 25, 25, 25)});
        borderColorMap.put(SupernovaSwordItem.class, new int[]{
                toARGB(255, 255, 255, 222),
                toARGB(255, 90, 37, 131)});
        borderColorMap.put(TerraBladeSwordItem.class, new int[]{
                toARGB(255, 174, 229, 58),
                toARGB(255, 0, 82, 60)});
        borderColorMap.put(TerratonHammerItem.class, new int[]{
                toARGB(255, 186, 198, 195),
                toARGB(255, 25, 50, 50)});
        borderColorMap.put(TruthseekerSwordItem.class, new int[]{
                toARGB(255, 128, 244, 58),
                toARGB(255, 93, 33, 0)});
        borderColorMap.put(ZeusThunderboltItem.class, new int[]{
                toARGB(255, 255, 215, 86),
                toARGB(255, 164, 92, 0)});

        // Iterate over the map to determine if the stack matches any key
        for (Map.Entry<Class<?>, int[]> entry : borderColorMap.entrySet()) {
            if (entry.getKey().isInstance(stack)) {
                int[] colors = entry.getValue();
                event.setBorderStart(colors[0]);
                event.setBorderEnd(colors[1]);
                break; // Exit the loop once a match is found
            }
        }

    }

    public static int toARGB(int a, int r, int g, int b) {
        // Ensure values are clamped between 0 and 255
        a = Math.max(0, Math.min(255, a));
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));

        // Combine the components into a single ARGB integer
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

}
