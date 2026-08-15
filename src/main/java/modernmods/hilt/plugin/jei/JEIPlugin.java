package modernmods.hilt.plugin.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import modernmods.hilt.Hilt;
import modernmods.hilt.client.screen.MultiModuleScreen;
import modernmods.hilt.inventory.MultiModuleContainerMenu;
import modernmods.hilt.plugin.jei.entity.EntityIngredientHelper;
import modernmods.hilt.plugin.jei.entity.EntityIngredientRenderer;
import modernmods.hilt.recipe.crafting.ShapedRetexturedRecipe;

import java.util.Collections;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
  @Override
  public ResourceLocation getPluginUid() {
    return Hilt.getResource("jei");
  }

  @Override
  public void registerIngredients(IModIngredientRegistration registration) {
    registration.register(HiltJEIConstants.ENTITY_TYPE, Collections.emptyList(), new EntityIngredientHelper(), new EntityIngredientRenderer(16));
  }

  @Override
  public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registry) {
    // TODO 1.21: re-enable when JEI exposes the replacement for addCategoryExtension.
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void registerGuiHandlers(IGuiHandlerRegistration registration) {
    registration.addGuiContainerHandler(MultiModuleScreen.class, new MultiModuleContainerHandler());
  }

  private static class MultiModuleContainerHandler<C extends MultiModuleContainerMenu<?>> implements IGuiContainerHandler<MultiModuleScreen<C>> {
    @Override
    public List<Rect2i> getGuiExtraAreas(MultiModuleScreen<C> guiContainer) {
      return guiContainer.getModuleAreas();
    }
  }
}
