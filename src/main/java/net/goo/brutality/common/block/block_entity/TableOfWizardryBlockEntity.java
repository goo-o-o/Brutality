package net.goo.brutality.common.block.block_entity;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.goo.brutality.client.gui.screen.table_of_wizardry.SynthesisView;
import net.goo.brutality.client.gui.screen.table_of_wizardry.TableOfWizardryBookSection;
import net.goo.brutality.common.block.IBrutalityMagicBlock;
import net.goo.brutality.common.item.generic.BrutalityAugmentItem;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.common.recipe.ConjureRecipe;
import net.goo.brutality.common.registry.BrutalityBlockEntities;
import net.goo.brutality.common.registry.BrutalityRecipes;
import net.goo.brutality.common.registry.BrutalitySpells;
import net.goo.brutality.util.ParticleHelper;
import net.goo.brutality.util.magic.AugmentHelper;
import net.goo.brutality.util.magic.ManaHelper;
import net.goo.brutality.util.magic.SpellStorage;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TableOfWizardryBlockEntity extends BlockEntity implements Nameable {
    private static final RandomSource RANDOM = RandomSource.create();
    private static final String TAG_CUSTOM_NAME = "CustomName";
    private static final String TAG_SECTION = "Section";
    private static final String TAG_STATE = "State";
    private static final String TAG_SCHOOL = "School";
    private static final String TAG_SPELL = "Spell";
    private static final String TAG_CRAFTING_ITEM = "CraftingItem";

    public int time;
    public float flip, oFlip, flipT, flipA;
    public float open, oOpen;
    public float rot, oRot, tRot;

    private Component name;
    public boolean isCrafting;
    public int prevProgress;
    public int progress;
    public int maxProgress;

    public TableOfWizardryBookSection currentSection = TableOfWizardryBookSection.GLOSSARY;
    public IBrutalitySpell.MagicSchool currentSchool;
    public BrutalitySpell currentSpell;
    public GuiState currentState = GuiState.SECTION_VIEW;

    public Object2IntOpenHashMap<BlockState> counts = new Object2IntOpenHashMap<>();

    public static final List<Vec3> PEDESTAL_OFFSETS = List.of(
            new Vec3(0, 0, -3), new Vec3(2, 0, -2), new Vec3(3, 0, 0), new Vec3(2, 0, 2),
            new Vec3(0, 0, 3), new Vec3(-2, 0, 2), new Vec3(-3, 0, 0), new Vec3(-2, 0, -2)
    );
    private Player craftingPlayer;
    public Vec3 craftingPlayerStartPos;
    public ItemStack craftingItem;

    public TableOfWizardryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BrutalityBlockEntities.TABLE_OF_WIZARDRY_BLOCK_ENTITY.get(), pPos, pBlockState);
        counts.defaultReturnValue(0);
    }

    public int getMaxProgress() {
        return Math.max(200 - getTotalPoints(), 100);
    }

    public int getTotalPoints() {
        int sum = 0;
        for (IBrutalityMagicBlock.MagicBlockGroup group : IBrutalityMagicBlock.MagicBlockGroup.values()) {
            int blocksInGroupUsed = 0;

            List<BlockState> sortedGroupBlocks = Arrays.stream(group.getBlockStates())
                    .filter(state -> counts.containsKey(state))
                    .sorted(Comparator.comparingInt((BlockState s) -> (((IBrutalityMagicBlock) s.getBlock()).getMagicPower(s)))
                            .reversed()).toList();

            for (BlockState state : sortedGroupBlocks) {
                if (blocksInGroupUsed >= group.maximumBlockCount) break;
                int countInWorld = counts.getInt(state);
                int power = ((IBrutalityMagicBlock) state.getBlock()).getMagicPower(state);

                // Determine how many of this specific block we can actually count
                int remainingCap = group.maximumBlockCount - blocksInGroupUsed;
                int amountToCount = Math.min(countInWorld, remainingCap);

                sum += amountToCount * power;
                blocksInGroupUsed += amountToCount;
            }
        }

        return sum;
    }

    public void updateNearbyMagicBlockCount() {
        if (level == null) return;
        int radius = 6;
        counts.clear();

        BlockPos center = this.getBlockPos();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    mutable.set(center.getX() + x, center.getY() + y, center.getZ() + z);

                    // Use getBlockState once
                    BlockState state = level.getBlockState(mutable);
                    if (state.getBlock() instanceof IBrutalityMagicBlock) {
                        counts.addTo(state, 1);
                    }
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (this.hasCustomName()) {
            pTag.putString(TAG_CUSTOM_NAME, Component.Serializer.toJson(this.name));
        }
        if (this.currentSection != null) pTag.putString(TAG_SECTION, this.currentSection.name());
        if (this.currentState != null) pTag.putString(TAG_STATE, this.currentState.name());
        if (this.currentSchool != null) pTag.putString(TAG_SCHOOL, this.currentSchool.name());
        if (this.currentSpell != null) {
            pTag.putString(TAG_SPELL, BrutalitySpells.getIdFromSpell(this.currentSpell).toString());
        }
        if (this.craftingItem != null && !this.craftingItem.isEmpty()) {
            pTag.put(TAG_CRAFTING_ITEM, this.craftingItem.save(new CompoundTag()));
        }
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains(TAG_CUSTOM_NAME, 8)) this.name = Component.Serializer.fromJson(pTag.getString(TAG_CUSTOM_NAME));
        if (pTag.contains(TAG_SECTION)) this.currentSection = TableOfWizardryBookSection.valueOf(pTag.getString(TAG_SECTION));
        if (pTag.contains(TAG_STATE)) this.currentState = GuiState.valueOf(pTag.getString(TAG_STATE));
        if (pTag.contains(TAG_SCHOOL)) this.currentSchool = IBrutalitySpell.MagicSchool.valueOf(pTag.getString(TAG_SCHOOL));
        if (pTag.contains(TAG_SPELL)) this.currentSpell = BrutalitySpells.getSpell(ResourceLocation.parse(pTag.getString(TAG_SPELL)));
        if (pTag.contains(TAG_CRAFTING_ITEM)) this.craftingItem = ItemStack.of(pTag.getCompound(TAG_CRAFTING_ITEM));
    }

    private PedestalOfWizardryBlockEntity getPedestalAt(Vec3 offset) {
        if (level == null) return null;
        BlockPos checkPos = this.worldPosition.offset((int) offset.x, (int) offset.y, (int) offset.z);
        return level.getBlockEntity(checkPos) instanceof PedestalOfWizardryBlockEntity pedestal ? pedestal : null;
    }

    public List<ItemStack> getPedestalItems() {
        List<ItemStack> items = new ArrayList<>();
        for (Vec3 offset : PEDESTAL_OFFSETS) {
            PedestalOfWizardryBlockEntity pedestal = getPedestalAt(offset);
            items.add(pedestal != null ? pedestal.getStoredItem() : ItemStack.EMPTY);
        }
        return items;
    }

    public List<ItemStack> getPedestalItemsFiltered() {
        List<ItemStack> items = new ArrayList<>();
        for (Vec3 offset : PEDESTAL_OFFSETS) {
            PedestalOfWizardryBlockEntity pedestal = getPedestalAt(offset);
            if (pedestal != null && !pedestal.getStoredItem().isEmpty()) items.add(pedestal.getStoredItem());
        }
        return items;
    }

    public List<Pair<ItemStack, Vec3>> getPedestalItemsWithOffset() {
        List<Pair<ItemStack, Vec3>> items = new ArrayList<>();
        for (Vec3 offset : PEDESTAL_OFFSETS) {
            PedestalOfWizardryBlockEntity pedestal = getPedestalAt(offset);
            if (pedestal != null && !pedestal.getStoredItem().isEmpty()) {
                items.add(Pair.of(pedestal.getStoredItem(), offset));
            }
        }
        return items;
    }

    public float getNormalizedProgress() {
        return (float) progress / maxProgress;
    }


    public static class Tickers {

        public static void commonTick(Level level, TableOfWizardryBlockEntity blockEntity) {
            if (blockEntity.isCrafting) {
                blockEntity.prevProgress = blockEntity.progress;
                blockEntity.progress++;
                if (blockEntity.progress >= blockEntity.maxProgress) {
                    blockEntity.completeCraft();
                    blockEntity.stopCrafting();
                }
            } else {
                blockEntity.prevProgress = 0;
                blockEntity.progress = 0;
            }

            if (level.getGameTime() % 20 == 0) blockEntity.updateNearbyMagicBlockCount();
        }

        public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, TableOfWizardryBlockEntity blockEntity) {
            commonTick(pLevel, blockEntity);
            bookAnimationTick(blockEntity, pLevel, pPos);
        }

        public static void serverTick(Level level, BlockPos pos, BlockState state, TableOfWizardryBlockEntity blockEntity) {
            commonTick(level, blockEntity);
            blockEntity.setChanged();
        }

        public static void bookAnimationTick(TableOfWizardryBlockEntity entity, Level level, BlockPos pos) {
            entity.oOpen = entity.open;
            entity.oRot = entity.rot;
            Player nearestPlayer = level.getNearestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 3.0D, false);

            if (nearestPlayer != null) {
                double dx = nearestPlayer.getX() - (pos.getX() + 0.5D);
                double dz = nearestPlayer.getZ() - (pos.getZ() + 0.5D);
                entity.tRot = (float) Mth.atan2(dz, dx);
                entity.open += 0.1F;
                if (entity.open < 0.5F || RANDOM.nextInt(40) == 0) {
                    float oldFlipT = entity.flipT;
                    do {
                        entity.flipT += (float) (RANDOM.nextInt(4) - RANDOM.nextInt(4));
                    } while (oldFlipT == entity.flipT);
                }
            } else {
                entity.tRot += 0.02F;
                entity.open -= 0.1F;
            }

            while (entity.rot >= (float) Math.PI) entity.rot -= ((float) Math.PI * 2F);
            while (entity.rot < -(float) Math.PI) entity.rot += ((float) Math.PI * 2F);
            while (entity.tRot >= (float) Math.PI) entity.tRot -= ((float) Math.PI * 2F);
            while (entity.tRot < -(float) Math.PI) entity.tRot += ((float) Math.PI * 2F);

            float angleDelta = entity.tRot - entity.rot;
            while (angleDelta >= (float) Math.PI) angleDelta -= ((float) Math.PI * 2F);
            while (angleDelta < -(float) Math.PI) angleDelta += ((float) Math.PI * 2F);

            entity.rot += angleDelta * 0.4F;
            entity.open = Mth.clamp(entity.open, 0.0F, 1.0F);
            ++entity.time;
            entity.oFlip = entity.flip;
            float flipDelta = Mth.clamp((entity.flipT - entity.flip) * 0.4F, -0.2F, 0.2F);
            entity.flipA += (flipDelta - entity.flipA) * 0.9F;
            entity.flip += entity.flipA;
        }
    }

    public void tryStartCrafting(Player player, ItemStack item) {
        if (this.level == null || this.currentSection == null) return;

        delegateCraftingOperation(this.currentSection, player, item);

    }

    public void completeCraft() {
        if (this.level == null) return;
        delegateCraftingFinished(this.currentSection);
        this.stopCrafting();
    }

    private void delegateCraftingOperation(TableOfWizardryBookSection section, Player player, ItemStack craftingItem) {
        switch (section) {
            case CONJURE -> startConjureOperation();
            case SYNTHESISE -> startSynthesisOperation();
            case AUGMENT -> startAugmentOperation(player, craftingItem);
        }
    }

    private void delegateCraftingFinished(TableOfWizardryBookSection section) {
        switch (section) {
            case CONJURE -> completeConjureOperation();
            case SYNTHESISE -> completeSynthesisOperation();
            case AUGMENT -> completeAugmentOperation();
        }
    }

    public boolean canSynthesise() {
        if (this.level != null) {
            List<ItemStack> filtered = getPedestalItemsFiltered();
            if (filtered.size() != 2) return false;
            return SpellStorage.getSpells(filtered.get(0)).get(0).equals(SpellStorage.getSpells(filtered.get(1)).get(0));
        }
        return false;
    }

    public boolean canAugment() {
        if (this.level != null) {
            List<ItemStack> filtered = getPedestalItemsFiltered();
            for (ItemStack stack : filtered) {
                if (stack.getItem() instanceof BrutalityAugmentItem) return true;
            }
        }
        return false;
    }

    private void startAugmentOperation(Player player, ItemStack item) {
        if (canAugment()) {
            this.craftingPlayer = player;
            this.craftingPlayerStartPos = player.getPosition(0);
            this.craftingItem = item;


            if (this.craftingPlayer.getInventory().contains(this.craftingItem)) {
                this.craftingPlayer.getInventory().removeItem(craftingItem);
                this.isCrafting = true;
                this.progress = 0;
                this.maxProgress = getMaxProgress();
                this.setChanged();
                if (this.level != null) {
                    this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
                }
            }
        }
    }

    private void completeAugmentOperation() {
        if (canAugment()) {
            if (this.craftingItem != null) {
                List<ItemStack> consumedStacks = AugmentHelper.addAugments(
                        this.craftingItem,
                        getPedestalItemsFiltered().toArray(new ItemStack[0])
                );

                if (!consumedStacks.isEmpty()) {
                    for (ItemStack consumed : consumedStacks) {
                        for (Vec3 offset : PEDESTAL_OFFSETS) {
                            PedestalOfWizardryBlockEntity pedestal = getPedestalAt(offset);
                            if (pedestal != null) {
                                ItemStack pedestalStack = pedestal.getStoredItem();
                                // Match by reference or equality to ensure we don't shrink the wrong stack
                                if (!pedestalStack.isEmpty() && pedestalStack == consumed) {
                                    pedestalStack.shrink(1);
                                    pedestal.setChanged();
                                    break;
                                }
                            }
                        }
                    }
                }

                if (level != null && !level.isClientSide()) {
                    ItemStack result = craftingItem;
                    ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, result);
                    level.addFreshEntity(itemEntity);
                    craftingItem = null;
                    this.setChanged();
                }
            }
        }
    }


    private void startSynthesisOperation() {
        if (canSynthesise()) {
            this.isCrafting = true;
            this.progress = 0;
            this.maxProgress = getMaxProgress();
            this.setChanged();
            if (this.level != null) {
                this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    }

    private void completeSynthesisOperation() {
        if (canSynthesise()) {
            if (this.craftingPlayer != null && ManaHelper.getMana(this.craftingPlayer) >= 100) {
                SpellStorage.SpellEntry spellEntry = SpellStorage.getSpells(getPedestalItemsFiltered().get(0)).get(0);
                consumePedestalItems();
                if (level != null && !level.isClientSide()) {
                    ItemStack result = SpellStorage.getScrollFromSpell(spellEntry.spell(), spellEntry.level() + SynthesisView.SynthesisResult.getLevelBonus(getTotalPoints(), 0.0025));
                    ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, result);
                    level.addFreshEntity(itemEntity);
                }
            }
        }
    }

    private void startConjureOperation() {
        if (this.currentSpell == null) return;
        List<ItemStack> pedestalItems = this.getPedestalItems();
        SimpleContainer container = new SimpleContainer(pedestalItems.toArray(new ItemStack[0]));

        if (this.level != null) {
            this.level.getRecipeManager().getRecipeFor(BrutalityRecipes.CONJURE_TYPE.get(), container, this.level).ifPresent(recipe -> {
                if (!recipe.requiredEntities().isEmpty()) {
                    List<Entity> nearbyEntities = this.level.getEntitiesOfClass(Entity.class,
                            new AABB(this.worldPosition).inflate(5.0),
                            entity -> recipe.requiredEntities().contains(entity.getType()));
                    if (nearbyEntities.isEmpty()) return;
                }
                this.isCrafting = true;
                this.progress = 0;
                this.maxProgress = getMaxProgress();
                this.setChanged();
                this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
            });
        }
    }


    private void completeConjureOperation() {
        List<ItemStack> pedestalItems = this.getPedestalItems();
        SimpleContainer container = new SimpleContainer(pedestalItems.toArray(new ItemStack[0]));

        if (this.level != null) {
            this.level.getRecipeManager().getRecipeFor(BrutalityRecipes.CONJURE_TYPE.get(), container, this.level).ifPresent(recipe -> {
                if (enoughMana(recipe)) {
                    if (sacrificeEntities(recipe)) {
                        consumePedestalItems();
                        if (!level.isClientSide()) {
                            ItemStack result = recipe.getResultItem(this.level.registryAccess());
                            ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, result);
                            level.addFreshEntity(itemEntity);
                        }
                    }
                }
            });
        }
    }

    private boolean enoughMana(ConjureRecipe recipe) {
        if (level != null) {
            if (this.craftingPlayer != null) {
                if (ManaHelper.getMana(this.craftingPlayer) >= recipe.mana()) {
                    ManaHelper.modifyManaValue(this.craftingPlayer, -recipe.mana());
                    return true;
                }
            }
        }
        return false;
    }

    private void consumePedestalItems() {
        for (Vec3 offset : PEDESTAL_OFFSETS) {
            PedestalOfWizardryBlockEntity pedestal = getPedestalAt(offset);
            if (pedestal != null) {
                ItemStack stack = pedestal.getStoredItem();
                if (!stack.isEmpty()) {
                    stack.shrink(1);
                    pedestal.setChanged();
                    if (level != null) {
                        level.sendBlockUpdated(pedestal.getBlockPos(), pedestal.getBlockState(), pedestal.getBlockState(), 3);
                    }
                }
            }
        }
    }

    private boolean sacrificeEntities(ConjureRecipe recipe) {
        if (this.level == null) return false;
        if (recipe.requiredEntities().isEmpty()) return true;
        AABB sacrificeArea = new AABB(this.worldPosition).inflate(5.0);
        for (EntityType<?> type : recipe.requiredEntities()) {
            List<? extends Entity> targets = this.level.getEntitiesOfClass(Entity.class, sacrificeArea,
                    entity -> entity.getType() == type && entity.isAlive());
            if (!targets.isEmpty()) {
                Entity sacrifice = targets.get(0);
                if (this.level instanceof ClientLevel clientLevel) {
                    ParticleHelper.addParticles(clientLevel, TerramityModParticleTypes.EVIL_GLINT.get(), true, sacrifice.getX(), sacrifice.getY(0.5), sacrifice.getZ(), 10, 2, 2, 2, 0);
                }
                sacrifice.kill();
                return true;
            }
        }
        return false;
    }

    public void stopCrafting() {
        this.isCrafting = false;
        this.progress = 0;
        this.setChanged();
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos(), getBlockPos().offset(1, 1, 1)).inflate(5, 2, 5);
    }

    public @NotNull Component getName() {
        return this.name != null ? this.name : Component.translatable("container.enchant");
    }

    public void setCustomName(@Nullable Component pName) {
        this.name = pName;
    }

    @Nullable
    public Component getCustomName() {
        return this.name;
    }

    public enum GuiState {SECTION_VIEW, SPELL_PAGE}

    public static Vec3 getRenderItemOffset(Vec3 pedestalOffset, float progress, int itemCount, int itemIndex) {
        float animationEnd = 0.65f;
        float ritualProgress = Mth.clamp(progress / animationEnd, 0.0f, 1.0f);
        if (progress >= animationEnd) return new Vec3(0, 2.5, 0);

        double targetFormationAngle = itemIndex * ((Math.PI * 2.0) / itemCount);
        double startAngle = Math.atan2(pedestalOffset.z, pedestalOffset.x);
        double angleDiff = targetFormationAngle - startAngle;

        while (angleDiff <= -Math.PI) angleDiff += (Math.PI * 2.0);
        while (angleDiff > Math.PI) angleDiff -= (Math.PI * 2.0);

        double finalAngle = (startAngle + (angleDiff * Mth.clamp(ritualProgress / 0.5F, 0.0F, 1.0F))) + (ritualProgress * 15.0);
        float easing = ritualProgress * ritualProgress;
        double currentRadius = Mth.lerp(easing, Math.sqrt(pedestalOffset.x * pedestalOffset.x + pedestalOffset.z * pedestalOffset.z), 0.0);

        return new Vec3(Math.cos(finalAngle) * currentRadius, Mth.lerp(easing, pedestalOffset.y, 2.5), Math.sin(finalAngle) * currentRadius);
    }
}