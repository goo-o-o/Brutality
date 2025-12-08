package net.goo.brutality.util.helpers;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.BrutalityCategories;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public final class SafeCategoryHelper {
    
    private static final Map<Class<?>, String> CATEGORY_CACHE = new ConcurrentHashMap<>();
    
    private SafeCategoryHelper() {
        // Utility class
    }

    public static String getSafeCategoryString(Object item) {
        if (item == null) {
            return "generic";
        }
        
        String cached = CATEGORY_CACHE.get(item.getClass());
        if (cached != null) {
            return cached;
        }
        
        try {
            var method = item.getClass().getMethod("category");
            var category = method.invoke(item);
            
            if (category instanceof BrutalityCategories) {
                String result = category.toString().toLowerCase(java.util.Locale.ROOT);
                CATEGORY_CACHE.put(item.getClass(), result);
                return result;
            }
        } catch (Exception e) {
            Brutality.LOGGER.error("Failed to get category for {}: {}", item.getClass().getSimpleName(), e.getMessage());
        }

        // Default
        String fallback = "generic";
        CATEGORY_CACHE.put(item.getClass(), fallback);
        return fallback;
    }

    public static void preloadCategories() {
        try {
            Class<?> itemTypeClass = Class.forName("net.goo.brutality.item.BrutalityCategories$ItemType");
            Object[] itemTypeValues = (Object[]) itemTypeClass.getMethod("values").invoke(null);
            
            Class<?> attackTypeClass = Class.forName("net.goo.brutality.item.BrutalityCategories$AttackType");
            Object[] attackTypeValues = (Object[]) attackTypeClass.getMethod("values").invoke(null);
            
            Class<?> curioTypeClass = Class.forName("net.goo.brutality.item.BrutalityCategories$CurioType");
            Object[] curioTypeValues = (Object[]) curioTypeClass.getMethod("values").invoke(null);

            Brutality.LOGGER.info("[Brutality] Categories preloaded successfully: {} item types, {} attack types, {} curio types", itemTypeValues.length, attackTypeValues.length, curioTypeValues.length);
                
        } catch (Exception e) {
            Brutality.LOGGER.error("[Brutality] CRITICAL: Failed to preload BrutalityCategories!");
            e.printStackTrace();
            throw new RuntimeException("Category preloading failed", e);
        }
    }
}