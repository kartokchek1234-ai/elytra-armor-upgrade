package io.github.elytraarmorupgrade.item;
import io.github.elytraarmorupgrade.ElytraArmorUpgrade;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import java.util.Map;
public final class ModItems {
    public static final int ELYTRA_DURABILITY = 432;
    private static final ResourceKey<EquipmentAsset> REINFORCED_ASSET = assetKey("reinforced_elytra");
    private static final ResourceKey<EquipmentAsset> ARMORED_ASSET = assetKey("armored_elytra");
    private static final ResourceKey<EquipmentAsset> DEFENDER_ASSET = assetKey("defender_elytra");
    private static final ArmorMaterial REINFORCED_MATERIAL = armorMaterial(3, 0.0F, 0.0F, REINFORCED_ASSET);
    private static final ArmorMaterial ARMORED_MATERIAL = armorMaterial(5, 1.0F, 0.0F, ARMORED_ASSET);
    private static final ArmorMaterial DEFENDER_MATERIAL = armorMaterial(8, 3.0F, 0.1F, DEFENDER_ASSET);
    // Avoid creating an ItemStack before item components are bound during startup.
    private static final Equippable ELYTRA_EQUIPPABLE = Equippable.builder(EquipmentSlot.CHEST)
            .setEquipSound(SoundEvents.ARMOR_EQUIP_ELYTRA).setDispensable(true).setSwappable(true).setDamageOnHurt(true).build();
    public static final Item REINFORCED_ELYTRA = register("reinforced_elytra", properties(REINFORCED_MATERIAL,"item.elytra_armor_upgrade.reinforced_elytra.lore",4));
    public static final Item ARMORED_ELYTRA = register("armored_elytra", properties(ARMORED_MATERIAL,"item.elytra_armor_upgrade.armored_elytra.lore",4));
    public static final Item DEFENDER_ELYTRA = register("defender_elytra", properties(DEFENDER_MATERIAL,"item.elytra_armor_upgrade.defender_elytra.lore",5));
    private ModItems() {}
    private static ResourceKey<EquipmentAsset> assetKey(String path) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, ElytraArmorUpgrade.id(path));
    }
    private static ArmorMaterial armorMaterial(int defense,float toughness,float knockback,ResourceKey<EquipmentAsset> assetKey) {
        return new ArmorMaterial(15,Map.of(ArmorType.CHESTPLATE,defense),15,SoundEvents.ARMOR_EQUIP_NETHERITE,toughness,knockback,ItemTags.REPAIRS_NETHERITE_ARMOR,assetKey);
    }
    private static Item.Properties properties(ArmorMaterial material,String loreKey,int count) {
        var lines = new java.util.ArrayList<Component>(count);
        for(int i=1;i<=count;i++) lines.add(Component.translatable(loreKey+"."+i));
        return new Item.Properties().humanoidArmor(material,ArmorType.CHESTPLATE).durability(ELYTRA_DURABILITY)
                .component(DataComponents.EQUIPPABLE,ELYTRA_EQUIPPABLE).component(DataComponents.GLIDER,Unit.INSTANCE)
                .component(DataComponents.LORE,new ItemLore(lines));
    }
    private static Item register(String path,Item.Properties properties) {
        ResourceKey<Item> key=ResourceKey.create(Registries.ITEM,ElytraArmorUpgrade.id(path));
        return Registry.register(BuiltInRegistries.ITEM,key,new Item(properties.setId(key)));
    }
    public static void initialize() {}
}
