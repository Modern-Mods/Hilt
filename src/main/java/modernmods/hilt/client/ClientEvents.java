package modernmods.hilt.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.EmptyFluidHandler;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import modernmods.hilt.Hilt;
import modernmods.hilt.block.GaugeBlock;
import modernmods.hilt.client.book.BookLoader;
import modernmods.hilt.client.book.repository.FileRepository;
import modernmods.hilt.client.model.FallbackModelLoader;
import modernmods.hilt.client.model.NBTKeyModel;
import modernmods.hilt.client.model.RetexturedModel;
import modernmods.hilt.client.model.TextureColorHelper;
import modernmods.hilt.client.model.connected.ConnectedModel;
import modernmods.hilt.client.model.util.ColoredBlockModel;
import modernmods.hilt.client.model.util.HiltItemLayerModel;
import modernmods.hilt.client.model.util.ModelHelper;
import modernmods.hilt.client.render.FluidCuboid;
import modernmods.hilt.client.render.RenderItem;
import modernmods.hilt.command.client.HiltClientCommand;
import modernmods.hilt.datagen.HiltTags;
import modernmods.hilt.fluid.texture.FluidTextureManager;
import modernmods.hilt.fluid.tooltip.FluidTooltipHandler;
import modernmods.hilt.registration.HiltRegistrations;
import modernmods.hilt.registration.RegistrationHelper;
import modernmods.hilt.util.OffhandCooldownTracker;
import modernmods.hilt.util.RegistryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = Hilt.modId, value = Dist.CLIENT, bus = Bus.MOD)
public class ClientEvents {
  private static final ResourceLocation CROSSHAIR_ATTACK_INDICATOR_BACKGROUND = ResourceLocation.withDefaultNamespace("hud/crosshair_attack_indicator_background");
  private static final ResourceLocation CROSSHAIR_ATTACK_INDICATOR_PROGRESS = ResourceLocation.withDefaultNamespace("hud/crosshair_attack_indicator_progress");
  private static final ResourceLocation HOTBAR_ATTACK_INDICATOR_BACKGROUND = ResourceLocation.withDefaultNamespace("hud/hotbar_attack_indicator_background");
  private static final ResourceLocation HOTBAR_ATTACK_INDICATOR_PROGRESS = ResourceLocation.withDefaultNamespace("hud/hotbar_attack_indicator_progress");

  /** Called on construct to initiatlize things that need early entry */
  public static void onConstruct() {}

