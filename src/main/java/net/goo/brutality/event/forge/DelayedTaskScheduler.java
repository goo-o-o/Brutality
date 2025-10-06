package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DelayedTaskScheduler {
    private static final Map<Level, ConcurrentLinkedQueue<AbstractMap.SimpleEntry<Runnable, Integer>>> tasksPerLevel = new HashMap<>();

    public static void queueServerWork(Level level, int tick, Runnable action) {
        if (!level.isClientSide()) {
            tasksPerLevel.computeIfAbsent(level, k -> new ConcurrentLinkedQueue<>()).add(new AbstractMap.SimpleEntry<>(action, tick));
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (!event.level.isClientSide() && event.phase == TickEvent.Phase.END) {
            ConcurrentLinkedQueue<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = tasksPerLevel.get(event.level);
            if (workQueue != null) {
                List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
                workQueue.forEach((work) -> {
                    work.setValue(work.getValue() - 1);
                    if (work.getValue() <= 0) {
                        actions.add(work);
                    }
                });
                actions.forEach((e) -> e.getKey().run());
                workQueue.removeAll(actions);
            }
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (!event.getLevel().isClientSide()) {
            tasksPerLevel.remove((ServerLevel) event.getLevel());
        }
    }
}