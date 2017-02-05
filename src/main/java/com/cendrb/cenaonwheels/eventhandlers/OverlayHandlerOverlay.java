package com.cendrb.cenaonwheels.eventhandlers;

import com.cendrb.cenaonwheels.KlidWorldSavedData;
import com.cendrb.cenaonwheels.RefStrings;
import com.cendrb.cenaonwheels.init.ModItems;
import com.cendrb.cenaonwheels.util.ItemHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

/**
 * Created by cendr on 31/01/2017.
 */
public class OverlayHandlerOverlay extends Gui {

    private final static ResourceLocation klidDetectorScale = new ResourceLocation(RefStrings.MODID, "textures/gui/klidDetectorScale.png");

    private final static int BAR_WIDTH = 128;
    private final static int BAR_HEIGHT = 32;
    private final static int BAR_SPACING_ABOVE_EXP_BAR = 30;  // pixels between the BAR and the Experience Bar below it

    private Minecraft minecraftInstance;

    public OverlayHandlerOverlay(Minecraft minecraftInstance) {
        this.minecraftInstance = minecraftInstance;
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().thePlayer;
        if (entityPlayerSP == null) return;  // just in case

        // look for the klidDetector in the hotbar
        if (ItemHelper.isItem(entityPlayerSP.getHeldItem(EnumHand.MAIN_HAND), ModItems.klidDetector) ||
                ItemHelper.isItem(entityPlayerSP.getHeldItem(EnumHand.OFF_HAND), ModItems.klidDetector)) {
            renderStatusBar(event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
        }
    }

    public void renderStatusBar(int screenWidth, int screenHeight) {
        // These are the variables that contain world and player information
        World world = minecraftInstance.theWorld;

        // This object draws text using the Minecraft font
        FontRenderer fontRenderer = minecraftInstance.fontRendererObj;

        // Saving the current state of OpenGL so that I can restore it when I'm done
        GL11.glPushMatrix();

        // Set the rendering color to white
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // Magically bind the texture
        minecraftInstance.renderEngine.bindTexture(klidDetectorScale);

        // we will draw the status bar just above the hotbar obtained by inspecting the vanilla hotbar rendering code
        final int vanillaExpLeftX = screenWidth / 2 - 91; // leftmost edge of the experience bar
        final int vanillaExpTopY = screenHeight - 32 + 3;  // top of the experience bar

        // Shift our rendering origin to just above the experience bar
        // The top left corner of the screen is x=0, y=0
        GL11.glTranslatef(vanillaExpLeftX, vanillaExpTopY - BAR_SPACING_ABOVE_EXP_BAR - BAR_HEIGHT, 0);

        int currentAtmosphericKlid = KlidWorldSavedData.getFor(world).getKlidInTheAtmosphere();
        int klidSlowThreshold = KlidWorldSavedData.KLID_TIME_SLOW_THRESHOLD;

        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 1);
        drawTexturedModalRect(0, 0, 0, 0, BAR_WIDTH, 2);
        drawTexturedModalRect(0, 0, 0, 0, (int)(BAR_WIDTH * ((float)currentAtmosphericKlid / klidSlowThreshold)), BAR_HEIGHT);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 1);
        String currentAtmosphericKlidString = String.valueOf(currentAtmosphericKlid);
        fontRenderer.drawString(currentAtmosphericKlidString, -fontRenderer.getStringWidth(currentAtmosphericKlidString), 1, 0xFFFFFF);
        GL11.glPopMatrix();

        GL11.glPopMatrix();
    }
}
