package com.cendrb.cenaonwheels.block;

import com.cendrb.cenaonwheels.tileentity.TileEntityHeimCore;
import com.cendrb.cenaonwheels.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by cendr_000 on 4. 7. 2015.
 */
public class BlockHeimCore extends BlockBase implements ITileEntityProvider {

    public static final PropertyBool EXPLOSIVE = PropertyBool.create("explosive");

    public BlockHeimCore() {
        super(Material.ROCK, "heimCore");
        setResistance(1000);
        setDefaultState(blockState.getBaseState().withProperty(EXPLOSIVE, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, EXPLOSIVE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(EXPLOSIVE) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(EXPLOSIVE, meta == 1);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        super.neighborChanged(state, worldIn, pos, blockIn);
        TileEntityHeimCore heimCore = (TileEntityHeimCore) worldIn.getTileEntity(pos);
        if (heimCore != null) {
            heimCore.updateArmedStatus();
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityHeimCore heimCore = (TileEntityHeimCore) worldIn.getTileEntity(pos);
        if (!worldIn.isRemote && heimCore != null && !heimCore.isRunning() && heldItem == null) {
            if (state.getValue(EXPLOSIVE)) {
                if (heimCore.getArmed()) {
                    playerIn.addChatMessage(new TextComponentString("Destroy TNT on top to start"));
                } else {
                    playerIn.addChatMessage(new TextComponentString("Place TNT on top to arm"));
                }
            } else {
                if (!setupMultiblockAkbar(worldIn, pos)) {
                    playerIn.addChatMessage(new TextComponentString("AKBAR 420: Requires 3X3X3 structure of Akbar blocks. Akbar core in the middle. Then click on top middle block."));
                }
            }
        }

        return true;
    }

    public static boolean setupMultiblockAkbar(World world, BlockPos pos) {
        IBlockState currentState = world.getBlockState(pos);
        if (currentState.getBlock() instanceof BlockHeimCore)
            if (WorldHelper.surroundedBy(world, pos, BlockHeim.class)) {
                world.setBlockState(pos, currentState.withProperty(EXPLOSIVE, true));

                world.setBlockToAir(pos.north());
                world.setBlockToAir(pos.south());
                world.setBlockToAir(pos.west());
                world.setBlockToAir(pos.east());
                world.setBlockToAir(pos.north().west());
                world.setBlockToAir(pos.north().east());
                world.setBlockToAir(pos.south().west());
                world.setBlockToAir(pos.south().east());

                world.setBlockToAir(pos.up());
                world.setBlockToAir(pos.up().north());
                world.setBlockToAir(pos.up().south());
                world.setBlockToAir(pos.up().west());
                world.setBlockToAir(pos.up().east());
                world.setBlockToAir(pos.up().north().west());
                world.setBlockToAir(pos.up().north().east());
                world.setBlockToAir(pos.up().south().west());
                world.setBlockToAir(pos.up().south().east());

                world.setBlockToAir(pos.down());
                world.setBlockToAir(pos.down().north());
                world.setBlockToAir(pos.down().south());
                world.setBlockToAir(pos.down().west());
                world.setBlockToAir(pos.down().east());
                world.setBlockToAir(pos.down().north().west());
                world.setBlockToAir(pos.down().north().east());
                world.setBlockToAir(pos.down().south().west());
                world.setBlockToAir(pos.down().south().east());

                return true;
            }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityHeimCore();
    }
}
