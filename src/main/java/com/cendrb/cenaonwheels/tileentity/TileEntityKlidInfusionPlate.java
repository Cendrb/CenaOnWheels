package com.cendrb.cenaonwheels.tileentity;

import com.cendrb.cenaonwheels.IKlidAcceptor;
import com.cendrb.cenaonwheels.init.KlidInfusionRecipe;
import com.cendrb.cenaonwheels.init.KlidInfusionRecipeResult;
import com.cendrb.cenaonwheels.init.ModBlocks;
import com.cendrb.cenaonwheels.init.ModKlidInfusionRecipes;
import com.cendrb.cenaonwheels.util.COWLogger;
import com.cendrb.cenaonwheels.util.WorldHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr_000 on 21.10.2016.
 */
public class TileEntityKlidInfusionPlate extends TileEntity implements ITickable, IKlidAcceptor {

    // in case the nbt is corrupt use this default value
    private float efficiency = ModBlocks.klidInfusionBasicPlate.getEfficiency();
    private int klidInfused = 0;
    private ArrayList<Item> currentIngredients;
    private boolean infusionRunning;
    private KlidInfusionRecipe currentRecipe;

    private MainItemStackHandler itemStackHandler;

    public TileEntityKlidInfusionPlate() {
        itemStackHandler = new MainItemStackHandler();
        currentIngredients = new ArrayList<>();
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

    @Override
    public void acceptKlid(int amount) {
        if (infusionRunning) {
            infuseWith(amount);
        } else {
            if (isInfusionReady()) {
                infusionRunning = true;
                infuseWith(amount);
            } else {
                WorldHelper.releaseKlidAt(worldObj, pos.getX(), pos.getY(), pos.getZ(), amount);
            }
        }
    }

    private void infuseWith(int klidAmount) {
        int efficientKlidAmount = (int) (klidAmount * efficiency);
        int klidLost = klidAmount - efficientKlidAmount;
        WorldHelper.releaseKlidAt(worldObj, pos.getX(), pos.getY(), pos.getZ(), klidLost);

        klidInfused += efficientKlidAmount;
        COWLogger.logDebug("Total done: " + klidInfused);
        COWLogger.logDebug("Infused: " + efficientKlidAmount);
        COWLogger.logDebug("Klid lost: " + klidLost);

        if (klidInfused >= currentRecipe.getRequiredKlid()) {
            COWLogger.logDebug("Infusion done!");
            if (klidInfused > currentRecipe.getRequiredKlid())
                WorldHelper.releaseKlidAt(worldObj, pos.getX(), pos.getY(), pos.getZ(), klidInfused - currentRecipe.getRequiredKlid());
            infusionRunning = false;
            currentRecipe = null;
            klidInfused = 0;
            currentIngredients = new ArrayList<>();
        }
    }

    public void setEfficiency(float efficiency) {
        this.efficiency = efficiency;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public boolean isInfusionReady() {
        return currentRecipe != null;
    }

    public boolean isInfusionRunning() {
        return infusionRunning;
    }

    public int getKlidInfused() {
        return klidInfused;
    }

    public int getKlidRequired() {
        if (currentRecipe == null)
            return 0;
        else
            return currentRecipe.getRequiredKlid();
    }

    public class MainItemStackHandler extends ItemStackHandler {
        public MainItemStackHandler() {
            setSize(1);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (infusionRunning) {
                return stack;
            } else {
                // check if can be klid infused
                ArrayList<Item> theoreticalFutureList = new ArrayList<>(currentIngredients);
                for (int i = 0; i < stack.stackSize; i++)
                    theoreticalFutureList.add(stack.getItem());
                KlidInfusionRecipeResult result = ModKlidInfusionRecipes.isPartOfRecipe(theoreticalFutureList);
                if (result == KlidInfusionRecipeResult.Complete) {
                    COWLogger.logDebug("Infusion ready!");
                    currentRecipe = ModKlidInfusionRecipes.getRecipeFor(theoreticalFutureList);
                    currentIngredients = theoreticalFutureList;
                    return null;
                } else if (result == KlidInfusionRecipeResult.PartOfTheRecipe) {
                    currentIngredients = theoreticalFutureList;
                    return null;
                } else {
                    return stack;
                }
            }
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return super.extractItem(slot, amount, simulate);
        }
    }
}
