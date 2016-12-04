package com.cendrb.cenaonwheels.block;

import com.cendrb.cenaonwheels.tileentity.TileEntityKlidInfusionPlate;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by cendr_000 on 18.10.2016.
 */
public class BlockKlidInfusionPlate extends BlockBase implements ITileEntityProvider {

    private float efficiency;

    public BlockKlidInfusionPlate(String tierName, float efficiency) {
        super(Material.ROCK, "klidInfusion" + tierName + "Plate");
        this.efficiency = efficiency;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return new AxisAlignedBB(0, 0, 0, 1, 0.55, 1);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        TileEntity tileEntity;
        if ((tileEntity = worldIn.getTileEntity(pos)) instanceof TileEntityKlidInfusionPlate)
            ((TileEntityKlidInfusionPlate) tileEntity).setEfficiency(efficiency);
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityKlidInfusionPlate();
    }
}
