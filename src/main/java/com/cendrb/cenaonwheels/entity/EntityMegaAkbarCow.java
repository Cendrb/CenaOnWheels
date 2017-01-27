package com.cendrb.cenaonwheels.entity;

import com.cendrb.cenaonwheels.util.WorldHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.world.World;

/**
 * Created by cendr_000 on 4. 7. 2015.
 */
public class EntityMegaAkbarCow extends EntityCow {

    public EntityMegaAkbarCow(World world) {
        super(world);
        setSize(10, 10);
        setScale(10);
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
            WorldHelper.createHeimExplosion(worldObj, this, posX, posY - 5, posZ, 50.0F);
            setDead();
        }
    }
}
