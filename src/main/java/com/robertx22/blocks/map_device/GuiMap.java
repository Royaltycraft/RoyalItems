package com.robertx22.blocks.map_device;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.robertx22.blocks.TileGui;
import com.robertx22.mmorpg.Ref;
import com.robertx22.uncommon.CLOC;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMap extends TileGui {

	// This is the resource location for the background image
	private static final ResourceLocation texture = new ResourceLocation(Ref.MODID, "textures/gui/map_device.png");
	private TileMapDevice tileEntity;

	public GuiMap(InventoryPlayer invPlayer, TileMapDevice tileInventory) {
		super(new ContainerMap(invPlayer, tileInventory));

		// Set the width and height of the gui
		xSize = 176;
		ySize = 207;

		this.tileEntity = tileInventory;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {

		// Bind the image texture
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		// Draw the image
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;
		fontRenderer.drawString(tileEntity.getDisplayName().getUnformattedText(), LABEL_XPOS, LABEL_YPOS,
				Color.darkGray.getRGB());

		final int MAP_XPOS = 90;
		final int MAP_YPOS = 15;
		String map = CLOC.word("put_map");
		fontRenderer.drawString(map, MAP_XPOS - fontRenderer.getStringWidth(map) / 2, MAP_YPOS,
				Color.darkGray.getRGB());

		final int START_XPOS = 88;
		final int START_YPOS = 85;
		String start = CLOC.word("start");
		fontRenderer.drawString(start, START_XPOS - fontRenderer.getStringWidth(start) / 2, START_YPOS,
				Color.darkGray.getRGB());

		List<String> hoveringText = new ArrayList<String>();

		// If hoveringText is not empty draw the hovering text
		if (!hoveringText.isEmpty()) {
			drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
		}
	}

	// Returns true if the given x,y coordinates are within the given rectangle
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
	}
}