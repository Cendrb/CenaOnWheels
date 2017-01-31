package com.cendrb.cenaonwheels.particle;

import com.cendrb.cenaonwheels.RefStrings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by cendr on 30/01/2017.
 */
public class AschParticle extends Particle {

    private final ResourceLocation aschResource = new ResourceLocation(RefStrings.MODID, "entity/aschParticle");

    public AschParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);

        particleMaxAge = 100;

        particleAlpha = 0.99F;

        // set the texture to the flame texture, which we have previously added using TextureStitchEvent
        //   (see TextureStitcherBreathFX)
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(aschResource.toString());
        setParticleTexture(sprite);  // initialise the icon to our custom texture
    }


    /**
     * Used to control what texture and lighting is used for the EntityFX.
     * Returns 1, which means "use a texture from the blocks + items texture sheet"
     * The vanilla layers are:
     * normal particles: ignores world brightness lighting map
     * Layer 0 - uses the particles texture sheet (textures\particle\particles.png)
     * Layer 1 - uses the blocks + items texture sheet
     * lit particles: changes brightness depending on world lighting i.e. block light + sky light
     * Layer 3 - uses the blocks + items texture sheet (I think)
     */
    @Override
    public int getFXLayer() {
        return 1;
    }

    // this function is used by ParticleManager.addEffect() to determine whether depthmask writing should be on or not.
    // FlameBreathFX uses alphablending (i.e. the FX is partially transparent) but we want depthmask writing on,
    //   otherwise translucent objects (such as water) render over the top of our breath, even if the breath is in front
    //  of the water and not behind
    @Override
    public boolean isTransparent() {
        return false;
    }

    /**
     * call once per tick to update the Particle position, calculate collisions, remove when max lifetime is reached, etc
     */
    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        moveEntity(motionX, motionY, motionZ);  // simple linear motion.  You can change speed by changing motionX, motionY,

        if (this.particleMaxAge-- <= 0) {
            this.setExpired();
        }
    }

    /**
     * Render the Particle onto the screen.  For more help with the tessellator see
     * http://greyminecraftcoder.blogspot.co.at/2014/12/the-tessellator-and-worldrenderer-18.html
     * <p/>
     * You don't actually need to override this method, this is just a deobfuscated example of the vanilla, to show you
     * how it works in case you want to do something a bit unusual.
     * <p/>
     * The Particle is rendered as a two-dimensional object (Quad) in the world (three-dimensional coordinates).
     * The corners of the quad are chosen so that the Particle is drawn directly facing the viewer (or in other words,
     * so that the quad is always directly face-on to the screen.)
     * In order to manage this, it needs to know two direction vectors:
     * 1) the 3D vector direction corresponding to left-right on the viewer's screen (edgeLRdirection)
     * 2) the 3D vector direction corresponding to up-down on the viewer's screen (edgeUDdirection)
     * These two vectors are calculated by the caller.
     * For example, the top right corner of the quad on the viewer's screen is equal to:
     * the centre point of the quad (x,y,z)
     * plus the edgeLRdirection vector multiplied by half the quad's width
     * plus the edgeUDdirection vector multiplied by half the quad's height.
     * NB edgeLRdirectionY is not provided because it's always 0, i.e. the top of the viewer's screen is always directly
     * up, so moving left-right on the viewer's screen doesn't affect the y coordinate position in the world
     *
     * @param vertexBuffer
     * @param entity
     * @param partialTick
     * @param edgeLRdirectionX edgeLRdirection[XYZ] is the vector direction pointing left-right on the player's screen
     * @param edgeUDdirectionY edgeUDdirection[XYZ] is the vector direction pointing up-down on the player's screen
     * @param edgeLRdirectionZ edgeLRdirection[XYZ] is the vector direction pointing left-right on the player's screen
     * @param edgeUDdirectionX edgeUDdirection[XYZ] is the vector direction pointing up-down on the player's screen
     * @param edgeUDdirectionZ edgeUDdirection[XYZ] is the vector direction pointing up-down on the player's screen
     */
    @Override
    public void renderParticle(VertexBuffer vertexBuffer, Entity entity, float partialTick,
                               float edgeLRdirectionX, float edgeUDdirectionY, float edgeLRdirectionZ,
                               float edgeUDdirectionX, float edgeUDdirectionZ) {
        double minU = this.particleTexture.getMinU();
        double maxU = this.particleTexture.getMaxU();
        double minV = this.particleTexture.getMinV();
        double maxV = this.particleTexture.getMaxV();

        double scale = 0.1F * this.particleScale;  // vanilla scaling factor
        final double scaleLR = scale;
        final double scaleUD = scale;
        double x = this.prevPosX + (this.posX - this.prevPosX) * partialTick - interpPosX;
        double y = this.prevPosY + (this.posY - this.prevPosY) * partialTick - interpPosY;
        double z = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTick - interpPosZ;


        // "lightmap" changes the brightness of the particle depending on the local illumination (block light, sky light)
        //  in this example, it's held constant, but we still need to add it to each vertex anyway.
        int combinedBrightness = this.getBrightnessForRender(partialTick);
        int skyLightTimes16 = combinedBrightness >> 16 & 65535;
        int blockLightTimes16 = combinedBrightness & 65535;

        // the caller has already initiated rendering, using:
        //worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

        vertexBuffer.pos(x - edgeLRdirectionX * scaleLR - edgeUDdirectionX * scaleUD,
                y - edgeUDdirectionY * scaleUD,
                z - edgeLRdirectionZ * scaleLR - edgeUDdirectionZ * scaleUD)
                .tex(maxU, maxV)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(skyLightTimes16, blockLightTimes16)
                .endVertex();
        vertexBuffer.pos(x - edgeLRdirectionX * scaleLR + edgeUDdirectionX * scaleUD,
                y + edgeUDdirectionY * scaleUD,
                z - edgeLRdirectionZ * scaleLR + edgeUDdirectionZ * scaleUD)
                .tex(maxU, minV)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(skyLightTimes16, blockLightTimes16)
                .endVertex();
        vertexBuffer.pos(x + edgeLRdirectionX * scaleLR + edgeUDdirectionX * scaleUD,
                y + edgeUDdirectionY * scaleUD,
                z + edgeLRdirectionZ * scaleLR + edgeUDdirectionZ * scaleUD)
                .tex(minU, minV)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(skyLightTimes16, blockLightTimes16)
                .endVertex();
        vertexBuffer.pos(x + edgeLRdirectionX * scaleLR - edgeUDdirectionX * scaleUD,
                y - edgeUDdirectionY * scaleUD,
                z + edgeLRdirectionZ * scaleLR - edgeUDdirectionZ * scaleUD)
                .tex(minU, maxV)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(skyLightTimes16, blockLightTimes16)
                .endVertex();

    }


}