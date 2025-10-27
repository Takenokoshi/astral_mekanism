package astral_mekanism.recipes.ingredient;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class ArrayItemStackIngredient implements InputIngredient<ItemStack[]> {

    public final ItemStackIngredient[] ingredients;

    public ArrayItemStackIngredient(ItemStackIngredient[] ingredients) {
        if (ingredients.length > Byte.MAX_VALUE) {
            ingredients = Arrays.copyOf(ingredients, Byte.MAX_VALUE);
        }
        this.ingredients = ingredients;
    }

    public static ArrayItemStackIngredient create(ItemStack[] instances) {
        ItemStackIngredient[] ingredients = new ItemStackIngredient[instances.length];
        for (int i = 0; i < instances.length; i++) {
            ingredients[i] = IngredientCreatorAccess.item().from(instances[i]);
        }
        return new ArrayItemStackIngredient(ingredients);
    }

    public static ArrayItemStackIngredient deserialize(JsonObject json) {
        JsonArray array = json.getAsJsonArray("ingredients");
        ItemStackIngredient[] ingredients = new ItemStackIngredient[array.size()];

        for (int i = 0; i < array.size(); i++) {
            JsonObject ingredientJson = array.get(i).getAsJsonObject();
            ingredients[i] = IngredientCreatorAccess.item().deserialize(ingredientJson);
        }

        return new ArrayItemStackIngredient(ingredients);
    }

    public static ArrayItemStackIngredient readFromBuf(FriendlyByteBuf buffer) {
        byte length = buffer.readByte();
        ItemStackIngredient[] ingredients = new ItemStackIngredient[length];
        for (int i = 0; i < length; i++) {
            ingredients[i] = IngredientCreatorAccess.item().read(buffer);
        }
        return new ArrayItemStackIngredient(ingredients);
    }

    @Override
    public boolean test(ItemStack[] t) {
        boolean result = ingredients.length == t.length;
        int i = 0;
        while (result && i < ingredients.length) {
            result &= ingredients[i].test(t[i]);
            i++;
        }
        return result;
    }

    @Override
    public ItemStack[] getMatchingInstance(ItemStack[] inputs) {
        if (ingredients.length != inputs.length) {
            return null;
        }
        ItemStack[] result = new ItemStack[ingredients.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = ingredients[i].getMatchingInstance(inputs[i]);
            if (result[i] == null) {
                return null;
            }
        }
        return result;
    }

    public boolean hasNoMatchingInstances() {
        boolean result = false;
        int i = 0;
        while (!result && i < ingredients.length) {
            result |= ingredients[i].hasNoMatchingInstances();
            i++;
        }
        return result;
    }

    @Override
    public long getNeededAmount(ItemStack[] inputs) {
        return getMatchingInstance(inputs) == null ? 0 : 1;
    }

    @Override
    public List<ItemStack[]> getRepresentations() {
        ItemStack[] resultStacks = new ItemStack[ingredients.length];
        for (int i = 0; i < resultStacks.length; i++) {
            List<ItemStack> stacks = ingredients[i].getRepresentations();
            if (stacks.isEmpty()) {
                return List.of();
            }
            resultStacks[i] = ingredients[i].getRepresentations().get(0);
        }
        return List.<ItemStack[]>of(resultStacks);
    }

    @Override
    public JsonElement serialize() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "astral_mekanism:array_item_ingredient");
        JsonArray array = new JsonArray();
        for (ItemStackIngredient ingredient : ingredients) {
            array.add(ingredient.serialize());
        }
        obj.add("ingredients", array);
        return obj;
    }

    @Override
    public boolean testType(@NotNull ItemStack[] stacks) {
        if (stacks.length != ingredients.length) {
            return false;
        }
        for (int i = 0; i < ingredients.length; i++) {
            if (!ingredients[i].testType(stacks[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeByte(ingredients.length);
        for (ItemStackIngredient ingredient : ingredients) {
            ingredient.write(buffer);
        }
    }

    public void logMissingTags() {
        for (ItemStackIngredient ingredient : ingredients) {
            ingredient.logMissingTags();
        }
    }

}
