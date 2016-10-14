package com.cendrb.cenaonwheels.block;

import com.cendrb.cenaonwheels.tileentity.TileEntityMultiblockPart;
import com.cendrb.cenaonwheels.util.COWLogger;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public abstract class BlockMultiblockPart extends BlockBase implements ITileEntityProvider {
    public BlockMultiblockPart(Material materialIn, String pureName) {
        super(materialIn, pureName);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        if (!(blockIn instanceof BlockMultiblockMaster)) {
            COWLogger.logDebug("neighborChanged called");
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity != null && tileEntity instanceof TileEntityMultiblockPart)
                ((TileEntityMultiblockPart) tileEntity).requestMultiblockCheck();
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMultiblockPart();
    }
}
