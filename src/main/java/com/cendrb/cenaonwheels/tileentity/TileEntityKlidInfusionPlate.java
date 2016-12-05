package com.cendrb.cenaonwheels.tileentity;

import com.cendrb.cenaonwheels.IKlidAcceptor;
import com.cendrb.cenaonwheels.init.ModBlocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by cendr_000 on 21.10.2016.
 */
public class TileEntityKlidInfusionPlate extends TileEntity implements ITickable, IKlidAcceptor {

    // in case the nbt is corrupt use this default value
    private float efficiency = ModBlocks.klidInfusionBasicPlate.getEfficiency();
    private int klidInfused = 0;

    private MainItemStackHandler itemStackHandler;

    public TileEntityKlidInfusionPlate() {
        itemStackHandler = new MainItemStackHandler();
    }

    @Override
    public void update() {

    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) itemStackHandler;
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("efficiency"))
            efficiency = compound.getFloat("efficiency");
        if (compound.hasKey("klidInfused"))
            klidInfused = compound.getInteger("klidInfused");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat("efficiency", efficiency);
        compound.setInteger("klidInfused", klidInfused);
        return compound;
    }

    public void setEfficiency(float efficiency) {
        this.efficiency = efficiency;
    }

    @Override
    public void acceptKlid(int amount) {

    }

    public static class MainItemStackHandler extends ItemStackHandler {
        public MainItemStackHandler() {
            setSize(1);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (stacks[0] == null || stacks[0].stackSize != 0) {
                return stack;
            } else {
                // check if can be klid infused

            }
            return null;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return super.extractItem(slot, amount, simulate);
        }
    }
}
