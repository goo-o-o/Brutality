package net.goo.brutality.client.renderers.shaders;


import net.goo.brutality.Brutality;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Brutality.MOD_ID)
public class PostShaderManager {
    public static Matrix4f viewStackMatrix;
    public static List<PostShaderInstance> instances = new ArrayList<>();
    public static List<PostShaderInstance> removalQueue = new ArrayList<>();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        for (PostShaderInstance i : instances) {
            i.tick();
        }
        for (PostShaderInstance i : removalQueue) {
            instances.remove(i);
        }
        removalQueue.clear();


        // Tick outline shaders manually since they're not in instances
        if (BrutalityShaders.itemOutlinePostShader != null)
            BrutalityShaders.itemOutlinePostShader.tick();
        if (BrutalityShaders.maxSwordOutlinePostShader != null)
            BrutalityShaders.maxSwordOutlinePostShader.tick();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        for (PostShaderInstance i : instances) {
            if (event.getStage().equals(i.getRenderStage())) {
                i.process();
            }
        }
    }
}