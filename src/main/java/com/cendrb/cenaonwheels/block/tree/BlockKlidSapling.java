package com.cendrb.cenaonwheels.block.tree;

import com.cendrb.cenaonwheels.block.BlockBase;
import com.cendrb.cenaonwheels.init.ModBlocks;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * Created by cendr on 25/01/2017.
 */
public class BlockKlidSapling extends BlockBase implements IGrowable {

    public static final PropertyEnum<KlidWoodType> TYPE = PropertyEnum.<KlidWoodType>create("type", KlidWoodType.class);
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

    public BlockKlidSapling(KlidWoodType woodType) {
        super(Material.GRASS, "klidSapling");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, woodType).withProperty(STAGE, 0));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE, STAGE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int stage;
        int treeType;
        if (meta % 2 == 0) {
            treeType = meta;
            stage = 0;
        } else {
            treeType = meta - 1;
            stage = 1;
        }
        return getDefaultState().withProperty(TYPE, KlidWoodType.getFromMetadata(treeType)).withProperty(STAGE, stage);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).getMetadata() * (1 + state.getValue(STAGE));
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            super.updateTick(worldIn, pos, state, rand);

            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
                this.grow(worldIn, rand, pos, state);
            }
        }
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        if (state.getValue(STAGE) == 0) {
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
        } else {
            this.generateTree(worldIn, pos, state, rand);
        }
    }

    private void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;

        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
        WorldGenerator worldgenerator = new WorldGenTrees(true, 5, ModBlocks.klidLog.getDefaultState(), ModBlocks.klidLeaves.getDefaultState(), false);
        worldgenerator.generate(worldIn, rand, pos);
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    public int damageDropped(IBlockState state) {
        return state.getValue(TYPE).getMetadata();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (KlidWoodType woodType : KlidWoodType.values()) {
            list.add(new ItemStack(itemIn, 1, woodType.getMetadata()));
        }
    }
}
