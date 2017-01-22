package com.cendrb.cenaonwheels.tileentity.render;

import com.cendrb.cenaonwheels.RefStrings;
import com.cendrb.cenaonwheels.tileentity.TileEntityKlidInfusionPlate;
import com.cendrb.cenaonwheels.util.COWLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.omg.CORBA.CODESET_INCOMPATIBLE;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cendr on 22/01/2017.
 */
public class TileEntityRendererKlidInfusionPlate extends TileEntitySpecialRenderer<TileEntityKlidInfusionPlate> {

    private static final ResourceLocation texture = new ResourceLocation("cenaonwheels:textures/blocks/bigFront.png");

    @Override
    public void renderTileEntityAt(TileEntityKlidInfusionPlate te, double x, double y, double z, float partialTicks, int destroyStage) {
        super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);

        /*for (ItemStack stack : currentIngredients) {
            Item item = stack.getItem();
            if (itemsRenderCache.containsKey(item)) {
                modelsToRender.add(itemsRenderCache.get(item));
            }
            else {
                IBakedModel bakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(
                        new ModelResourceLocation(Item.REGISTRY.getNameForObject(item), "inventory"));
                itemsRenderCache.put(item, bakedModel);
                modelsToRender.add(bakedModel);
            }
        }*/

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1F, 1F, 1F, 1F);

        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);
        GlStateManager.enableRescaleNormal();

        float scale = 0.5F;

        ArrayList<ItemStack> currentIngredients = te.getCurrentIngredients();
        final float modifier = 6F;
        final float rotationModifier = 0.25F;
        final float radiusBase = 1.2F;
        final float radiusMod = 0.1F;

        double ticks = 1;

        float offsetPerItem = 360 / currentIngredients.size();
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.05F, -0.5F, 0F);
        GlStateManager.scale(scale, scale, scale);

        int index = 0;
        for (ItemStack itemStack : currentIngredients) {
            float offset = offsetPerItem * index;
            float deg = (int) (ticks / rotationModifier % 360F + offset);
            float rad = deg * (float) Math.PI / 180F;
            float radiusX = (float) (radiusBase + radiusMod * Math.sin(ticks / modifier));
            float radiusZ = (float) (radiusBase + radiusMod * Math.cos(ticks / modifier));
            float xItem =  (float) (radiusX * Math.cos(rad));
            float zItem = (float) (radiusZ * Math.sin(rad));
            float yItem = (float) Math.cos((ticks + 50 * index) / 5F) / 10F;

            GlStateManager.pushMatrix();
            GlStateManager.translate(xItem, yItem, zItem);
            float xRotate = (float) Math.sin(ticks * rotationModifier) / 2F;
            float yRotate = (float) Math.max(0.6F, Math.sin(ticks * 0.1F) / 2F + 0.5F);
            float zRotate = (float) Math.cos(ticks * rotationModifier) / 2F;

            scale /= 2F;
            GlStateManager.translate(scale, scale, scale);
            GlStateManager.rotate(deg, xRotate, yRotate, zRotate);
            GlStateManager.translate(-scale, -scale, -scale);
            scale *= 2F;

            GlStateManager.color(1F, 1F, 1F, 1F);

            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Minecraft.getMinecraft().getRenderItem().renderItem(itemStack, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();

            index++;
        }

        GlStateManager.popMatrix();
        GlStateManager.popMatrix();

        /*
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = tessellator.getBuffer();
        bindTexture(texture);

        GL11.glDisable(GL11.GL_LIGHTING);     // turn off "item" lighting (face brightness depends on which direction it is facing)
        GL11.glDisable(GL11.GL_BLEND);        // turn off "alpha" transparency blending
        GL11.glDepthMask(true);          // gem is hidden behind other objects

        GlStateManager.color(1, 0.2f, 0.2f);

        vertexBuffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(0,2,0).tex(0,0.7).endVertex();
        vertexBuffer.pos(-5,2,0).tex(0.2,0.8).endVertex();
        vertexBuffer.pos(-2.5,-0.5,5).tex(0.4,0.1).endVertex();
        vertexBuffer.pos(0,-3,0).tex(0.6,0.3).endVertex();
        vertexBuffer.pos(-5,-3,0).tex(0,0.6).endVertex();
        tessellator.draw();

        int index = 0;
        for (ItemStack stack : currentIngredients) {
            GlStateManager.translate(x, y + 1 + index, z);
            index++;

            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

        }*/

        /*
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glColor3f(0, 1, 0);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x+1, y, z);
        GL11.glVertex3d(x+2, y+1, z);
        GL11.glVertex3d(x, y, z+2);
        GL11.glEnd();
        GL11.glPopAttrib();
        GL11.glPopMatrix();
        */

    }
}
