package com.robertx22.items.gearitems.offhands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShieldRenderer extends TileEntityItemStackRenderer {

  private final ModelShield modelShield = new ModelShield();

  public ShieldRenderer() {

  }

  @Override
  public void renderByItem(ItemStack stack) {
    Item item = stack.getItem();
    if (item instanceof NormalShield) {

      NormalShield shield = (NormalShield) item;

      Minecraft.getMinecraft().getTextureManager().bindTexture(shield.resource);
      GlStateManager.pushMatrix();
      GlStateManager.scale(1.0, -1.0, -1.0);
      modelShield.render();
      GlStateManager.popMatrix();

    }
  }

}
