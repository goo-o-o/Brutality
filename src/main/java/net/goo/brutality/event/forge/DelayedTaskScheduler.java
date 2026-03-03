package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.minecraft.client.multiplayer.ClientLevel;
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
    private static final Map<ServerLevel, ConcurrentLinkedQueue<AbstractMap.SimpleEntry<Runnable, Integer>>> tasksPerServerLevel = new HashMap<>();
    private static final Map<ClientLevel, ConcurrentLinkedQueue<AbstractMap.SimpleEntry<Runnable, Integer>>> tasksPerClientLevel = new HashMap<>();

    public static void queueCommonWork(Level level, int tick, Runnable action) {
        if (level.isClientSide()) {
            tasksPerClientLevel.computeIfAbsent((ClientLevel) level, k -> new ConcurrentLinkedQueue<>()).add(new AbstractMap.SimpleEntry<>(action, tick));
        } else {
            tasksPerServerLevel.computeIfAbsent((ServerLevel) level, k -> new ConcurrentLinkedQueue<>()).add(new AbstractMap.SimpleEntry<>(action, tick));
        }
    }

    public static void queueClientWork(Level level, int tick, Runnable action) {
        if (level.isClientSide()) {
            tasksPerClientLevel.computeIfAbsent((ClientLevel) level, k -> new ConcurrentLinkedQueue<>()).add(new AbstractMap.SimpleEntry<>(action, tick));
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!event.level.isClientSide()) {
                ConcurrentLinkedQueue<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = tasksPerServerLevel.get(((ServerLevel) event.level));
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
            } else {
                ConcurrentLinkedQueue<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = tasksPerClientLevel.get(((ClientLevel) event.level));
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
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            tasksPerClientLevel.remove((ClientLevel) event.getLevel());
        } else {
            tasksPerServerLevel.remove((ServerLevel) event.getLevel());
        }
    }
}