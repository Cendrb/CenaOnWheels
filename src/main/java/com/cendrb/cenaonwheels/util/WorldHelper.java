package com.cendrb.cenaonwheels.util;

import com.cendrb.cenaonwheels.block.BlockVisStorageCasing;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by cendr_000 on 15.05.2016.
 */
public class WorldHelper {
    public static void spawnItemStack(World world, ItemStack itemStack, double x, double y, double z)
    {
        if(!world.isRemote) {
            EntityItem entityItem = new EntityItem(world, x, y, z, itemStack);

            // Apply some random motion to the item
            float multiplier = 0.1f;
            float motionX = world.rand.nextFloat() - 0.5f;
            float motionY = world.rand.nextFloat() - 0.5f;
            float motionZ = world.rand.nextFloat() - 0.5f;

            entityItem.motionX = motionX * multiplier;
            entityItem.motionY = motionY * multiplier;
            entityItem.motionZ = motionZ * multiplier;

            world.spawnEntityInWorld(entityItem);
        }
    }

    public static <T extends Block> boolean isBlock(World world, BlockPos pos, Class<T> blockClass)
    {
        return blockClass.isInstance(world.getBlockState(pos).getBlock());
    }
}
