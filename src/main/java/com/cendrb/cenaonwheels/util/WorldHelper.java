package com.cendrb.cenaonwheels.util;

import com.cendrb.cenaonwheels.KlidWorldSavedData;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by cendr_000 on 15.05.2016.
 */
public class WorldHelper {
    public static void spawnItemStack(World world, ItemStack itemStack, double x, double y, double z) {
        if (!world.isRemote) {
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

    public static <T extends Block> boolean isBlock(World world, BlockPos pos, Class<T> blockClass) {
        return blockClass.isAssignableFrom(world.getBlockState(pos).getBlock().getClass());
    }

    public static <T extends Block> boolean isBlock(World world, BlockPos pos, Block block) {
        return block == world.getBlockState(pos).getBlock();
    }

    public static void spawnParticles(World world, EnumParticleTypes type, double x, double y, double z, double xRadius, double yRadius, double zRadius, int count) {
        Random random = new Random();
        for (int i = count; i > 0; i--) {
            world.spawnParticle(type,
                    x + random.nextGaussian() * xRadius * getRandomPolarity(random),
                    y + random.nextGaussian() * yRadius * getRandomPolarity(random),
                    z + random.nextGaussian() * zRadius * getRandomPolarity(random),
                    0, 0.3, 0);
        }
    }

    private static void spawnKlidReleasedParticles(World world, double x, double y, double z)
    {
        spawnParticles(world, EnumParticleTypes.CLOUD, x, y, z, 3, 3, 3, 40);
    }

    public static void releaseKlidAt(World world, double x, double y, double z, int klidAmount)
    {
        KlidWorldSavedData savedData = KlidWorldSavedData.getFor(world);
        savedData.setKlidInTheAtmosphere(savedData.getKlidInTheAtmosphere() + klidAmount);
        WorldHelper.spawnKlidReleasedParticles(world, x, y, z);
    }

    private static double getRandomPolarity(Random random) {
        return random.nextBoolean() ? 1 : -1;
    }
}
