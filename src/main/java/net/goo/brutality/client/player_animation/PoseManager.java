package net.goo.brutality.client.player_animation;

import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class PoseManager {
    public static final class PoseDefinition {
        private final Predicate<LivingEntity> condition;
        private final KeyframeAnimation animation;
        private final int priority;
        @Nullable
        private KeyframeAnimationPlayer player;

        public PoseDefinition(
                Predicate<LivingEntity> condition,
                ResourceLocation animLoc,
                int priority
        ) {
            this.condition = condition;
            this.priority = priority;
            this.animation = PlayerAnimationRegistry.getAnimation(animLoc);
            if (animation != null) {
                this.player = new KeyframeAnimationPlayer(animation);
            }
        }

        public Predicate<LivingEntity> condition() {
            return condition;
        }

        public int priority() {
            return priority;
        }

        public KeyframeAnimation animation() {
            return animation;
        }
    }

    private static final List<PoseDefinition> POSES = new ArrayList<>();

    static {
        // Example: Registering a pose for holding a shield
        register(new PoseDefinition(
                entity -> entity.isUsingItem() && entity.getUseItem().is(BrutalityItems.MAX.get()),
            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID,"spinning_pose"), 10));

    }

    public static void register(PoseDefinition pose) {
        POSES.add(pose);
        // Keep highest priority at the top
        POSES.sort(Comparator.comparingInt(PoseDefinition::priority).reversed());
    }

    @Nullable
    public static PoseDefinition getActivePose(LivingEntity entity) {
        for (PoseDefinition pose : POSES) {
            if (pose.condition().test(entity)) {
                return pose;
            }
        }
        return null; // Default to no pose
    }
}