package modernmods.hilt.fluid;

import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import modernmods.hilt.fluid.texture.ClientInvertedFluidType;

import java.util.function.Consumer;

/** Fluid type adding an extra flipped texture for the in world block */
public class InvertedFluidType extends FluidType {
  public InvertedFluidType(Properties properties) {
    super(properties);
  }

  @Override
  public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
    consumer.accept(new ClientInvertedFluidType(this));
  }
}
