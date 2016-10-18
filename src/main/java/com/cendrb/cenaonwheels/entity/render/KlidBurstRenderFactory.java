package com.cendrb.cenaonwheels.entity.render;

import com.cendrb.cenaonwheels.RefStrings;
import com.cendrb.cenaonwheels.entity.EntityKlidBurst;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelShulkerBullet;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.lwjgl.opengl.GL11;

/**
 * Created by cendr_000 on 16.10.2016.
 */
public class KlidBurstRenderFactory implements IRenderFactory<EntityKlidBurst> {

    @Override
    public Render<? super EntityKlidBurst> createRenderFor(RenderManager manager) {
        return new KlidBurstRender(manager);
    }

    public static class KlidBurstRender extends Render<EntityKlidBurst>
    {
        private static final ResourceLocation texture = new ResourceLocation(RefStrings.MODID, "textures/entity/klidBurst.png");

        private final ModelCow model = new ModelCow();

        protected KlidBurstRender(RenderManager renderManager) {
            super(renderManager);
        }

        @Override
        public void doRender(EntityKlidBurst entity, double x, double y, double z, float entityYaw, float partialTicks) {
            GlStateManager.pushMatrix();

            GlStateManager.translate(x, y + 0.5, z);
            bindEntityTexture(entity);
            GlStateManager.rotate(180, 0, 0, 1);
            model.render(entity, 0.0F, 0.0F, 0.0F, entityYaw, 0, 0.03125F);
            GlStateManager.popMatrix();

            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }

        @Override
        protected ResourceLocation getEntityTexture(EntityKlidBurst entity) {
            return texture;
        }
    }
}
