package io.github.elytraarmorupgrade;
import io.github.elytraarmorupgrade.item.ModItems;
import io.github.elytraarmorupgrade.recipe.ElytraUpgradeRecipe;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public final class ElytraArmorUpgrade implements ModInitializer {
    public static final String MOD_ID = "elytra_armor_upgrade";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Identifier id(String path) { return Identifier.fromNamespaceAndPath(MOD_ID, path); }
    @Override public void onInitialize() {
        ModItems.initialize();
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id("elytra_upgrade"), ElytraUpgradeRecipe.SERIALIZER);
        LOGGER.info("Бронированные элитры загружены.");
    }
}
