package modernmods.hilt.recipe.data;

import lombok.RequiredArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import modernmods.hilt.data.loadable.Loadable;
import modernmods.hilt.data.loadable.Loadables;
import modernmods.hilt.data.loadable.primitive.IntLoadable;
import modernmods.hilt.data.loadable.record.RecordLoadable;
import modernmods.hilt.recipe.ingredient.FluidIngredient;

import java.util.List;

/** Datagen fluid ingredient to create an ingredient matching a fluid from another mod, should not be used outside datagen */
@RequiredArgsConstructor(staticName = "of")
public class FluidNameIngredient extends FluidIngredient {
  private static final RecordLoadable<FluidNameIngredient> LOADABLE = RecordLoadable.create(
    Loadables.RESOURCE_LOCATION.requiredField("fluid", i -> i.fluidName),
    IntLoadable.FROM_ONE.requiredField("amount", i -> i.amount),
    FluidNameIngredient::new);

  private final ResourceLocation fluidName;
  private final int amount;

  @Override
  public Loadable<FluidNameIngredient> loadable() {
    return LOADABLE;
  }

  @Override
  public boolean test(Fluid fluid) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getAmount(Fluid fluid) {
    return amount;
  }

  @Override
  protected List<FluidStack> getAllFluids() {
    throw new UnsupportedOperationException();
  }
}
