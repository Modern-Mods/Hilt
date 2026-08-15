package modernmods.hilt.client.screen.book;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import modernmods.hilt.client.screen.book.element.BookElement;

public interface ILayerRenderFunction {
  void draw(BookElement element, GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, Font fontRenderer);
}
