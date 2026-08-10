package net.kamkeyke.raccooncore.datagen;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * A {@link SpecialRecipeBuilder} with support for recipe unlock advancements.
 *
 * <p>This builder behaves similarly to vanilla recipe builders such as
 * {@link ShapedRecipeBuilder}, while supporting dynamic/special crafting
 * recipes.</p>
 *
 * <p>Unlike vanilla {@link SpecialRecipeBuilder}, this builder allows
 * {@link #unlockedBy(String, CriterionTriggerInstance)} criteria to be added
 * and automatically generates the corresponding recipe advancement.</p>
 *
 * @since 2.6.0
 */
public class RaccoonSpecialRecipeBuilder extends SpecialRecipeBuilder {
    private final RecipeSerializer<?> serializer;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    public RaccoonSpecialRecipeBuilder(RecipeSerializer<?> serializer) {
        super(serializer);
        this.serializer = serializer;
    }

    /**
     * Creates a new special recipe builder.
     *
     * @param serializer the serializer for the special recipe
     * @return a new builder
     */
    public static RaccoonSpecialRecipeBuilder special(@NotNull RecipeSerializer<? extends CraftingRecipe> serializer) {
        return new RaccoonSpecialRecipeBuilder(serializer);
    }

    /**
     * Adds an advancement criterion required to unlock the recipe.
     *
     * @param criterionName the name of the criterion
     * @param criterion the criterion trigger
     * @return this builder
     */
    public RaccoonSpecialRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterion) {
        this.advancement.addCriterion(criterionName, criterion);
        return this;
    }

    /**
     * Saves the special recipe using the given recipe ID.
     *
     * <p>The recipe ID must include a namespace, for example:
     * {@code "heartpouch:heartstone_pouch_from_wool"}.</p>
     *
     * @param consumer the recipe output consumer
     * @param recipeId the recipe ID
     */
    @Override
    public void save(@NotNull Consumer<FinishedRecipe> consumer, @NotNull String recipeId) {
        save(consumer, ResourceLocation.parse(recipeId));
    }

    /**
     * Saves the special recipe using the given recipe ID.
     *
     * @param consumer the recipe output consumer
     * @param recipeId the recipe ID
     */
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation recipeId) {
        if (advancement.getCriteria().isEmpty()) throw new IllegalStateException("No way of obtaining recipe " + recipeId);

        advancement
                .parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)
                .addCriterion(
                        "has_the_recipe",
                        RecipeUnlockedTrigger.unlocked(recipeId)
                )
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(RequirementsStrategy.OR);

        consumer.accept(new Result(recipeId, serializer, advancement, recipeId.withPrefix("recipes/misc/")));
    }

    /**
     * Finished recipe data containing the generated advancement.
     */
    private static class Result extends CraftingRecipeBuilder.CraftingResult {
        private final ResourceLocation id;
        private final RecipeSerializer<?> serializer;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        private Result(ResourceLocation id, RecipeSerializer<?> serializer, Advancement.Builder advancement, ResourceLocation advancementId) {
            super(CraftingBookCategory.MISC);
            this.id = id;
            this.serializer = serializer;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            super.serializeRecipeData(json);
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return serializer;
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return id;
        }

        @Override
        @Nullable
        public JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }

        @Override
        @Nullable
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }
}