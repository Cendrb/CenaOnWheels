package com.cendrb.cenaonwheels.entity;

import com.cendrb.cenaonwheels.util.COWLogger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by cendr_000 on 16.10.2016.
 */
public class EntityKlidBurst extends Entity {

    private BlockPos target;

    public EntityKlidBurst(World worldIn) {
        super(worldIn);
        this.setSize(0.3F, 0.3F);
        this.noClip = true;
    }

    @Override
    protected void entityInit() {

    }

    @Override
    public void onUpdate() {
        if (target != null) {
            //COWLogger.logDebug(COWLogger.formatBlockPos(target));
            double xDiff = (target.getX() + 0.5) - posX;
            double yDiff = (target.getY() + 0.5) - posY;
            double zDiff = (target.getZ() + 0.5) - posZ;
            motionX = xDiff / 15.0;
            motionY = yDiff / 15.0;
            motionZ = zDiff / 15.0;

            // yeah, you need to move the entity yourself
            setPosition(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            // so that it looks a bit better
            ProjectileHelper.rotateTowardsMovement(this, 1f);

            if(xDiff < 0.3 && yDiff <0.3 && zDiff < 0.3)
                setDead();
        }

        if(worldObj.isRemote)
            worldObj.spawnParticle(EnumParticleTypes.CRIT_MAGIC, posX, posY, posZ, -motionX, -motionY, -motionZ);

        super.onUpdate();
    }

    /**
     * Checks if the entity is in range to render.
     */
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 16384.0D;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float partialTicks) {
        return 1.0F;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float partialTicks) {
        return 15728880;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        target = BlockPos.fromLong(compound.getLong("target"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setLong("target", target.toLong());
    }

    public void setTarget(BlockPos target) {
        this.target = target;
    }
}
