package com.cendrb.cenaonwheels.tileentity.render;

import com.cendrb.cenaonwheels.RefStrings;
import com.cendrb.cenaonwheels.init.ModItems;
import com.cendrb.cenaonwheels.tileentity.TileEntityKlidStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import org.lwjgl.opengl.GL11;

/**
 * Created by cendr_000 on 18.10.2016.
 */
public class TileEntityRendererKlidStorage extends TileEntitySpecialRenderer<TileEntityKlidStorage> {

    private static IBakedModel model;

    private static ItemStack stack = new ItemStack(ModItems.tempusShard);

    public TileEntityRendererKlidStorage() {
        super();
    }

    @Override
    public void renderTileEntityAt(TileEntityKlidStorage te, double x, double y, double z, float partialTicks, int destroyStage) {
        super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);

        if(model == null)
            model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation(RefStrings.MODID, "tempusShard"), "inventory"));

        GlStateManager.pushMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                getWorld(),
                model,
                getWorld().getBlockState(te.getPos()),
                te.getPos(),
                Tessellator.getInstance().getBuffer(),
                true);
        tessellator.draw();

        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

        GlStateManager.popMatrix();
    }
}
