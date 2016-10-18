package com.cendrb.cenaonwheels.item;

import com.cendrb.cenaonwheels.IKlidAcceptor;
import com.cendrb.cenaonwheels.ITargetable;
import com.cendrb.cenaonwheels.block.BlockCowKlidGenerator;
import com.cendrb.cenaonwheels.block.BlockKlidStorageCap;
import com.cendrb.cenaonwheels.tileentity.TileEntityCowKlidGenerator;
import com.cendrb.cenaonwheels.tileentity.TileEntityKlidStorage;
import com.cendrb.cenaonwheels.util.COWLogger;
import com.cendrb.cenaonwheels.util.WorldHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by cendr_000 on 14.10.2016.
 */
public class ItemWrench extends ItemBase {

    private static String LINK_BLOCK_POS = "linkBlockPos";
    private static String LINK_BLOCK_CLASS = "linkBlockClass";

    public ItemWrench() {
        super("wrench");
        setMaxStackSize(1);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos posClicked, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            NBTTagCompound compound = new NBTTagCompound();
            stack.setTagCompound(compound);
            tag = compound;
        }

        if (playerIn.isSneaking()) {
            // link mode
            if (WorldHelper.isBlock(worldIn, posClicked, BlockCowKlidGenerator.class)) {
                TileEntity tileEntityClicked = worldIn.getTileEntity(posClicked);
                if (tileEntityClicked instanceof ITargetable) {
                    tag.setLong(LINK_BLOCK_POS, posClicked.toLong());
                    tag.setString(LINK_BLOCK_CLASS, "Klid Generator");
                }
                return EnumActionResult.SUCCESS;
            } else {
                if(tag.hasKey(LINK_BLOCK_POS)) {
                    BlockPos savedPos = BlockPos.fromLong(tag.getLong(LINK_BLOCK_POS));
                    TileEntity tileEntity = worldIn.getTileEntity(savedPos);
                    if (tileEntity instanceof ITargetable)
                    {
                        ((ITargetable)tileEntity).setTargetLocation(posClicked);
                    }
                }
                tag.removeTag(LINK_BLOCK_POS);
                tag.removeTag(LINK_BLOCK_CLASS);
            }
        } else {
            if (!worldIn.isRemote) {
                // info mode
                TileEntity tileEntity;
                if (WorldHelper.isBlock(worldIn, posClicked, BlockKlidStorageCap.class) && (tileEntity = worldIn.getTileEntity(posClicked)) instanceof TileEntityKlidStorage) {
                    TileEntityKlidStorage visStorage = (TileEntityKlidStorage) tileEntity;
                    playerIn.addChatComponentMessage(new TextComponentString(String.format("Klid Storage\nStructure complete: %s\nMaximum capacity: %d\nCurrent energy: %d",
                            String.valueOf(visStorage.isMultiblockComplete()),
                            visStorage.getCurrentEnergyMax(),
                            visStorage.getCurrentEnergy())));
                    return EnumActionResult.SUCCESS;
                } else if (WorldHelper.isBlock(worldIn, posClicked, BlockCowKlidGenerator.class) && (tileEntity = worldIn.getTileEntity(posClicked)) instanceof TileEntityCowKlidGenerator) {
                    TileEntityCowKlidGenerator cowVisGenerator = (TileEntityCowKlidGenerator) tileEntity;
                    playerIn.addChatComponentMessage(new TextComponentString("Cow Klid Generator\nPěkná sračka"));
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        if (stack.getTagCompound() != null && stack.getTagCompound().hasKey(LINK_BLOCK_POS))
            tooltip.add(
                    stack.getTagCompound().getString(LINK_BLOCK_CLASS) +
                            " at " +
                            COWLogger.formatBlockPos(BlockPos.fromLong(stack.getTagCompound().getLong(LINK_BLOCK_POS))));
    }
}
