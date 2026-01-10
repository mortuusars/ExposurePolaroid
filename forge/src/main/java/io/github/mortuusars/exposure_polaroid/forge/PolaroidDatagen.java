package io.github.mortuusars.exposure_polaroid.forge;

import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PolaroidDatagen {
    public static void gather(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        dataGenerator.addProvider(true,new PolaroidAdvancements(packOutput,lookupProvider,existingFileHelper,List.of(new PolaroidAdvancements.Adventure())));
        dataGenerator.addProvider(true,new PolaroidRecipes(packOutput));
    }

    static class PolaroidAdvancements extends ForgeAdvancementProvider {
        public PolaroidAdvancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper, List<AdvancementGenerator> subProviders) {
            super(output, registries, existingFileHelper, subProviders);
        }

        static class Adventure implements AdvancementGenerator {

            @Override
            public void generate(HolderLookup.Provider arg, Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {
                Advancement advancement = Advancement.Builder.advancement()
                        .parent(Exposure.resource("adventure/exposure"))
                        .display(new DisplayInfo(ExposurePolaroid.Items.INSTANT_CAMERA.get().getDefaultInstance(),
                                Component.translatable("advancement.exposure_polaroid.instant_classic.title"),
                                Component.translatable("advancement.exposure_polaroid.instant_classic.description"),null, FrameType.TASK,true,true,false))
                        .addCriterion("get_instant_camera", InventoryChangeTrigger.TriggerInstance.hasItems(ExposurePolaroid.Items.INSTANT_CAMERA.get()))
                        .save(consumer,
                        ExposurePolaroid.resource("adventure/instant_classic"),existingFileHelper);
            }
        }

    }


    static class PolaroidRecipes extends RecipeProvider {
        public PolaroidRecipes(PackOutput output) {
            super(output);
        }

        @Override
        protected void buildRecipes(Consumer<FinishedRecipe> writer) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ExposurePolaroid.Items.INSTANT_CAMERA.get())
                    .define('B', ItemTags.BUTTONS)
                    .define('G', Items.GLASS_PANE)
                    .define('I', Items.IRON_INGOT)
                    .define('F', Exposure.Tags.Items.FLASHES)
                    .define('C', Items.CRAFTING_TABLE)
                    .pattern("III")
                    .pattern("BGF")
                    .pattern("ICI")
                    .unlockedBy(getHasName(Items.IRON_INGOT),has(Items.IRON_INGOT))
                    .save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ExposurePolaroid.Items.INSTANT_BLACK_AND_WHITE_SLIDE.get())
                    .define('B', Items.BONE_MEAL)
                    .define('K', Items.BLACK_DYE)
                    .define('P', Items.PAPER)
                    .pattern("BKB")
                    .pattern("PPP")
                    .unlockedBy(getHasName(Items.BLACK_DYE),has(Items.BLACK_DYE))
                    .save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ExposurePolaroid.Items.HIGH_SENSITIVITY_INSTANT_BLACK_AND_WHITE_SLIDE.get())
                    .define('B', Items.BONE_MEAL)
                    .define('K', Items.BLACK_DYE)
                    .define('C', Items.PRISMARINE_CRYSTALS)
                    .define('P', Items.PAPER)
                    .pattern("BKC")
                    .pattern("PPP")
                    .unlockedBy(getHasName(Items.BLACK_DYE),has(Items.BLACK_DYE))
                    .save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ExposurePolaroid.Items.INSTANT_COLOR_SLIDE.get())
                    .define('L', Items.LAPIS_LAZULI)
                    .define('K', Items.BLACK_DYE)
                    .define('C', Items.CYAN_DYE)
                    .define('M', Items.MAGENTA_DYE)
                    .define('Y', Items.YELLOW_DYE)
                    .define('P', Items.PAPER)
                    .pattern("LKL")
                    .pattern("CMY")
                    .pattern("PPP")
                    .unlockedBy(getHasName(Items.BLACK_DYE),has(Items.BLACK_DYE))
                    .save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ExposurePolaroid.Items.HIGH_SENSITIVITY_INSTANT_COLOR_SLIDE.get())
                    .define('L', Items.LAPIS_LAZULI)
                    .define('K', Items.BLACK_DYE)
                    .define('C', Items.CYAN_DYE)
                    .define('M', Items.MAGENTA_DYE)
                    .define('Y', Items.YELLOW_DYE)
                    .define('P', Items.PAPER)
                    .define('R', Items.PRISMARINE_CRYSTALS)
                    .pattern("LKR")
                    .pattern("CMY")
                    .pattern("PPP")
                    .unlockedBy(getHasName(Items.BLACK_DYE),has(Items.BLACK_DYE))
                    .save(writer);
        }
    }
}
