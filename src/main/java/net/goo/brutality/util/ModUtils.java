package net.goo.brutality.util;

import com.lowdragmc.photon.client.fx.EntityEffect;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXRuntime;
import net.goo.brutality.config.BrutalityCommonConfig;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.*;
import net.goo.brutality.particle.providers.WaveParticleData;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.phys.CylindricalBoundingBox;
import net.mcreator.terramity.init.TerramityModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ModUtils {
    protected static final RandomSource random = RandomSource.create();

    private static final boolean HAS_BETTER_COMBAT = ModList.get().isLoaded("bettercombat");
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static MethodHandle getComboCount;
    private static MethodHandle getCurrentAttack;
    private static MethodHandle shouldAttackWithOffHand;
    private static MethodHandle getItemStack;

    static {
        if (HAS_BETTER_COMBAT) {
            try {
                // PlayerAttackProperties.getComboCount()
                Class<?> propsClass = Class.forName("net.bettercombat.logic.PlayerAttackProperties");
                Class<?> helperClass = Class.forName("net.bettercombat.logic.PlayerAttackHelper");
                Class<?> handClass = Class.forName("net.bettercombat.api.AttackHand");
                MethodHandles.Lookup lookup = MethodHandles.lookup();

                getComboCount = lookup.findVirtual(propsClass, "getComboCount", MethodType.methodType(int.class));
                getCurrentAttack = lookup.findStatic(helperClass, "getCurrentAttack", MethodType.methodType(handClass, Player.class, int.class));

                shouldAttackWithOffHand = lookup.findStatic(helperClass, "shouldAttackWithOffHand", MethodType.methodType(boolean.class, Player.class, int.class));
                getItemStack = lookup.findVirtual(handClass, "itemStack", MethodType.methodType(ItemStack.class));


            } catch (Throwable t) {
                getComboCount = null;
            }
        }
    }

    public static ItemStack getAttackStack(Player player) {
        if (!HAS_BETTER_COMBAT || getComboCount == null) {
            return player.getMainHandItem();
        }

        try {
            // 1. Cast player to PlayerAttackProperties via unreflect + bind
            int combo = (int) getComboCount.invoke((Object) player);

            // 2. Get AttackHand
            Object hand = getCurrentAttack.invoke(player, combo);
            if (hand != null) {
                return (ItemStack) getItemStack.invoke(hand);
            }

            // 3. Fallback: off-hand check
            boolean offHand = (boolean) shouldAttackWithOffHand.invoke(player, combo);
            return offHand ? player.getOffhandItem() : player.getMainHandItem();

        } catch (Throwable t) {
            return player.getMainHandItem(); // safe fallback
        }
    }



    public static boolean doubleDownRestricted(Container c) {
        return
                c instanceof CraftingContainer || c instanceof PlayerEnderChestContainer
                        || c instanceof BaseContainerBlockEntity;
    }

    public static float getAttackDamage(@Nullable Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            return (float) livingEntity.getAttributeValue(Attributes.ATTACK_DAMAGE);
        }
        return 1;
    }

    public static Level.ExplosionInteraction getThrowingWeaponExplosionInteractionFromConfig() {
        return BrutalityCommonConfig.THROWING_WEAPONS_BREAK_BLOCKS.get() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE;
    }

    public static Explosion.BlockInteraction getThrowingWeaponBlockInteractionFromConfig() {
        return BrutalityCommonConfig.THROWING_WEAPONS_BREAK_BLOCKS.get() ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.KEEP;
    }

    public static void handleActiveAbilityWithCd(ICuriosItemHandler handler, Item item, int cooldownTicks, Runnable runnable) {
        if (handler.isEquipped(item)) {
            if (handler.getWearer() instanceof Player wearer) {
                if (!wearer.getCooldowns().isOnCooldown(item)) {
                    runnable.run();
                    wearer.getCooldowns().addCooldown(item, cooldownTicks);
                }
            }
        }
    }

    public static void handleActiveAbility(ICuriosItemHandler handler, Item item, Runnable runnable) {
        if (handler.isEquipped(item)) {
            runnable.run();
        }
    }

    public static Random getSyncedSeededRandom(Entity player) {
        long seed = player.level().getGameTime(); // Shared game time
        seed = seed * 31 + player.getUUID().hashCode(); // Unique per player
        seed = seed * 31 + (long) (player.getX() * 1000); // Player X position (scaled)
        seed = seed * 31 + (long) (player.getY() * 1000); // Player Y position (scaled)
        seed = seed * 31 + (long) (player.getZ() * 1000); // Player Z position (scaled)
        seed = seed * 31 + player.tickCount;
        return new Random(seed);
    }

    public static double computeAttributes(@Nullable Player player, ItemStack stack, double originalDamage) {
        if (player == null) return originalDamage;

        Map<Predicate<ItemStack>, List<Attribute>> attributeMap = Map.of(
                s -> s.getItem() instanceof SwordItem || s.is(ItemTags.SWORDS), // Filter
                List.of(ModAttributes.SWORD_DAMAGE.get(), ModAttributes.SLASH_DAMAGE.get()), // Attributes that affect it

                s -> s.getItem() instanceof AxeItem || s.is(ItemTags.AXES), // Filter
                List.of(ModAttributes.AXE_DAMAGE.get(), ModAttributes.SLASH_DAMAGE.get()), // Attributes that affect it

                s -> s.getItem() instanceof BrutalityHammerItem || s.is(TerramityModItems.HELLROK_GIGATON_HAMMER.get()), // Filter
                List.of(ModAttributes.HAMMER_DAMAGE.get(), ModAttributes.BLUNT_DAMAGE.get()), // Attributes that affect it

                s -> s.getItem() instanceof BrutalityScytheItem, // Filter
                List.of(ModAttributes.SCYTHE_DAMAGE.get(), ModAttributes.SLASH_DAMAGE.get()),  // Attributes that affect it

                s -> s.getItem() instanceof BrutalitySpearItem || s.getItem() instanceof BrutalityTridentItem || s.getItem() instanceof TridentItem, // Filter
                List.of(ModAttributes.SPEAR_DAMAGE.get(), ModAttributes.PIERCING_DAMAGE.get()),  // Attributes that affect it

                s -> !s.is(TerramityModItems.HELLROK_GIGATON_HAMMER.get()) && (s.getItem() instanceof PickaxeItem || s.is(ItemTags.PICKAXES) || s.getItem() instanceof HoeItem || s.is(ItemTags.HOES)), // Filter
                List.of(ModAttributes.PIERCING_DAMAGE.get()), // Attributes that affect it

                s -> s.getItem() instanceof ShovelItem || s.is(ItemTags.SHOVELS), // Filter
                List.of(ModAttributes.BLUNT_DAMAGE.get())  // Attributes that affect it
        );


        for (var entry : attributeMap.entrySet()) {
            if (entry.getKey().test(stack)) {
                for (Attribute attribute : entry.getValue()) {
                    AttributeInstance attributeInstance = player.getAttribute(attribute);
                    if (attributeInstance != null) {
                        originalDamage = calculateValue(attributeInstance, originalDamage);
                    }
                }
            }
        }
        return originalDamage;
    }

    private static double calculateValue(AttributeInstance attributeInstance, double baseValue) {

        for (AttributeModifier additionMultiplier : attributeInstance.getModifiersOrEmpty(AttributeModifier.Operation.ADDITION)) {
            baseValue += additionMultiplier.getAmount();
        }

        double finalValue = baseValue;

        for (AttributeModifier multiplyBaseModifier : attributeInstance.getModifiersOrEmpty(AttributeModifier.Operation.MULTIPLY_BASE)) {
            finalValue += baseValue * multiplyBaseModifier.getAmount();
        }

        for (AttributeModifier multiplyTotalModifier : attributeInstance.getModifiersOrEmpty(AttributeModifier.Operation.MULTIPLY_TOTAL)) {
            finalValue *= 1.0D + multiplyTotalModifier.getAmount();
        }

        return attributeInstance.getAttribute().sanitizeValue(finalValue);
    }


    private static BrutalityCategories.AttackType getAttackType(Item item, ItemStack stack) {
        if (item == null || stack == null)
            return BrutalityCategories.AttackType.NONE;

        if (item instanceof BrutalityGeoItem geoItem) {
            return geoItem.getAttackType();
        }
        if (item instanceof SwordItem || stack.is(ItemTags.SWORDS)) {
            return BrutalityCategories.AttackType.SLASH;
        }
        if (item instanceof AxeItem || stack.is(ItemTags.AXES)) {
            return BrutalityCategories.AttackType.SLASH;
        }
        return BrutalityCategories.AttackType.NONE;
    }


    public static int calculateFallDamage(LivingEntity living, float pFallDistance, float pDamageMultiplier) {
        if (living.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
            return 0;
        } else {
            MobEffectInstance mobeffectinstance = living.getEffect(MobEffects.JUMP);
            float f = mobeffectinstance == null ? 0.0F : (float) (mobeffectinstance.getAmplifier() + 1);
            return Mth.ceil((pFallDistance - 3.0F - f) * pDamageMultiplier);
        }
    }

    public static void removeFX(Entity entity, FX fx) {
        List<EntityEffect> effects = EntityEffect.CACHE.get(entity);
        if (effects == null) return;

        boolean hasEffect = effects.stream().anyMatch(effect -> effect.getFx().equals(fx));
        if (!hasEffect) return;

        Iterator<EntityEffect> iter = effects.iterator();
        while (iter.hasNext()) {
            EntityEffect nextEffect = iter.next();
            FX nextFX = nextEffect.getFx();

            if (nextFX != null && nextFX.equals(fx)) {
                iter.remove();
                FXRuntime runtime = nextEffect.getRuntime();
                if (runtime != null && runtime.isAlive()) {
                    runtime.destroy(false);
                }
            }
        }

        if (effects.isEmpty()) {
            EntityEffect.CACHE.remove(entity);
        }
    }


    public static List<? extends Entity> getEntitiesInSphere(Entity origin, @Nullable Predicate<? super Entity> predicate, double radius) {
        List<? extends Entity> aabbEntities = predicate == null ?
                origin.level().getEntities(origin, origin.getBoundingBox().inflate(radius)) :
                origin.level().getEntities(origin, origin.getBoundingBox().inflate(radius), predicate);
        return aabbEntities.stream().filter(entity -> entity.distanceTo(origin) < radius).toList();
    }

    public static <T extends Entity> List<T> getEntitiesInSphere(Class<T> clazz, Entity origin, Predicate<? super Entity> predicate, double radius) {
        List<T> aabbEntities = predicate == null ?
                origin.level().getEntitiesOfClass(clazz, origin.getBoundingBox().inflate(radius)) :
                origin.level().getEntitiesOfClass(clazz, origin.getBoundingBox().inflate(radius), predicate);
        return aabbEntities.stream().filter(entity -> entity.distanceTo(origin) < radius && entity != origin).toList();
    }

    public static <T extends Entity> List<T> getEntitiesInCylinder(Class<T> clazz, Entity origin, Predicate<? super Entity> predicate, double radius, double height) {
        AABB aabb = new AABB(
                origin.getX() - radius,
                origin.getY(),
                origin.getZ() - radius,
                origin.getX() + radius,
                origin.getY() + height,
                origin.getZ() + radius
        );

        List<T> aabbEntities = predicate == null ?
                origin.level().getEntitiesOfClass(clazz, aabb) :
                origin.level().getEntitiesOfClass(clazz, aabb, predicate);
        return aabbEntities.stream().filter(entity -> horizontalDistanceTo(entity, origin) < radius && entity != origin).toList();
    }

    public static float horizontalDistanceTo(Entity first, Entity second) {
        float x = (float) (first.getX() - second.getX());
        float z = (float) (first.getZ() - second.getZ());
        return Mth.sqrt(x * x + z * z);
    }

    public static <T extends ParticleOptions> int sendParticles(ServerLevel serverLevel, T pType, boolean longDistance, double pPosX, double pPosY, double pPosZ, int pParticleCount, double pXOffset, double pYOffset, double pZOffset, double pSpeed) {
        ClientboundLevelParticlesPacket clientboundlevelparticlespacket = new ClientboundLevelParticlesPacket(
                pType, longDistance, pPosX, pPosY, pPosZ, (float) pXOffset, (float) pYOffset, (float) pZOffset, (float) pSpeed, pParticleCount
        );
        int i = 0;
        for (ServerPlayer serverPlayer : serverLevel.players()) {
            if (sendParticles(serverLevel, serverPlayer, longDistance, pPosX, pPosY, pPosZ, clientboundlevelparticlespacket)) {
                ++i;
            }
        }
        return i;
    }

    public static <T extends ParticleOptions> int sendParticles(ServerLevel serverLevel, T pType,
                                                                boolean longDistance, Entity toSpawnOn, double pXOffset,
                                                                double pYOffset, double pZOffset, int pParticleCount, double pSpeed) {
        return sendParticles(serverLevel, pType, longDistance, toSpawnOn.getX(), toSpawnOn.getY(0.5), toSpawnOn.getZ(), pParticleCount, pXOffset, pYOffset, pZOffset, pSpeed);
    }

    public static <T extends ParticleOptions> int sendParticles(ServerLevel serverLevel, T pType,
                                                                boolean longDistance, Vec3 spawnPos,
                                                                double pXOffset, double pYOffset, double pZOffset, int pParticleCount, double pSpeed) {
        return sendParticles(serverLevel, pType, longDistance, spawnPos.x, spawnPos.y, spawnPos.z, pParticleCount, pXOffset, pYOffset, pZOffset, pSpeed);
    }

    public static <T extends ParticleOptions> int sendParticles(ServerLevel serverLevel, T pType,
                                                                boolean longDistance, double pPosX, double pPosY, double pPosZ, int pParticleCount, double pSpeed) {
        return sendParticles(serverLevel, pType, longDistance, pPosX, pPosY, pPosZ, pParticleCount, 0, 0, 0, pSpeed);
    }

    private static boolean sendParticles(ServerLevel serverLevel, ServerPlayer pPlayer, boolean pLongDistance, double pPosX, double pPosY, double pPosZ, Packet<?> pPacket) {
        if (pPlayer.level() != serverLevel) {
            return false;
        } else {
            BlockPos blockpos = pPlayer.blockPosition();
            if (blockpos.closerToCenterThan(new Vec3(pPosX, pPosY, pPosZ), pLongDistance ? 512.0D : 32.0D)) {
                pPlayer.connection.send(pPacket);
                return true;
            } else {
                return false;
            }
        }
    }


    public record RayData<T extends Entity>(List<T> entityList, int distance, Vec3 endPos) {
        public RayData(List<T> entityList, int distance, Vec3 endPos) {
            this.entityList = Collections.unmodifiableList(entityList);
            this.distance = distance;
            this.endPos = endPos;
        }
    }

    public static class ModEasings {
        public static float easeQuadOut(float input) {
            return input * (2.0F - input);
        }

        public static float easeQuadIn(float input) {
            return input * input;
        }

        public static float easeOut(float t) {
            return 1 - (1 - t) * (1 - t);
        }

    }

    public static <T extends Entity> List<T> applyWaveEffect(ServerLevel level, Vec3 origin, Class<T> clazz, WaveParticleData<?> particleData, @Nullable Predicate<? super T> filter, Consumer<Entity> effect) {
        return applyWaveEffect(level, origin.x(), origin.y(), origin.z(), clazz, particleData, filter, effect);
    }

    public static <T extends Entity> List<T> applyWaveEffect(ServerLevel level, Entity origin, Class<T> clazz, WaveParticleData<?> particleData, @Nullable Predicate<? super T> filter, Consumer<Entity> effect) {
        return applyWaveEffect(level, origin.getX(), origin.getY(0.5), origin.getZ(), clazz, particleData, filter, effect);
    }

    public static <T extends Entity> List<T> applyWaveEffect(ServerLevel level, double x, double y, double z, Class<T> clazz, WaveParticleData<?> particleData, @Nullable Predicate<? super T> filter, Consumer<Entity> effect) {
        float maxRadius = particleData.radius();
        int lifetime = particleData.growthDuration();
        Set<T> affectedEntities = new HashSet<>();
        int ticksPerCheck = 1;
        Vec3 center = new Vec3(x, y, z);

        for (int age = 0; age <= lifetime; age += ticksPerCheck) {

            float growthProgress = ((float) age) / lifetime;
            growthProgress = Mth.clamp(growthProgress, 0.0F, 1.0F);
            final float currentRadius = maxRadius * ModEasings.easeOut(growthProgress);
            float previousGrowthProgress = (float) (age - 1) / lifetime;
            previousGrowthProgress = Mth.clamp(previousGrowthProgress, 0.0F, 1.0F);
            final float previousRadius = maxRadius * ModEasings.easeOut(previousGrowthProgress);

            DelayedTaskScheduler.queueServerWork(level, age, () -> {
                CylindricalBoundingBox circle = new CylindricalBoundingBox(center, currentRadius, previousRadius);

                List<T> entities = level.getEntitiesOfClass(clazz, circle, entity ->
                        (filter == null || filter.test(entity)) && circle.contains(entity.position()));

                for (T entity : entities) {
                    if (affectedEntities.add(entity) && entity.isAlive()) {
                        effect.accept(entity);
                    }
                }
            });

        }

        return new ArrayList<>(affectedEntities);
    }


    public static <T extends Entity> RayData<T> getEntitiesInRay(Class<T> entityClass, LivingEntity origin,
                                                                 float maxRange, ClipContext.Fluid fluidContext, ClipContext.Block blockContext,
                                                                 float beamRadius, @Nullable Predicate<? super T> filter, @NotNull Integer pierceCap,
                                                                 @Nullable Consumer<Vec3> segmentConsumer
    ) {

        Vec3 startPos = origin.getEyePosition();
        Vec3 endPos = startPos.add(origin.getLookAngle().scale(maxRange));
        Level level = origin.level();

        BlockHitResult blockResult = level.clip(new ClipContext(
                startPos,
                endPos,
                blockContext,
                fluidContext,
                origin
        ));

        int distance = (int) startPos.distanceTo(blockResult.getBlockPos().getCenter());
        List<T> targets = collectEntitiesAlongRay(entityClass, origin, startPos, distance, beamRadius, filter, pierceCap, segmentConsumer);

        return new RayData<>(targets.stream().distinct().collect(Collectors.toList()), distance, blockResult.getLocation());
    }

    private static <T extends Entity> List<T> collectEntitiesAlongRay(Class<T> entityClass, LivingEntity livingEntity,
                                                                      Vec3 startPos, int distance, float beamRadius, @Nullable Predicate<? super T> filter, Integer pierceCap, @Nullable Consumer<Vec3> segmentConsumer) {

        List<T> targets = new ArrayList<>();
        Vec3 lookAngle = livingEntity.getLookAngle();
        Level level = livingEntity.level();

        if (filter != null) {
            for (float dist = 0; dist < distance; dist += beamRadius * 2) {
                Vec3 segmentCenter = startPos.add(lookAngle.scale(dist));
                AABB segmentBox = new AABB(segmentCenter, segmentCenter).inflate(beamRadius);


                if (segmentConsumer != null) {
                    segmentConsumer.accept(segmentCenter);
                }

                targets.addAll(level.getEntitiesOfClass(entityClass, segmentBox, filter));

                if (targets.size() > pierceCap) break;
            }

        } else {
            for (float dist = 0; dist < distance; dist += beamRadius * 2) {
                Vec3 segmentCenter = startPos.add(lookAngle.scale(dist));
                AABB segmentBox = new AABB(segmentCenter, segmentCenter).inflate(beamRadius);

                targets.addAll(level.getEntitiesOfClass(entityClass, segmentBox));
                if (targets.size() > pierceCap) break;
            }
        }
        return targets;
    }


    public static SimpleParticleType getRandomParticle(List<RegistryObject<SimpleParticleType>> particleType) {
        return particleType.get(random.nextInt(particleType.size())).get();
    }

    public static SoundEvent getRandomSound(List<RegistryObject<SoundEvent>> soundEvent) {
        return soundEvent.get(random.nextInt(soundEvent.size())).get();
    }

    public static SoundEvent getRandomSound(SoundEvent... soundEvent) {
        return soundEvent[random.nextInt(soundEvent.length)];
    }


    public static boolean isPlayerBehind(Player player, Entity entity, int range) {
        Vec3 playerPos = player.getPosition(1.0F);
        Vec3 entityPos = entity.getPosition(1.0F);

        Vec3 targetVec = entityPos.subtract(playerPos);
        float targetDeg = (float) Math.toDegrees(Math.atan2(targetVec.x, targetVec.z)) + 180;

        return targetDeg > (90 - range) && targetDeg < (270 + range);
    }

    public static boolean hasFullArmorSet(LivingEntity livingEntity, ArmorMaterial material) {
        for (ItemStack stack : livingEntity.getArmorSlots()) {
            if (!(stack.getItem() instanceof ArmorItem armorItem)) {
                return false;
            }
            if (armorItem.getMaterial() != material) {
                return false;
            }
        }
        return true;
    }

    public static void setTextureIdx(ItemStack stack, int data) {
        stack.getOrCreateTag().putInt("texture", data);
    }

    public static void removeTextureIdx(ItemStack stack) {
        stack.getOrCreateTag().remove("texture");
    }

    public static int getTextureIdx(ItemStack stack) {
        return stack.getOrCreateTag().getInt("texture");
    }

    public record ModValue(Integer value, boolean overwrite) {
    }

    /**
     * @param livingEntity The {@link LivingEntity} to add effects to
     * @param mobEffect    The {@link MobEffect} to add
     * @param durationMod  A small {@link ModValue} record to pass the duration modifier as well as whether it should overwrite the original instance, the duration will be incremented if overwrite = false
     * @param amplifierMod A small {@link ModValue} record to pass the amplifier modifier as well as whether it should overwrite the original instance, the amplifier will be incremented if overwrite = false
     * @param ifAbsent     The code to run on the {@link LivingEntity} if the effect is absent
     * @param limit        The maximum amplifier of the {@link MobEffect}, inclusive
     * @param ifLimit      The code to run on the {@link LivingEntity} if the limit is reached
     */
    public static void modifyEffect(LivingEntity livingEntity, MobEffect mobEffect, @Nullable ModValue durationMod, @Nullable ModValue amplifierMod, Integer limit, @Nullable Consumer<LivingEntity> ifAbsent, @Nullable Consumer<LivingEntity> ifLimit) {
        if (livingEntity.hasEffect(mobEffect)) {
            MobEffectInstance original = livingEntity.getEffect(mobEffect);
            if (original != null) {
                if (limit != null && ifLimit != null) {
                    if (original.getAmplifier() >= limit) {
                        ifLimit.accept(livingEntity);
                    }
                    return;
                }

                int newDuration = original.getDuration();
                int newAmplifier = original.getAmplifier();

                if (durationMod != null)
                    if (durationMod.overwrite()) {
                        newDuration = durationMod.value;
                    } else {
                        newDuration += durationMod.value;
                    }

                if (amplifierMod != null)
                    if (amplifierMod.overwrite()) {
                        newAmplifier = amplifierMod.value;
                    } else {
                        newAmplifier += amplifierMod.value;
                    }

                livingEntity.addEffect(new MobEffectInstance(mobEffect, newDuration, newAmplifier, original.isAmbient(), original.isVisible(), original.showIcon()));
            }
        } else if (ifAbsent != null) {
            ifAbsent.accept(livingEntity);
        }
    }


    public static BlockPos getBlockLookingAt(Player player, boolean isFluid, float hitDistance) {
        HitResult block = player.pick(hitDistance, 1.0F, isFluid);

        if (block.getType() == HitResult.Type.BLOCK) {
            return ((BlockHitResult) block).getBlockPos();
        }
        return null;
    }

    public static Vec3 getRandomPosAroundPlayer(Player player, float scale) {
        double randomX = player.getRandomX(Mth.nextFloat(player.getRandom(), -scale, scale));
        double randomY = player.getRandomY() * Mth.nextFloat(player.getRandom(), -scale, scale);
        double randomZ = player.getRandomZ(Mth.nextFloat(player.getRandom(), -scale, scale));

        return new Vec3(randomX, randomY, randomZ);
    }

    public static boolean isStandable(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        VoxelShape shape = state.getCollisionShape(level, pos);
        return !shape.isEmpty() && shape.max(Direction.Axis.Y) >= 0.75;
    }

    public static Entity getEntityPlayerLookingAt(Player pPlayer, double range) {
        // Get player's eye position
        Vec3 playerPos = pPlayer.getEyePosition(1.0F);
        Vec3 viewVector = pPlayer.getViewVector(1.0F).normalize();

        // Get the list of entities within the specified range
        List<Entity> entities = pPlayer.level().getEntitiesOfClass(Entity.class, new AABB(playerPos, playerPos).inflate(range));
        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE; // Start with a very large distance

        for (Entity entity : entities) {
            // Calculate direction to the entity
            Vec3 entityPos = new Vec3(entity.getX(), entity.getY(0.5), entity.getZ());
            Vec3 directionToEntity = entityPos.subtract(playerPos);
            double distanceToEntity = directionToEntity.length();

            Vec3 endPos = playerPos.add(viewVector.scale(range));

            if (distanceToEntity > 0 && distanceToEntity <= range) {
                directionToEntity = directionToEntity.normalize();
                double dotProduct = viewVector.dot(directionToEntity);

                // Check if the angle is within range
                if (dotProduct > 1.0D - 0.075D / distanceToEntity) { // Adjust threshold as needed
                    // Check line of sight with raycasting
                    ClipContext context = new ClipContext(playerPos, entityPos, ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, pPlayer);
                    BlockHitResult blockHit = pPlayer.level().clip(context);

                    if (blockHit.getType() == BlockHitResult.Type.MISS) {
                        // Check hitbox intersection
                        Optional<Vec3> clipPoint = entity.getBoundingBox().clip(playerPos, endPos);
                        if (clipPoint.isPresent()) {
                            double distance = playerPos.distanceTo(clipPoint.get());
                            if (distance < closestDistance) {
                                closestEntity = entity;
                                closestDistance = distance;
                            }
                        }
                    }

                }
            }
        }

        return closestEntity; // Return the closest entity or null if none found
    }

}
