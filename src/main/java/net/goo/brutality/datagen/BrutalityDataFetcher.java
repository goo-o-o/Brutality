package net.goo.brutality.datagen;

import com.google.common.collect.Multimap;
import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import net.daphne.lethality.LethalityMod;
import net.goo.brutality.Brutality;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.mcreator.terramity.TerramityMod;
import net.mcreator.terramity.entity.*;
import net.mehvahdjukaar.moonlight.core.misc.FakeLevel;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class BrutalityDataFetcher implements DataProvider {
    private final PackOutput.PathProvider dataPath;
    private final Map<String, String> translations = new HashMap<>();
    private final Map<String, String> curioTypes = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final DecimalFormat decimalFormat = new DecimalFormat("0.##");

    public BrutalityDataFetcher(PackOutput output) {
        this.dataPath = output.createPathProvider(PackOutput.Target.DATA_PACK, "item_data");
        loadTranslations();
        loadAllFilesFromModDirectory("terramity", "data/curios/tags/items");
        loadAllFilesFromModDirectory("brutality", "data/curios/tags/items");
        loadAllFilesFromModDirectory("lethality", "data/curios/tags/items");
    }

    private void loadTranslations() {
        loadMinecraftTranslations();
        loadTranslationFileFromJar("/assets/brutality/lang/en_us.json");
        loadTranslationsFromMod("terramity", "en_us.json");
        loadTranslationsFromMod("lethality", "en_us.json");

    }

    private void loadMinecraftTranslations() {
        try {
            File jarFile = new File(System.getProperty("user.home") +
                    "/.gradle/caches/forge_gradle/minecraft_repo/versions/1.20.1/client-extra.jar");

            try (JarFile jar = new JarFile(jarFile)) {
                JarEntry entry = jar.getJarEntry("assets/minecraft/lang/en_us.json");
                if (entry != null) {
                    try (InputStream stream = jar.getInputStream(entry)) {
                        JsonObject json = gson.fromJson(new InputStreamReader(stream), JsonObject.class);
                        json.entrySet().forEach(e ->
                                translations.put(e.getKey(), e.getValue().getAsString()));
                        Brutality.LOGGER.debug("Loaded {} translations from Minecraft JAR", json.size());
                    }
                } else {
                    Brutality.LOGGER.warn("Minecraft en_us.json not found in JAR");
                }
            }
        } catch (IOException e) {
            Brutality.LOGGER.error("Failed to load Minecraft translations: {}", e.toString());
        }
    }

    /**
     * Loads translations from this mod's JAR file
     */
    private void loadTranslationFileFromJar(String resourcePath) {
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in != null) {
                JsonObject json = gson.fromJson(new InputStreamReader(in), JsonObject.class);
                json.entrySet().forEach(entry -> translations.put(entry.getKey(), entry.getValue().getAsString()));
                Brutality.LOGGER.debug("Loaded {} translations from internal {}", json.size(), resourcePath);
            } else {
                Brutality.LOGGER.warn("Internal translation file not found: {}", resourcePath);
            }
        } catch (Exception e) {
            Brutality.LOGGER.error("Failed to load internal translations from {}: {}", resourcePath, e.toString());
        }
    }

    /**
     * Loads translations from another mod's JAR file
     */
    private void loadTranslationsFromMod(String modId, String langFile) {
        try {
            ModFile modFile = (ModFile) ModList.get().getModFileById(modId).getFile();
            Path path = modFile.findResource("assets/" + modId + "/lang/" + langFile);
            loadTranslationFileFromPath(path);
        } catch (Exception e) {
            Brutality.LOGGER.error("Failed to load translations from mod {}: {}", modId, e.toString());
        }
    }


    private void loadAllFilesFromModDirectory(String modId, String directoryPath) {
        try {
            ModFile modFile = (ModFile) ModList.get().getModFileById(modId).getFile();
            Path modRoot = modFile.findResource(""); // Gets the mod root directory

            // Walk through all files in the specified directory
            Files.walk(modRoot.resolve(directoryPath))
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(this::processCurioTypeFile);
        } catch (Exception e) {
            Brutality.LOGGER.error("Failed to load files from {} in mod {}: {}",
                    directoryPath, modId, e.toString());
        }
    }

    private void processCurioTypeFile(Path filePath) {
        try (InputStream in = Files.newInputStream(filePath)) {
            JsonObject json = gson.fromJson(new InputStreamReader(in), JsonObject.class);

            // Extract filename without extension as potential type name
            String fileName = filePath.getFileName().toString();
            String type = fileName.substring(0, fileName.lastIndexOf('.'));

            // Process the JSON content
            if (json.has("values")) {
                JsonArray items = json.getAsJsonArray("values");
                for (JsonElement item : items) {
                    String itemId = item.getAsString();
                    curioTypes.put(itemId, type); // Map item ID to type
                }
            }

            Brutality.LOGGER.debug("processCurioTypeFile# Loaded {} items from {}",
                    json.getAsJsonArray("values").size(), filePath);
        } catch (Exception e) {
            Brutality.LOGGER.error("Failed to process file {}: {}", filePath, e.toString());
        }
    }

    /**
     * Loads translations from a filesystem path
     */
    private void loadTranslationFileFromPath(Path path) {
        try (InputStream in = Files.newInputStream(path)) {
            JsonObject json = gson.fromJson(new InputStreamReader(in), JsonObject.class);
            json.entrySet().forEach(entry -> translations.put(entry.getKey(), entry.getValue().getAsString()));
            Brutality.LOGGER.debug("Loaded {} translations from {}", json.size(), path);
        } catch (Exception e) {
            Brutality.LOGGER.error("Failed to load translations from path {}: {}", path, e.toString());
        }
    }

    public static RegistryAccess.Frozen getFakeRegistryAccess() {
        ResourceKey<Registry<DimensionType>> DIMENSION_TYPE_KEY =
                ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("minecraft", "dimension_type"));

        ResourceKey<DimensionType> OVERWORLD_DIM_TYPE_KEY =
                ResourceKey.create(DIMENSION_TYPE_KEY, ResourceLocation.fromNamespaceAndPath("minecraft", "overworld"));

        MappedRegistry<DimensionType> dimensionTypeRegistry =
                new MappedRegistry<>(DIMENSION_TYPE_KEY, Lifecycle.stable(), false);

        DimensionType dummyDimType = new DimensionType(
                OptionalLong.of(6000L),
                true,
                false,
                false,
                true,
                1.0,
                true,
                true,
                0,
                384,
                384,
                BlockTags.INFINIBURN_OVERWORLD,
                ResourceLocation.fromNamespaceAndPath("minecraft", "overworld"),
                0.0f,
                new DimensionType.MonsterSettings(
                        false,
                        false,
                        ConstantInt.of(0),
                        0
                )
        );

        // ✅ Register the dummy dimension type with its key
        dimensionTypeRegistry.register(OVERWORLD_DIM_TYPE_KEY, dummyDimType, Lifecycle.stable());

        ResourceKey<Registry<DamageType>> DAMAGE_TYPE_KEY =
                ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("minecraft", "damage_type"));
        MappedRegistry<DamageType> damageTypeRegistry =
                new MappedRegistry<>(DAMAGE_TYPE_KEY, Lifecycle.stable(), false);


        List<ResourceKey<DamageType>> damageKeys = new ArrayList<>();

        for (Field field : DamageTypes.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) &&
                    field.getType().equals(ResourceKey.class)) {

                try {
                    @SuppressWarnings("unchecked")
                    ResourceKey<DamageType> key = (ResourceKey<DamageType>) field.get(null);
                    damageKeys.add(key);
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // or log
                }
            }
        }
        for (ResourceKey<DamageType> key : damageKeys) {
            damageTypeRegistry.register(
                    key,
                    new DamageType(key.location().getPath(), 0.0F),
                    Lifecycle.stable()
            );
        }

        return new RegistryAccess.ImmutableRegistryAccess(
                Map.of(DIMENSION_TYPE_KEY, dimensionTypeRegistry,
                        DAMAGE_TYPE_KEY, damageTypeRegistry)
        ).freeze();
    }

    // ========== Main Data Generation ==========
    public @NotNull CompletableFuture<?> run(CachedOutput cache) {
        List<ItemData> itemDataList = new ArrayList<>();
        List<EffectData> effectDataList = new ArrayList<>();
        List<EntityData> entityDataList = new ArrayList<>();

        RegistryAccess.Frozen fakeRegistryAccess = getFakeRegistryAccess();
        // Process items
        ForgeRegistries.ITEMS.forEach(item -> {
            String modId = item.getCreatorModId(item.getDefaultInstance());
            if (shouldProcess(modId)) {
                ItemData data = processItem(item);
                itemDataList.add(data);
            }
        });

        // Process effects
        ForgeRegistries.MOB_EFFECTS.forEach(effect -> {
            ResourceLocation registryName = ForgeRegistries.MOB_EFFECTS.getKey(effect);
            if (registryName != null) {
                String modId = registryName.getNamespace();
                if (shouldProcess(modId)) {
                    EffectData data = processEffect(effect, modId);
                    effectDataList.add(data);
                }
            }
        });

        ForgeRegistries.ENTITY_TYPES.forEach(entity -> {
            ResourceLocation registryName = ForgeRegistries.ENTITY_TYPES.getKey(entity);
            if (registryName != null) {
                String modId = registryName.getNamespace();
                if (shouldProcess(modId)) {
                    EntityData data = processEntity(entity, modId, fakeRegistryAccess);
                    entityDataList.add(data);
                }
            }
        });


        Path itemPath = this.dataPath.json(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "item_stats"));
        Path effectPath = this.dataPath.json(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "effect_stats"));
        Path entityPath = this.dataPath.json(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "entity_stats"));

        CompletableFuture<?> itemFuture = DataProvider.saveStable(cache, gson.toJsonTree(itemDataList), itemPath);
        CompletableFuture<?> effectFuture = DataProvider.saveStable(cache, gson.toJsonTree(effectDataList), effectPath);
        CompletableFuture<?> entityFuture = DataProvider.saveStable(cache, gson.toJsonTree(entityDataList), entityPath);

        return CompletableFuture.allOf(itemFuture, effectFuture, entityFuture);
    }

    private boolean shouldProcess(String modId) {
        return modId != null && (modId.equalsIgnoreCase(Brutality.MOD_ID) || modId.equalsIgnoreCase(TerramityMod.MODID) || modId.equalsIgnoreCase(LethalityMod.MOD_ID));
    }

    private EffectData processEffect(MobEffect effect, String modId) {
        EffectData data = new EffectData();
        data.id = Objects.requireNonNull(ForgeRegistries.MOB_EFFECTS.getKey(effect)).getPath();
        data.namespace = modId;
        data.name = translations.getOrDefault(effect.getDescriptionId(), fallbackName(effect.getDescriptionId()));
        data.category = effect.getCategory().toString().toLowerCase();
        return data;
    }

    private static final Set<Class<?>> BOSSES = Set.of(
            GobEntity.class,
            SuperSnifferEntity.class,
            TrialGuardianEntity.class,
            VirtueEntity.class,
            GundalfEntity.class,
            SorceressCirceEntity.class,
            UltraSnifferEntity.class
    );
    private static final Set<Class<?>> MINIBOSSES = Set.of(
            UvogreEntity.class,
            HellrokEntity.class
    );

    private EntityData processEntity(EntityType<? extends Entity> entity, String modId, RegistryAccess.Frozen registryAccess) {
        EntityData data = new EntityData();
        data.id = Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entity)).getPath();
        data.namespace = modId;
        data.name = translations.getOrDefault(entity.getDescriptionId(), fallbackName(entity.getDescriptionId()));
        data.height = String.valueOf(entity.getHeight());
        data.width = String.valueOf(entity.getHeight());
        try {

            FakeLevel fakeLevel = FakeLevel.getDefault(false, registryAccess);
            Entity entityInstance = entity.create(fakeLevel);

            if (entityInstance instanceof LivingEntity living) {
                data.health = String.valueOf(living.getMaxHealth());
                data.armor = String.valueOf(living.getArmorValue());
                data.xpReward = String.valueOf(living.getExperienceReward());


                for (Class<?> bossClass : BOSSES) {
                    if (bossClass.isInstance(entityInstance)) {
                        data.type = "boss";
                        break;
                    }
                }

                for (Class<?> miniBossClass : MINIBOSSES) {
                    if (miniBossClass.isInstance(entityInstance)) {
                        data.type = "miniboss";
                        break;
                    }
                }

                MobType species = living.getMobType();
                if (species == MobType.UNDEFINED) {
                    data.species = "unknown";
                } else if (species == MobType.UNDEAD) {
                    data.species = "undead";
                } else if (species == MobType.ARTHROPOD) {
                    data.species = "arthropod";
                } else if (species == MobType.ILLAGER) {
                    data.species = "illager";
                } else if (species == MobType.WATER) {
                    data.species = "water";
                } else {
                    data.species = "custom";
                }

                if (entityInstance instanceof Monster) {
                    data.category = "hostile";
                } else if (entityInstance instanceof Animal || entityInstance instanceof AmbientCreature) {
                    data.category = "passive";
                } else if (entityInstance instanceof PathfinderMob) {
                    data.category = "neutral"; // fallback/default
                } else {
                    data.category = "unknown";
                }

            }
        } catch (Exception e) {
            Brutality.LOGGER.warn("Failed to create FakeLevel or entity: {}", e.getMessage());
        }

        return data;
    }


    private ItemData processItem(Item item) {
        ItemStack stack = item.getDefaultInstance();
        ItemData data = new ItemData();

        data.id = item.toString();
        data.namespace = item.getCreatorModId(stack);
        data.name = translations.getOrDefault(item.getDescriptionId(), fallbackName(item.getDescriptionId()));

        analyzeItemType(item, stack, data);
        analyzeFoodProperties(item, stack, data);
        analyzeAttributes(item, stack, data);
        analyzeDurability(item, stack, data);
        analyzeRarity(item, stack, data);
        if (stack.isEnchantable())
            analyzeEnchantability(stack, data);
        if (item instanceof ArmorItem armorItem)
            analyzeArmorMaterial(armorItem, data);
        determineCategory(data, item);


        return data;
    }

    private void analyzeEnchantability(ItemStack stack, ItemData data) {
        data.allowedEnchantments = getAllowedEnchants(stack).toString();
        data.enchantmentValue = String.valueOf(stack.getEnchantmentValue());
    }

    private List<String> getAllowedEnchants(ItemStack stack) {
        List<String> allowed = new ArrayList<>();
        for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS) {
            if (enchantment.canEnchant(stack))
                allowed.add(translations.getOrDefault(enchantment.getDescriptionId(), String.valueOf(enchantment.getFullname(0))));
        }

        return allowed;
    }

    // ========== Item Analysis Methods ==========
    private void analyzeItemType(Item item, ItemStack stack, ItemData data) {
        if (item instanceof BlockItem blockItem) {
            analyzeBlockItem(blockItem, data);
        } else if (item instanceof BrutalityGeoItem geoItem) {
            data.itemType = geoItem.getCategoryAsString().toLowerCase();
        } else if (item instanceof ICurioItem) {
            data.itemType = "curio";
            data.category = determineCurioType(item);
        } else {
            data.itemType = determineVanillaItemType(stack);
        }
    }

    private void analyzeArmorMaterial(ArmorItem item, ItemData data) {
        data.armorMaterial = item.getMaterial().getName();
        data.armorValue = String.valueOf(item.getDefense());
        data.armorToughness = String.valueOf(item.getToughness());
        data.armorType = String.valueOf(item.getType());
    }


    private void analyzeBlockItem(BlockItem blockItem, ItemData data) {
        data.itemType = "block";
        Block block = blockItem.getBlock();
        data.blockType = BlockTypeAnalyzer.getBlockType(block);
    }

    private String determineVanillaItemType(ItemStack stack) {
        for (Map.Entry<Predicate<ItemStack>, String> entry : EquipmentTypeMap.VANILLA_TYPES.entrySet()) {
            if (entry.getKey().test(stack)) {
                return entry.getValue();
            }
        }
        return "generic";
    }

    private void analyzeFoodProperties(Item item, ItemStack stack, ItemData data) {
        FoodProperties foodProps = item.getFoodProperties(stack, null);
        if (foodProps != null) {
            data.itemType = "food";
            data.foodType = foodProps.isMeat() ? "meat" : "non-meat";
            data.foodEffects = FoodEffectSerializer.serializeEffects(foodProps.getEffects());
        }
    }

    private void analyzeAttributes(Item item, ItemStack stack, ItemData data) {
        Multimap<Attribute, AttributeModifier> attributes = item.getAttributeModifiers(EquipmentSlot.MAINHAND, stack);
        if (attributes != null) {
            Collection<AttributeModifier> dmg = attributes.get(Attributes.ATTACK_DAMAGE);
            Collection<AttributeModifier> spd = attributes.get(Attributes.ATTACK_SPEED);
            if (!dmg.isEmpty()) data.attackDamage = decimalFormat.format(dmg.iterator().next().getAmount());
            if (!spd.isEmpty()) data.attackSpeed = decimalFormat.format(4 + spd.iterator().next().getAmount());
        }
    }

    private void analyzeDurability(Item item, ItemStack stack, ItemData data) {
        if (item.getMaxDamage(stack) != 0) {
            data.durability = item.getMaxDamage(stack);
        }
    }

    private void analyzeRarity(Item item, ItemStack stack, ItemData data) {
        data.rarity = item.getRarity(stack).toString().toLowerCase();
    }

    private String determineCurioType(Item item) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
        String fullId = itemId.toString();
        return curioTypes.getOrDefault(fullId, "curio");
    }

    private void determineCategory(ItemData data, Item item) {
        // First try the original equipment checks
        try {
            BrutalityCategories.ItemType itemType = BrutalityCategories.ItemType.valueOf(data.itemType.toUpperCase(Locale.ROOT));
            if (CategoryDeterminer.WEAPONS.contains(itemType)) {
                data.category = "weapon";
                return;
            }
            if (CategoryDeterminer.TOOLS.contains(itemType)) {
                data.category = "tool";
                return;
            }
            if (itemType == BrutalityCategories.ItemType.ARMOR) {
                data.category = "armor";
                return;
            }
        } catch (IllegalArgumentException ignored) {
        }

        String curioType = determineCurioType(item);
        if (!"curio".equals(curioType)) {
            data.itemType = curioType;
            data.category = "curio";
            return;
        }

        try {
            BrutalityCategories.CurioType.valueOf(data.itemType.toUpperCase(Locale.ROOT));
            data.category = "curio";
            return;
        } catch (IllegalArgumentException ignored) {
            // Continue to default
        }

        // Default case if nothing else matched
        data.category = "generic";
    }

    // ========== Helper Methods ==========
    private static String fallbackName(String descId) {
        String[] parts = descId.split("\\.");
        return parts.length > 2 ? parts[2] : descId;
    }

    @Override
    public @NotNull String getName() {
        return "Brutality Item Stats Provider";
    }

    // ========== Data Classes ==========
    private static class ItemData {
        String allowedEnchantments;
        String namespace;
        String id;
        String name;
        Integer durability;
        String attackDamage;
        String attackSpeed;
        String rarity;
        String itemType;
        String category;
        String blockType;
        String foodType;
        String foodEffects;
        String armorMaterial;
        String armorValue;
        String armorToughness;
        String armorType;
        String enchantmentValue;
    }

    private static class EffectData {
        String namespace;
        String id;
        String name;
        String category;
    }

    private static class EntityData {
        String namespace;
        String id;
        String name;
        String category;
        String type;
        String xpReward;
        String health;
        String width;
        String height;
        String armor;
        String species;
    }

    // ========== Helper Classes ==========
    private static class BlockTypeAnalyzer {
        private static final Map<Class<? extends Block>, String> BLOCK_TYPE_MAP = Map.ofEntries(
                Map.entry(StairBlock.class, "stair"),
                Map.entry(SlabBlock.class, "slab"),
                Map.entry(FenceBlock.class, "fence"),
                Map.entry(WallBlock.class, "wall"),
                Map.entry(DoorBlock.class, "door"),
                Map.entry(TrapDoorBlock.class, "trapdoor"),
                Map.entry(ButtonBlock.class, "button"),
                Map.entry(PressurePlateBlock.class, "pressure_plate"),
                Map.entry(LeavesBlock.class, "leaves"),
                Map.entry(SaplingBlock.class, "sapling"),
                Map.entry(FlowerBlock.class, "flower"),
                Map.entry(CropBlock.class, "crop"),
                Map.entry(BaseEntityBlock.class, "tile_entity")
        );

        public static String getBlockType(Block block) {
            return BLOCK_TYPE_MAP.entrySet().stream()
                    .filter(entry -> entry.getKey().isInstance(block))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .orElse("basic");
        }
    }

    private static class FoodEffectSerializer {
        public static String serializeEffects(List<Pair<MobEffectInstance, Float>> effects) {
            if (effects == null || effects.isEmpty()) return "[]";

            List<Map<String, Object>> effectsList = new ArrayList<>();
            for (Pair<MobEffectInstance, Float> effectPair : effects) {
                MobEffectInstance effect = effectPair.getFirst();
                if (effect != null) {
                    effectsList.add(createEffectData(effect, effectPair.getSecond()));
                }
            }
            return new Gson().toJson(effectsList);
        }

        private static Map<String, Object> createEffectData(MobEffectInstance effect, float probability) {
            Map<String, Object> effectData = new HashMap<>();
            effectData.put("effect", effect.getEffect().getDisplayName().getString());
            effectData.put("duration", effect.getDuration());
            effectData.put("amplifier", effect.getAmplifier());
            effectData.put("probability", probability);
            return effectData;
        }
    }

    private static class EquipmentTypeMap {
        static final Map<Predicate<ItemStack>, String> VANILLA_TYPES = new LinkedHashMap<>() {{
            put(stack -> stack.getItem() instanceof SwordItem || stack.is(ItemTags.SWORDS), "sword");
            put(stack -> stack.getItem() instanceof PickaxeItem || stack.is(ItemTags.PICKAXES), "pickaxe");
            put(stack -> stack.getItem() instanceof AxeItem || stack.is(ItemTags.AXES), "axe");
            put(stack -> stack.getItem() instanceof ShovelItem || stack.is(ItemTags.SHOVELS), "shovel");
            put(stack -> stack.getItem() instanceof HoeItem || stack.is(ItemTags.HOES), "hoe");
            put(stack -> stack.getItem() instanceof BowItem, "bow");
            put(stack -> stack.getItem() instanceof CrossbowItem, "crossbow");
            put(stack -> stack.getItem() instanceof TridentItem, "trident");
            put(stack -> stack.getItem() instanceof ArmorItem, "armor");
            put(stack -> stack.getItem() instanceof ElytraItem, "elytra");
            put(stack -> stack.getItem() instanceof ShieldItem, "shield");
            put(stack -> stack.getItem() instanceof FishingRodItem, "fishing_rod");
            put(stack -> stack.getItem() instanceof FlintAndSteelItem, "flint_and_steel");
            put(stack -> stack.getItem() instanceof ShearsItem, "shears");
            put(stack -> stack.getItem() instanceof CompassItem, "compass");
        }};
    }

    private static class CategoryDeterminer {
        static final Set<BrutalityCategories.ItemType> WEAPONS = EnumSet.of(
                BrutalityCategories.ItemType.SWORD,
                BrutalityCategories.ItemType.AXE,
                BrutalityCategories.ItemType.HAMMER,
                BrutalityCategories.ItemType.TRIDENT,
                BrutalityCategories.ItemType.SCYTHE,
                BrutalityCategories.ItemType.BOW,
                BrutalityCategories.ItemType.LANCE,
                BrutalityCategories.ItemType.STAFF
        );

        static final Set<BrutalityCategories.ItemType> TOOLS = EnumSet.of(
                BrutalityCategories.ItemType.PICKAXE
//                BrutalityCategories.ItemType.SHOVEL,
//                BrutalityCategories.ItemType.HOE
        );
    }
}