  @SuppressWarnings("ConstantConditions")
  @SubscribeEvent
  static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    if (HiltRegistrations.SIGN != null) {
      event.registerBlockEntityRenderer(HiltRegistrations.SIGN, SignRenderer::new);
    }
    if (HiltRegistrations.HANGING_SIGN != null) {
      event.registerBlockEntityRenderer(HiltRegistrations.HANGING_SIGN, HangingSignRenderer::new);
    }
  }

  @SuppressWarnings("removal")
  @SubscribeEvent
  static void registerListeners(RegisterClientReloadListenersEvent event) {
    event.registerReloadListener(ModelHelper.LISTENER);
    event.registerReloadListener(new BookLoader());
    ResourceColorManager.init(event);
    FluidTooltipHandler.init(event);
    FluidTextureManager.init(event);
    event.registerReloadListener(FluidCuboid.REGISTRY);
    event.registerReloadListener(RenderItem.REGISTRY);
    event.registerReloadListener(RenderItem.STATE_REGISTRY);
    event.registerReloadListener(TextureColorHelper.RELOAD_LISTENER);
  }

  @SubscribeEvent
  static void clientSetup(FMLClientSetupEvent event) {
    event.enqueueWork(() -> RegistrationHelper.forEachWoodType(Sheets::addWoodType));

    BookLoader.registerBook(Hilt.getResource("test"), new FileRepository(Hilt.getResource("books/test")));
    HiltClientCommand.init();
  }

  @SubscribeEvent
  static void registerModelLoaders(RegisterGeometryLoaders event) {
    // standard models - useful in resource packs for any model
    event.register(Hilt.getResource("connected"), ConnectedModel.LOADER);
    event.register(Hilt.getResource("item_layer"), HiltItemLayerModel.LOADER);
    event.register(Hilt.getResource("colored_block"), ColoredBlockModel.LOADER);
    event.register(Hilt.getResource("fallback"), FallbackModelLoader.INSTANCE);

    // NBT dynamic models - require specific data defined in the block/item to use
    event.register(Hilt.getResource("nbt_key"), NBTKeyModel.LOADER);
    event.register(Hilt.getResource("retextured"), RetexturedModel.LOADER);
  }

  @SubscribeEvent
  static void commonSetup(FMLCommonSetupEvent event) {
    NeoForge.EVENT_BUS.register(new ExtraHeartRenderHandler());
    NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, RenderGuiLayerEvent.Post.class, ClientEvents::renderOffhandAttackIndicator);
    NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, RenderGuiLayerEvent.Post.class, ClientEvents::renderGaugeTooltip);
  }

  // registered with FORGE bus
  private static void renderOffhandAttackIndicator(RenderGuiLayerEvent.Post event) {
    // must have a player, not be in spectator, and have the indicator enabled
    Minecraft minecraft = Minecraft.getInstance();
    Options settings = minecraft.options;
    AttackIndicatorStatus indicator = settings.attackIndicator().get();
    if (minecraft.player == null || minecraft.gameMode == null || minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR || indicator == AttackIndicatorStatus.OFF) {
      return;
    }

    // only care about hotbar and crosshair
    // will be true for hotbar, false for crosshair
    boolean isHotbar = VanillaGuiLayers.HOTBAR.equals(event.getName());
    if (!isHotbar && !VanillaGuiLayers.CROSSHAIR.equals(event.getName())) {
      return;
    }

    // fetch the current cooldown
    OffhandCooldownTracker tracker = OffhandCooldownTracker.get(minecraft.player);
    if (tracker == null) {
      return;
    }
    float cooldown = tracker.getCooldown();
    if (cooldown >= 1.0f) {
      return;
    }

    // show attack indicator
    GuiGraphics graphics = event.getGuiGraphics();
    switch (indicator) {
      case CROSSHAIR:
        if (!isHotbar && minecraft.options.getCameraType().isFirstPerson()) {
          if (!minecraft.getDebugOverlay().showDebugScreen() || settings.hideGui || minecraft.player.isReducedDebugInfo() || settings.reducedDebugInfo().get()) {
            // mostly cloned from vanilla attack indicator
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            int scaledHeight = minecraft.getWindow().getGuiScaledHeight();
            // integer division makes this a pain to line up, there might be a simplier version of this formula but I cannot think of one
            int y = (scaledHeight / 2) - 14 + (2 * (scaledHeight % 2));
            int x = minecraft.getWindow().getGuiScaledWidth() / 2 - 8;
            int width = (int)(cooldown * 17.0F);
            graphics.blitSprite(CROSSHAIR_ATTACK_INDICATOR_BACKGROUND, x, y, 16, 4);
            graphics.blitSprite(CROSSHAIR_ATTACK_INDICATOR_PROGRESS, 16, 4, 0, 0, x, y, width, 4);
            RenderSystem.defaultBlendFunc();
          }
        }
        break;
      case HOTBAR:
        if (isHotbar && minecraft.cameraEntity == minecraft.player) {
          int centerWidth = minecraft.getWindow().getGuiScaledWidth() / 2;
          int y = minecraft.getWindow().getGuiScaledHeight() - 20;
          int x;
          // opposite of the vanilla hand location, extra bit to offset past the offhand slot
          if (minecraft.player.getMainArm() == HumanoidArm.RIGHT) {
            x = centerWidth - 91 - 22 - 32;
          } else {
            x = centerWidth + 91 + 6 + 32;
          }
//          RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
          int l1 = (int)(cooldown * 19.0F);
          RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
          graphics.blitSprite(HOTBAR_ATTACK_INDICATOR_BACKGROUND, x, y, 18, 18);
          graphics.blitSprite(HOTBAR_ATTACK_INDICATOR_PROGRESS, 18, 18, 0, 18 - l1, x, y + 18 - l1, 18, l1);
        }
        break;
    }
  }



  /** Renders the tooltip when targeting the gauge block */
  private static void renderGaugeTooltip(RenderGuiLayerEvent.Post event) {
    if (!VanillaGuiLayers.CROSSHAIR.equals(event.getName())) {
      return;
    }
    // must not be in a screen, though chat is fine
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.screen != null && minecraft.screen.getClass() != ChatScreen.class) {
      return;
    }
    // must have a hit result
    if (minecraft.level == null || minecraft.hitResult == null || minecraft.hitResult.getType() != HitResult.Type.BLOCK) {
      return;
    }
    BlockHitResult blockHit = (BlockHitResult) minecraft.hitResult;
    BlockPos pos = blockHit.getBlockPos();

    // must be targeting a gauge
    BlockState targeted = minecraft.level.getBlockState(blockHit.getBlockPos());
    if (!targeted.is(HiltTags.Blocks.GAUGES)) {
      return;
    }
    BlockEntity gaugeContainer;
    Direction side;
    if (targeted.is(HiltTags.Blocks.ATTACHED_GAUGES)) {
      side = targeted.getValue(BlockStateProperties.FACING);
      gaugeContainer = minecraft.level.getBlockEntity(pos.relative(side.getOpposite()));
    } else {
      side = blockHit.getDirection();
      gaugeContainer = minecraft.level.getBlockEntity(pos);
    }
    // must have a block entity behind the gauge that is not blacklisted
    if (gaugeContainer == null || RegistryHelper.contains(BuiltInRegistries.BLOCK_ENTITY_TYPE, HiltTags.BlockEntities.GAUGE_BLACKLIST, gaugeContainer.getType())) {
      return;
    }
    // block entity must have a fluid handler
    IFluidHandler handler = minecraft.level.getCapability(Capabilities.FluidHandler.BLOCK, gaugeContainer.getBlockPos(), side);
    if (handler == null) {
      handler = EmptyFluidHandler.INSTANCE;
    }
    if (handler.getTanks() <= 0) {
      return;
    }
    // if the fluid is empty, just render the capacity
    FluidStack fluid = handler.getFluidInTank(0);
    List<Component> tooltip;
    if (fluid.isEmpty()) {
      tooltip = List.of(GaugeBlock.formatCapacity(handler.getTankCapacity(0)));
    } else if (RegistryHelper.contains(BuiltInRegistries.BLOCK_ENTITY_TYPE, HiltTags.BlockEntities.HIDES_GAUGE_AMOUNT, gaugeContainer.getType())) {
      // in the tag, don't show capacity
      ResourceLocation id = BuiltInRegistries.FLUID.getKey(fluid.getFluid());
      tooltip = new ArrayList<>(3);
      tooltip.add(fluid.getDisplayName());
      FluidTooltipHandler.appendAdvanced(id, tooltip);
      tooltip.add(GaugeBlock.formatCapacity(handler.getTankCapacity(0)).withStyle(ChatFormatting.GRAY));
      tooltip.add(FluidTooltipHandler.formatModName(id));
    } else {
      // render full fluid tooltip
      tooltip = FluidTooltipHandler.getFluidTooltip(fluid);
    }

    int x = minecraft.getWindow().getGuiScaledWidth() / 2;
    int y = minecraft.getWindow().getGuiScaledHeight() / 2;
    event.getGuiGraphics().renderTooltip(minecraft.font, tooltip, Optional.empty(), x, y);
  }
}
