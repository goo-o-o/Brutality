package net.goo.brutality.client.datagen;

import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import net.daphne.lethality.LethalityMod;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.base.BrutalityGeoItem;
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
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.JarFile;


public class BrutalityDataFetcher implements DataProvider {
    private final PackOutput.PathProvider dataPath;
    private final Map<String, String> translations = new HashMap<>();
    private final Map<String, String> curioTypes = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    protected final DecimalFormat decimalFormat = new DecimalFormat("0.##");
    private final Map<String, Map<String, List<String>>> typesByMod = new LinkedHashMap<>();
    private final Map<String, Set<String>> raritiesByMod = new HashMap<>();
    private static final Set<String> targetMods = Set.of(
            Brutality.MOD_ID,
            TerramityMod.MODID,
            LethalityMod.MOD_ID
    );

    public BrutalityDataFetcher(PackOutput output) {
        this.dataPath = output.createPathProvider(PackOutput.Target.DATA_PACK, "stats");
        loadAllData();
    }

    private void loadAllData() {
        loadTranslations();
        loadCurioTypes("terramity");
        loadCurioTypes("brutality");
        loadCurioTypes("lethality");
    }

    private void loadTranslations() {
        TranslationLoader.loadMinecraft(translations, gson);
        TranslationLoader.loadFromResource(translations, gson, "/assets/brutality/lang/en_us.json");
        TranslationLoader.loadFromMod(translations, gson, "terramity", "en_us.json");
        TranslationLoader.loadFromMod(translations, gson, "lethality", "en_us.json");
    }

    private void loadCurioTypes(String modId) {
        CurioTypeLoader.loadFromMod(curioTypes, gson, modId);
    }

    @Override
    public @NotNull CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        // Process all registries
        processItems(futures, cache);
        processEffects(futures, cache);
        processEntities(futures, cache);

