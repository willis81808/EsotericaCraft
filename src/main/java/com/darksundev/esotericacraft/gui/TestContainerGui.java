package com.darksundev.esotericacraft.gui;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.Registrar;
import com.darksundev.esotericacraft.lists.ContainerList.TestContainer;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class TestContainerGui extends ContainerScreen<TestContainer>
{
	private static final ResourceLocation ESOTERICA_SPLASH_SCREEN = Registrar.location("/textures/gui/esotericacraft-splash-screen.png");
	
	public TestContainerGui(TestContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
		this.ySize = 133;
		this.addButton(new Button((this.width-100)/2, (this.height-20)/2 + 52, 100, 20, "Exit", (b) -> { EsotericaCraft.logger.info("Button Pressed!"); minecraft.player.closeScreen(); }));
	}
	
	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_)
	{
		this.renderBackground();
	    super.render(p_render_1_, p_render_2_, p_render_3_);
	    this.renderHoveredToolTip(p_render_1_, p_render_2_);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		//this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);
		//this.font.drawString("Welcome To EsotericaCraft!", 8.0F, (float)(this.ySize - 128 + 2), 4210752);
	}	

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		EsotericaCraft.logger.info(String.format("Mouse Position: (%d, %d)", mouseX, mouseY));
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(ESOTERICA_SPLASH_SCREEN);
		this.blit((this.width-256)/2, (this.height-256)/2 - 20, 0, 0, 256, 256);
	}

}
