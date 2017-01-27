package com.cendrb.cenaonwheels.entity;

import com.cendrb.cenaonwheels.util.WorldHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.world.World;

/**
 * Created by cendr_000 on 4. 7. 2015.
 */
public class EntityAkbarCow extends EntityCow {

    public EntityAkbarCow(World world) {
        super(world);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10000.0D);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (onGround) {
            WorldHelper.createHeimExplosion(worldObj, this, posX, posY - 3, posZ, 15.0F);
            setDead();
        }
    }
}
