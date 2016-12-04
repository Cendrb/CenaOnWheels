package com.cendrb.cenaonwheels;

import com.cendrb.cenaonwheels.init.ModBlocks;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by cendr_000 on 22.10.2016.
 */
public class OreGenerator implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.getDimension()) {
            case -1:
                generateNether(random, chunkX * 16, chunkZ * 16, world);
                break;
            case 0:
                generateOverworld(random, chunkX * 16, chunkZ * 16, world);
                break;
            case 1:
                generateEnd(random, chunkX * 16, chunkZ * 16, world);
                break;
        }
    }

    private void addOre(IBlockState block, Random random, World world, int posX, int posZ, int minY, int maxY, int minVein, int maxVein, int spawnChance) {
        for (int i = 0; i < spawnChance; i++) {
            int defaultChunkSize = 16;

            int xPos = posX + random.nextInt(defaultChunkSize);

            int zPos = posZ + random.nextInt(defaultChunkSize);

            int yPos = minY + random.nextInt(maxY - minY);

            new WorldGenMinable(block, (minVein + random.nextInt(maxVein - minVein))).generate(world, random, new BlockPos(xPos, yPos, zPos));
        }
    }


    private void generateEnd(Random random, int worldX, int chunkZ, World world) {

    }

    private void generateOverworld(Random random, int worldX, int worldZ, World world) {
        addOre(ModBlocks.oreTempus.getDefaultState(), random, world, worldX, worldZ, 1, 100, 4, 12, 8);
    }

    private void generateNether(Random random, int worldX, int worldZ, World world) {
    }
}
