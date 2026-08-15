package modernmods.hilt.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import modernmods.hilt.Hilt;

/**
 * Class for render types defined by Hilt
 */
public class HiltRenderTypes extends RenderType {

  private HiltRenderTypes(String name, VertexFormat format, Mode mode, int bufferSize, boolean useDelegate, boolean needsSorting, Runnable setupTaskIn, Runnable clearTaskIn) {
    super(name, format, mode, bufferSize, useDelegate, needsSorting, setupTaskIn, clearTaskIn);
  }

  /** Extension of {@link RenderType#POSITION_COLOR_TEX_LIGHTMAP_SHADER} with fog information based on {@link RenderType#ENTITY_TRANSLUCENT_CULL} */
  public static final RenderStateShard.ShaderStateShard FLUID_SHADER = new RenderStateShard.ShaderStateShard(HiltShaders::getConfiguredFluidShader);

  /**
   * Render type used for the fluid renderer.
   * TODO 1.21: can we replace this with {@link RenderType#ENTITY_TRANSLUCENT_CULL}? Would require including normals in our vertex format.
   */
  public static final RenderType FLUID = create(
    Hilt.modId + ":block_render_type",
    DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true,
    RenderType.CompositeState.builder()
      .setLightmapState(LIGHTMAP)
      .setShaderState(FLUID_SHADER)
      .setTextureState(BLOCK_SHEET_MIPPED)
      .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
      .createCompositeState(false));

  /**
   * Render type used for the structure renderer
   */
  public static final VertexFormat BLOCK_WITH_OVERLAY = VertexFormat.builder()
    .add("Position", VertexFormatElement.POSITION)
    .add("Color", VertexFormatElement.COLOR)
    .add("UV0", VertexFormatElement.UV0)
    .add("UV1", VertexFormatElement.UV1)
    .add("UV2", VertexFormatElement.UV2)
    .add("Normal", VertexFormatElement.NORMAL)
    .padding(1)
    .build();

  public static final RenderType TRANSLUCENT_FULLBRIGHT = create(
    Hilt.modId + ":translucent_fullbright",
    BLOCK_WITH_OVERLAY, Mode.QUADS, 256, false, false,
    RenderType.CompositeState.builder()
      .setShaderState(new RenderStateShard.ShaderStateShard(HiltShaders::getBlockFullBrightShader))
      .setLightmapState(new RenderStateShard.LightmapStateShard(false))
      .setOverlayState(OVERLAY)
      .setTextureState(BLOCK_SHEET_MIPPED)
      .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
      .createCompositeState(false));
}