        // Save aggregated data
        saveTypeLists(futures, cache);
        saveRarityLists(futures, cache);

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private void processItems(List<CompletableFuture<?>> futures, CachedOutput cache) {
        var fakeLevel = FakeLevel.getDefault(false, getFakeRegistryAccess());

        ForgeRegistries.ITEMS.forEach(item -> {
            String modId = item.getCreatorModId(item.getDefaultInstance());
            if (!shouldProcess(modId)) return;

            ItemData data = ItemProcessor.process(item, fakeLevel, translations, curioTypes, decimalFormat);
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);

            if (itemId != null) {
                Path itemPath = dataPath.json(ResourceLocation.fromNamespaceAndPath(modId, "item/" + itemId.getPath()));
                futures.add(DataProvider.saveStable(cache, gson.toJsonTree(data), itemPath));

                // Track for type aggregation
                typesByMod.computeIfAbsent(modId, k -> new LinkedHashMap<>())
                        .computeIfAbsent(data.itemType, k -> new ArrayList<>())
                        .add(itemId.getPath());

                // Track for rarity aggregation
                if (data.rarity != null) {
                    raritiesByMod.computeIfAbsent(modId, k -> new LinkedHashSet<>())
                            .add(data.rarity);
                }
            }
        });
    }

    private void processEffects(List<CompletableFuture<?>> futures, CachedOutput cache) {
        ForgeRegistries.MOB_EFFECTS.forEach(effect -> {
            ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(effect);
            if (effectId != null && shouldProcess(effectId.getNamespace())) {
                EffectData data = EffectProcessor.process(effect, translations);
                Path effectPath = dataPath.json(ResourceLocation.fromNamespaceAndPath(
                        effectId.getNamespace(), "effect/" + effectId.getPath()));
                futures.add(DataProvider.saveStable(cache, gson.toJsonTree(data), effectPath));
            }
        });
    }

    private void processEntities(List<CompletableFuture<?>> futures, CachedOutput cache) {
        var fakeLevel = FakeLevel.getDefault(false, getFakeRegistryAccess());

        ForgeRegistries.ENTITY_TYPES.forEach(entityType -> {
            ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
            if (entityId != null && shouldProcess(entityId.getNamespace())) {
                EntityData data;
                try {
                    data = EntityProcessor.process(entityType, fakeLevel, translations);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                Path entityPath = dataPath.json(ResourceLocation.fromNamespaceAndPath(
                        entityId.getNamespace(), "entity/" + entityId.getPath()));
                futures.add(DataProvider.saveStable(cache, gson.toJsonTree(data), entityPath));
            }
        });
    }

    private void saveTypeLists(List<CompletableFuture<?>> futures, CachedOutput cache) {
        typesByMod.forEach((modId, typeMap) -> {
            typeMap.forEach((type, itemList) -> {
                itemList.sort(String::compareToIgnoreCase);
                JsonArray array = new JsonArray();
                itemList.forEach(array::add);

                ResourceLocation typeRl = ResourceLocation.fromNamespaceAndPath(modId, "types/" + type);
                Path typePath = dataPath.json(typeRl);
                futures.add(DataProvider.saveStable(cache, array, typePath));
            });
        });
    }

    private void saveRarityLists(List<CompletableFuture<?>> futures, CachedOutput cache) {
        raritiesByMod.forEach((modId, raritySet) -> {
            if (!raritySet.isEmpty()) {
                List<String> rarityList = new ArrayList<>(raritySet);
                rarityList.sort(String::compareToIgnoreCase);

                JsonArray array = new JsonArray();
                rarityList.forEach(array::add);

                Path rarityPath = dataPath.json(ResourceLocation.fromNamespaceAndPath(modId, "rarities"));
                futures.add(DataProvider.saveStable(cache, array, rarityPath));
            }
        });
    }

    public static boolean shouldProcess(String modId) {
        return modId != null && targetMods.contains(modId.toLowerCase());
    }

    @Override
    public @NotNull String getName() {
        return "Brutality Data Fetcher";
    }

    // ========== Registry Methods ==========
    public static RegistryAccess.Frozen getFakeRegistryAccess() {
        return RegistryHelper.createFakeRegistryAccess();
    }

    // ========== Data Classes ==========
    public static class ItemData {
        public String name;
        public String itemType;
        public String rarity;
        public Integer durability;
        public String enchantmentValue;
        public JsonArray allowedEnchantments;
        public FoodData foodData;
        public Map<String, List<AttributeData>> attributes;

        // Armor-specific
        public String armorMaterial;
        public Integer armorValue;
        public Float armorToughness;
        public String armorType;
        public Float kbResistance;

        // Block-specific
        public String blockType;

        public static class FoodData {
            public float nutrition;
            public float saturation;
            public boolean isMeat;
            public boolean canAlwaysEat;
            public boolean isFastFood;
            public List<EffectData.FoodEffectData> effects;
        }

        public static class AttributeData {
            public String attribute;
            public float amount;
            public String operation;
        }
    }

    public static class EffectData {
        public String name;
        public String category;

        public static class FoodEffectData extends EffectData {
            public float duration;
            public float amplifier;
            public Float chance;
        }
    }

    public static class EntityData {
        public String name;
        public String category;
        public String type;
        public Integer xpReward;
        public Float health;
        public Float width;
        public Float height;
        public Integer armor;
        public boolean isLiving = false;
    }


// ========== Helper Classes ==========

    static class TranslationLoader {
        public static void loadMinecraft(Map<String, String> translations, Gson gson) {
            try {
                String jarPath = System.getProperty("user.home") +
                        "/.gradle/caches/forge_gradle/minecraft_repo/versions/1.20.1/client-extra.jar";
                JsonObject json = JsonLoader.loadFromJar(jarPath, "assets/minecraft/lang/en_us.json", gson);
                json.entrySet().forEach(e -> translations.put(e.getKey(), e.getValue().getAsString()));
                Brutality.LOGGER.debug("Loaded {} Minecraft translations", json.size());
            } catch (IOException e) {
                Brutality.LOGGER.error("Failed to load Minecraft translations", e);
            }
        }

        public static void loadFromResource(Map<String, String> translations, Gson gson, String resourcePath) {
            JsonObject json = JsonLoader.loadFromResource(resourcePath, gson);
            if (json != null) {
                json.entrySet().forEach(e -> translations.put(e.getKey(), e.getValue().getAsString()));
            }
        }

        public static void loadFromMod(Map<String, String> translations, Gson gson, String modId, String langFile) {
            JsonObject json = JsonLoader.loadFromMod(modId, "assets/" + modId + "/lang/" + langFile, gson);
            if (json != null) {
                json.entrySet().forEach(e -> translations.put(e.getKey(), e.getValue().getAsString()));
            }
        }
    }

    static class CurioTypeLoader {
        public static void loadFromMod(Map<String, String> curioTypes, Gson gson, String modId) {
            List<JsonObject> jsons = JsonLoader.loadAllFromModDir(modId, "data/curios/tags/items", gson);
            jsons.forEach(json -> processCurioJson(json, curioTypes));
        }

        private static void processCurioJson(JsonObject json, Map<String, String> curioTypes) {
            if (json.has("values")) {
                JsonArray items = json.getAsJsonArray("values");
                items.forEach(item -> {
                    // Get type from filename (handled by JsonLoader)
                    curioTypes.put(item.getAsString(), "curio"); // Simplified - type comes from filename in JsonLoader
                });
            }
        }
    }

    static class JsonLoader {
        public static JsonObject loadFromJar(String jarPath, String entryPath, Gson gson) throws IOException {
            return JarUtils.loadJsonFromJar(jarPath, entryPath, gson);
        }

        public static JsonObject loadFromResource(String resourcePath, Gson gson) {
            try (InputStream in = BrutalityDataFetcher.class.getResourceAsStream(resourcePath)) {
                return in != null ? gson.fromJson(new InputStreamReader(in), JsonObject.class) : null;
            } catch (Exception e) {
                Brutality.LOGGER.error("Failed to load resource: {}", resourcePath, e);
                return null;
            }
        }

        public static JsonObject loadFromMod(String modId, String path, Gson gson) {
            try {
                var modFile = ModList.get().getModFileById(modId).getFile();
                Path fullPath = modFile.findResource(path);
                return loadFromPath(fullPath, gson);
            } catch (Exception e) {
                Brutality.LOGGER.error("Failed to load from mod {}: {}", modId, path, e);
                return null;
            }
        }

        public static List<JsonObject> loadAllFromModDir(String modId, String dirPath, Gson gson) {
            List<JsonObject> result = new ArrayList<>();
            try {
                var modFile = ModList.get().getModFileById(modId).getFile();
                Path root = modFile.findResource("");
                FileUtils.walkJsonFiles(root.resolve(dirPath), path -> {
                    JsonObject json = loadFromPath(path, gson);
                    if (json != null) result.add(json);
                });
            } catch (Exception e) {
                Brutality.LOGGER.error("Failed to load files from mod {}: {}", modId, dirPath, e);
            }
            return result;
        }

        private static JsonObject loadFromPath(Path path, Gson gson) {
            try (InputStream in = Files.newInputStream(path)) {
                return gson.fromJson(new InputStreamReader(in), JsonObject.class);
            } catch (Exception e) {
                Brutality.LOGGER.error("Failed to load from path: {}", path, e);
                return null;
            }
        }
    }

    static class ItemProcessor {
        public static BrutalityDataFetcher.ItemData process(Item item, FakeLevel level,
                                                            Map<String, String> translations, Map<String, String> curioTypes,
                                                            DecimalFormat decimalFormat) {
            BrutalityDataFetcher.ItemData data = new BrutalityDataFetcher.ItemData();
            ItemStack stack = item.getDefaultInstance();
            String key = item.getDescriptionId();
            data.name = translations.getOrDefault(key,
                    translations.getOrDefault(key.replace("block.", "item."), "Unknown"));


            // Determine item type
            data.itemType = determineItemType(item, stack, curioTypes);

            // Basic properties
            if (item.getMaxDamage(stack) > 0) {
                data.durability = item.getMaxDamage(stack);
            }

            data.rarity = item.getRarity(stack).toString().toLowerCase();

            // Enchantments
            if (stack.isEnchantable()) {
                data.enchantmentValue = String.valueOf(stack.getEnchantmentValue());
                data.allowedEnchantments = getAllowedEnchants(stack, translations);
            }

            // Food
            if (item.getFoodProperties(stack, null) != null) {
                data.foodData = processFood(item, stack);
            }

            // Armor
            if (item instanceof ArmorItem armor) {
                processArmor(armor, data);
            }

            // Attributes
            if (level != null) {
                data.attributes = processAttributes(item, stack, curioTypes, decimalFormat);
            }

            return data;
        }

        private static String determineItemType(Item item, ItemStack stack, Map<String, String> curioTypes) {
            if (item instanceof BlockItem) return "block";
            if (item instanceof BrutalityGeoItem geo) return geo.getCategoryAsString();
            if (item instanceof ICurioItem) return curioTypes.getOrDefault(
                    ForgeRegistries.ITEMS.getKey(item).toString(), "curio");
            return ItemTypeMapper.getVanillaType(stack);
        }

        private static JsonArray getAllowedEnchants(ItemStack stack, Map<String, String> translations) {
            JsonArray array = new JsonArray();
            ForgeRegistries.ENCHANTMENTS.forEach(enchantment -> {
                if (enchantment.canEnchant(stack)) {
                    JsonObject obj = new JsonObject();
                    obj.addProperty("name", translations.getOrDefault(
                            enchantment.getDescriptionId(),
                            String.valueOf(enchantment.getFullname(0))));
                    obj.addProperty("min", enchantment.getMinLevel());
                    obj.addProperty("max", enchantment.getMaxLevel());
                    array.add(obj);
                }
            });
            return array;
        }

        private static BrutalityDataFetcher.ItemData.FoodData processFood(Item item, ItemStack stack) {
            FoodProperties food = item.getFoodProperties(stack, null);
            if (food == null) return null;

            BrutalityDataFetcher.ItemData.FoodData foodData = new BrutalityDataFetcher.ItemData.FoodData();
            foodData.nutrition = food.getNutrition();
            foodData.saturation = food.getNutrition() * food.getSaturationModifier() * 2.0f;
            foodData.isMeat = food.isMeat();
            foodData.canAlwaysEat = food.canAlwaysEat();
            foodData.isFastFood = food.isFastFood();
            foodData.effects = FoodEffectSerializer.serializeEffects(food.getEffects());

            return foodData;
        }

        private static class FoodEffectSerializer {
            public static List<BrutalityDataFetcher.EffectData.FoodEffectData> serializeEffects(List<Pair<MobEffectInstance, Float>> effectsWithPercentage) {
                if (effectsWithPercentage == null || effectsWithPercentage.isEmpty()) return new ArrayList<>();

                List<BrutalityDataFetcher.EffectData.FoodEffectData> effectsList = new ArrayList<>();
                effectsWithPercentage.forEach(effectInstanceFloatPair -> {
                    MobEffectInstance instance = effectInstanceFloatPair.getFirst();
                    Float chance = effectInstanceFloatPair.getSecond();

                    BrutalityDataFetcher.EffectData.FoodEffectData data = new BrutalityDataFetcher.EffectData.FoodEffectData();
                    data.name = String.valueOf(instance.getEffect().getDisplayName());
                    data.amplifier = instance.getAmplifier();
                    data.duration = instance.getDuration();
                    data.chance = chance;

                    effectsList.add(data);
                });
                return effectsList;
            }
        }

        private static void processArmor(ArmorItem armor, BrutalityDataFetcher.ItemData data) {
            data.armorMaterial = armor.getMaterial().getName();
            data.armorValue = armor.getDefense();
            data.armorToughness = armor.getToughness();
            data.kbResistance = armor.getMaterial().getKnockbackResistance();
            data.armorType = armor.getType().toString();
        }

        private static Map<String, List<BrutalityDataFetcher.ItemData.AttributeData>> processAttributes(
                Item item, ItemStack stack, Map<String, String> curioTypes, DecimalFormat decimalFormat) {

            Map<String, List<BrutalityDataFetcher.ItemData.AttributeData>> slotAttributes = new LinkedHashMap<>();

            // Process regular equipment slots
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                Multimap<Attribute, AttributeModifier> attrs = item.getAttributeModifiers(slot, stack);
                if (!attrs.isEmpty()) {
                    slotAttributes.put(slot.getName(), convertAttributes(attrs));
                }
            }

            // Process curio slots
            if (item instanceof ICurioItem) {
                processCurioAttributes(item, stack, curioTypes, slotAttributes);
            }

            return slotAttributes;
        }

        private static List<BrutalityDataFetcher.ItemData.AttributeData> convertAttributes(
                Multimap<Attribute, AttributeModifier> attributes) {

            List<BrutalityDataFetcher.ItemData.AttributeData> result = new ArrayList<>();

            for (var entry : attributes.entries()) {
                Attribute attribute = entry.getKey();
                AttributeModifier modifier = entry.getValue();

                BrutalityDataFetcher.ItemData.AttributeData data = new BrutalityDataFetcher.ItemData.AttributeData();
                data.attribute = attribute.getDescriptionId();
                data.amount = (float) modifier.getAmount();
                data.operation = modifier.getOperation().toString();

                result.add(data);
            }

            return result;
        }

        private static void processCurioAttributes(Item curioItem, ItemStack stack,
                                                   Map<String, String> curioTypes, Map<String, List<BrutalityDataFetcher.ItemData.AttributeData>> slotAttributes) {

            String itemId = ForgeRegistries.ITEMS.getKey(curioItem).toString();
            String slotType = curioTypes.getOrDefault(itemId, "curio");

            try {
                SlotContext slotContext = new SlotContext(slotType, null, 0, false, true);
                UUID slotUuid = UUID.nameUUIDFromBytes((slotType + "_attribute").getBytes(StandardCharsets.UTF_8));

                Multimap<Attribute, AttributeModifier> attrs = ((ICurioItem) curioItem).getAttributeModifiers(slotContext, slotUuid, stack);
                if (attrs != null && !attrs.isEmpty()) {
                    slotAttributes.put(slotType, convertAttributes(attrs));
                }
            } catch (Exception e) {
                Brutality.LOGGER.debug("Item {} doesn't support curio slot {}: {}", itemId, slotType, e.getMessage());
            }
        }
    }


    static class EntityProcessor {
        // Simplified entity processing
        public static BrutalityDataFetcher.EntityData process(EntityType<?> entityType,
                                                              FakeLevel level,
                                                              Map<String, String> translations) throws IllegalAccessException {
            BrutalityDataFetcher.EntityData data = new BrutalityDataFetcher.EntityData();
            data.name = translations.getOrDefault(entityType.getDescriptionId(), entityType.getDescriptionId());

            Entity entity = entityType.create(level);
            if (entity instanceof LivingEntity living) {
                data.health = living.getMaxHealth();
                data.armor = living.getArmorValue();
                data.xpReward = living.getExperienceReward();
                data.isLiving = true;
                data.category = determineCategory(living);
                data.type = determineEntityType(living);
            }

            data.width = entityType.getWidth();
            data.height = entityType.getHeight();

            return data;
        }

        private static String determineCategory(LivingEntity entity) {
            if (entity instanceof Monster) return "hostile";
            if (entity instanceof Animal || entity instanceof AmbientCreature) return "passive";
            return "neutral";
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

        private static String determineEntityType(LivingEntity entity) {
            if (BOSSES.contains(entity.getClass())) return "boss";
            if (MINIBOSSES.contains(entity.getClass())) return "miniboss";

            return "normal";
        }
    }


    static class EffectProcessor {
        public static BrutalityDataFetcher.EffectData process(MobEffect effect, Map<String, String> translations) {
            BrutalityDataFetcher.EffectData data = new BrutalityDataFetcher.EffectData();
            data.name = translations.get(effect.getDescriptionId());
            data.category = effect.getCategory().toString().toLowerCase();
            return data;
        }
    }

    // Utility methods
    static class FileUtils {
        public static void walkJsonFiles(Path dir, Consumer<Path> consumer) {
            try (var stream = Files.walk(dir)) {
                stream.filter(p -> p.toString().endsWith(".json")).forEach(consumer);
            } catch (Exception e) {
                Brutality.LOGGER.error("Failed to walk directory: {}", dir, e);
            }
        }
    }

    static class JarUtils {
        public static JsonObject loadJsonFromJar(String jarPath, String entryPath, Gson gson) throws IOException {
            try (var jar = new JarFile(jarPath)) {
                var entry = jar.getJarEntry(entryPath);
                if (entry != null) {
                    try (var stream = jar.getInputStream(entry)) {
                        return gson.fromJson(new InputStreamReader(stream), JsonObject.class);
                    }
                }
            }
            return null;
        }
    }

    static class ItemTypeMapper {
        private static final Map<Predicate<ItemStack>, String> TYPES = Map.ofEntries(
                Map.entry(stack -> stack.getItem() instanceof SwordItem || stack.is(ItemTags.SWORDS), "sword"),
                Map.entry(stack -> stack.getItem() instanceof PickaxeItem || stack.is(ItemTags.PICKAXES), "pickaxe"),
                Map.entry(stack -> stack.getItem() instanceof AxeItem || stack.is(ItemTags.AXES), "axe"),
                Map.entry(stack -> stack.getItem() instanceof ShovelItem || stack.is(ItemTags.SHOVELS), "shovel"),
                Map.entry(stack -> stack.getItem() instanceof HoeItem || stack.is(ItemTags.HOES), "hoe"),
                Map.entry(stack -> stack.getItem() instanceof BowItem, "bow"),
                Map.entry(stack -> stack.getItem() instanceof CrossbowItem, "crossbow"),
                Map.entry(stack -> stack.getItem() instanceof ArmorItem || stack.is(ItemTags.TRIMMABLE_ARMOR), "armor")
        );

        public static String getVanillaType(ItemStack stack) {
            return TYPES.entrySet().stream()
                    .filter(entry -> entry.getKey().test(stack))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElse("generic");
        }
    }


    static class RegistryHelper {
        public static RegistryAccess.Frozen createFakeRegistryAccess() {
            // Create dimension type registry
            ResourceKey<Registry<DimensionType>> DIMENSION_TYPE_KEY =
                    ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("minecraft", "dimension_type"));

            MappedRegistry<DimensionType> dimensionTypeRegistry =
                    new MappedRegistry<>(DIMENSION_TYPE_KEY, Lifecycle.stable(), false);

            // Create dummy dimension type
            ResourceKey<DimensionType> OVERWORLD_KEY =
                    ResourceKey.create(DIMENSION_TYPE_KEY, ResourceLocation.fromNamespaceAndPath("minecraft", "overworld"));

            DimensionType dummyDimType = new DimensionType(
                    OptionalLong.of(6000L),
                    true, false, false, true,
                    1.0, true, true,
                    0, 384, 384,
                    BlockTags.INFINIBURN_OVERWORLD,
                    ResourceLocation.fromNamespaceAndPath("minecraft", "overworld"),
                    0.0f,
                    new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)
            );

            dimensionTypeRegistry.register(OVERWORLD_KEY, dummyDimType, Lifecycle.stable());

            // Create damage type registry
            ResourceKey<Registry<DamageType>> DAMAGE_TYPE_KEY =
                    ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("minecraft", "damage_type"));

            MappedRegistry<DamageType> damageTypeRegistry =
                    new MappedRegistry<>(DAMAGE_TYPE_KEY, Lifecycle.stable(), false);

            // Register all damage types from DamageTypes class
            for (Field field : DamageTypes.class.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) && field.getType().equals(ResourceKey.class)) {
                    try {
                        @SuppressWarnings("unchecked")
                        ResourceKey<DamageType> key = (ResourceKey<DamageType>) field.get(null);
                        damageTypeRegistry.register(key, new DamageType(key.location().getPath(), 0.0F), Lifecycle.stable());
                    } catch (IllegalAccessException e) {
                        Brutality.LOGGER.error("Failed to access damage type field", e);
                    }
                }
            }

            // Create registry access with both registries
            return new RegistryAccess.ImmutableRegistryAccess(
                    List.of(dimensionTypeRegistry, damageTypeRegistry)
            ).freeze();
        }
    }
}