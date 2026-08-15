package modernmods.hilt.datagen;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.ApiStatus.Internal;
import modernmods.hilt.Hilt;

import java.util.concurrent.CompletableFuture;

import static modernmods.hilt.datagen.HiltTags.Blocks.ATTACHED_GAUGES;
import static modernmods.hilt.datagen.HiltTags.Blocks.GAUGES;
import static modernmods.hilt.datagen.HiltTags.Blocks.GAUGE_TANKS;

/** Provider for tags added by hilt, generally not useful for other mods */
@Internal
public class HiltBlockTagProvider extends BlockTagsProvider {
  public HiltBlockTagProvider(PackOutput output, CompletableFuture<Provider> holders, ExistingFileHelper existingFileHelper) {
    super(output, holders,  Hilt.modId, existingFileHelper);
  }

  @Override
  protected void addTags(Provider pProvider) {
    this.tag(GAUGES).addOptionalTag(ATTACHED_GAUGES.location()).addOptionalTag(GAUGE_TANKS.location());
  }

  @Override
  public String getName() {
    return "Hilt Block Tag Provider";
  }
}
