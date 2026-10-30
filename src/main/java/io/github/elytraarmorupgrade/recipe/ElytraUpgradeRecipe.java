package io.github.elytraarmorupgrade.recipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.elytraarmorupgrade.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import java.util.List;
public final class ElytraUpgradeRecipe extends CustomRecipe {
    public static final MapCodec<ElytraUpgradeRecipe> CODEC=Codec.intRange(1,3).fieldOf("level").xmap(ElytraUpgradeRecipe::new,ElytraUpgradeRecipe::level);
    public static final StreamCodec<RegistryFriendlyByteBuf,ElytraUpgradeRecipe> STREAM_CODEC=StreamCodec.composite(ByteBufCodecs.VAR_INT,ElytraUpgradeRecipe::level,ElytraUpgradeRecipe::new);
    public static final RecipeSerializer<ElytraUpgradeRecipe> SERIALIZER=new RecipeSerializer<>(CODEC,STREAM_CODEC);
    private final int level;
    public ElytraUpgradeRecipe(int level){this.level=level;}
    public int level(){return level;}
    @Override public boolean matches(CraftingInput input,Level level){return !findBase(input).isEmpty();}
    @Override public ItemStack assemble(CraftingInput input){
        ItemStack base=findBase(input); if(base.isEmpty()) return ItemStack.EMPTY;
        Item target=switch(this.level){case 1->ModItems.REINFORCED_ELYTRA;case 2->ModItems.ARMORED_ELYTRA;case 3->ModItems.DEFENDER_ELYTRA;default->throw new IllegalStateException();};
        ItemStack result=new ItemStack(target);
        var attrs=result.get(DataComponents.ATTRIBUTE_MODIFIERS); var equip=result.get(DataComponents.EQUIPPABLE);
        var glider=result.get(DataComponents.GLIDER); var enchantable=result.get(DataComponents.ENCHANTABLE); var maxDamage=result.get(DataComponents.MAX_DAMAGE);
        result.applyComponents(base.getComponentsPatch());
        result.set(DataComponents.ATTRIBUTE_MODIFIERS,attrs); result.set(DataComponents.EQUIPPABLE,equip);
        result.set(DataComponents.GLIDER,glider); result.set(DataComponents.ENCHANTABLE,enchantable); result.set(DataComponents.MAX_DAMAGE,maxDamage);
        return result;
    }
    @Override public RecipeSerializer<? extends CustomRecipe> getSerializer(){return SERIALIZER;}
    private ItemStack findBase(CraftingInput input){
        List<ItemStack> stacks=input.items(); int occupied=0;
        for(ItemStack stack:stacks) if(!stack.isEmpty()){occupied++;if(stack.getCount()!=1)return ItemStack.EMPTY;}
        if(occupied!=8)return ItemStack.EMPTY;
        Item baseType=switch(level){case 1->Items.ELYTRA;case 2->ModItems.REINFORCED_ELYTRA;case 3->ModItems.ARMORED_ELYTRA;default->null;};
        if(baseType==null)return ItemStack.EMPTY;
        ItemStack base=ItemStack.EMPTY; int diamonds=0,membranes=0,scraps=0,crystals=0,stars=0,ingots=0,echo=0;
        for(ItemStack stack:stacks){if(stack.isEmpty())continue;
            if(stack.is(baseType)){if(!base.isEmpty())return ItemStack.EMPTY;base=stack;}
            else if(stack.is(Items.DIAMOND))diamonds++; else if(stack.is(Items.PHANTOM_MEMBRANE))membranes++;
            else if(stack.is(Items.NETHERITE_SCRAP))scraps++; else if(stack.is(Items.END_CRYSTAL))crystals++;
            else if(stack.is(Items.NETHER_STAR))stars++; else if(stack.is(Items.NETHERITE_INGOT))ingots++;
            else if(stack.is(Items.ECHO_SHARD))echo++; else return ItemStack.EMPTY;
        }
        if(base.isEmpty())return ItemStack.EMPTY;
        boolean valid=switch(level){
            case 1->diamonds==4&&membranes==2&&scraps==1&&crystals==0&&stars==0&&ingots==0&&echo==0;
            case 2->diamonds==2&&scraps==4&&crystals==1&&membranes==0&&stars==0&&ingots==0&&echo==0;
            case 3->stars==1&&ingots==4&&echo==2&&diamonds==0&&membranes==0&&scraps==0&&crystals==0;
            default->false;};
        return valid?base:ItemStack.EMPTY;
    }
}
