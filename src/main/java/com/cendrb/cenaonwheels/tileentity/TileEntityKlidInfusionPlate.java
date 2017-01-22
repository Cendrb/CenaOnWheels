package com.cendrb.cenaonwheels.tileentity;

import com.cendrb.cenaonwheels.IKlidAcceptor;
import com.cendrb.cenaonwheels.init.KlidInfusionRecipe;
import com.cendrb.cenaonwheels.init.KlidInfusionRecipeResult;
import com.cendrb.cenaonwheels.init.ModKlidInfusionRecipes;
import com.cendrb.cenaonwheels.util.COWLogger;
import com.cendrb.cenaonwheels.util.WorldHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;

/**
 * Created by cendr_000 on 21.10.2016.
 */
public class TileEntityKlidInfusionPlate extends TileEntity implements IKlidAcceptor {

    private float efficiency;
    private int klidInfused = 0;
    private ArrayList<ItemStack> currentIngredients;
    private ItemStack outputItemStack;
    private boolean infusionRunning;
    private KlidInfusionRecipe currentRecipe;

    private MainItemStackHandler itemStackHandler;

    public TileEntityKlidInfusionPlate() {
        itemStackHandler = new MainItemStackHandler();
        currentIngredients = new ArrayList<>();
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
        if (compound.hasKey("infusionRunning"))
            infusionRunning = compound.getBoolean("infusionRunning");
        if (compound.hasKey("outputItemStack")) {
            outputItemStack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("outputItemStack"));
        }

        if (compound.hasKey("currentIngredients")) {
            NBTTagList tagList = (NBTTagList) compound.getTag("currentIngredients");
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound itemCompound = tagList.getCompoundTagAt(i);
                currentIngredients.add(ItemStack.loadItemStackFromNBT(itemCompound));
            }
            currentRecipe = ModKlidInfusionRecipes.getRecipeFor(currentIngredients);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat("efficiency", efficiency);
        compound.setInteger("klidInfused", klidInfused);
        compound.setBoolean("infusionRunning", infusionRunning);

        if (outputItemStack != null) {
            NBTTagCompound outputCompound = new NBTTagCompound();
            outputItemStack.writeToNBT(outputCompound);
            compound.setTag("outputItemStack", outputCompound);
        }

        NBTTagList ingredientsTagList = new NBTTagList();
        for (ItemStack itemStack : currentIngredients) {
            NBTTagCompound itemStackCompound = new NBTTagCompound();
            itemStack.writeToNBT(itemStackCompound);
            ingredientsTagList.appendTag(itemStackCompound);
        }
        compound.setTag("currentIngredients", ingredientsTagList);

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
        int klidLost;
        if (efficientKlidAmount >= currentRecipe.getKlidAcceptanceThreshold()) {
            klidLost = klidAmount - efficientKlidAmount;
            klidInfused += efficientKlidAmount;
            COWLogger.logDebug("Added " + efficientKlidAmount + ", progress " + klidInfused + "/" + getKlidRequired() + ", klid loss: " + klidLost);
        } else {
            klidLost = klidAmount;
            COWLogger.logDebug("Threshold not reached. Klid lost: " + klidLost);
        }
        WorldHelper.releaseKlidAt(worldObj, pos.getX(), pos.getY(), pos.getZ(), klidLost);

        if (klidInfused >= currentRecipe.getRequiredKlid()) {
            COWLogger.logDebug("Infusion complete!");
            if (klidInfused > currentRecipe.getRequiredKlid())
                WorldHelper.releaseKlidAt(worldObj, pos.getX(), pos.getY(), pos.getZ(), klidInfused - currentRecipe.getRequiredKlid());
            outputItemStack = currentRecipe.getResult().copy();
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
            if (infusionRunning || outputItemStack != null) {
                return stack;
            } else {
                // check if can be klid infused
                ArrayList<ItemStack> theoreticalFutureList = new ArrayList<>(currentIngredients);
                theoreticalFutureList.add(stack);
                KlidInfusionRecipeResult result = ModKlidInfusionRecipes.isPartOfRecipe(theoreticalFutureList);
                if (result == KlidInfusionRecipeResult.Complete) {
                    if (!simulate) {
                        COWLogger.logDebug("Infusion ready!");
                        currentRecipe = ModKlidInfusionRecipes.getRecipeFor(theoreticalFutureList);
                        currentIngredients = theoreticalFutureList;
                    }
                    return null;
                } else if (result == KlidInfusionRecipeResult.PartOfTheRecipe) {
                    if (!simulate)
                        currentIngredients = theoreticalFutureList;
                    return null;
                } else {
                    return stack;
                }
            }
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (outputItemStack != null) {
                ItemStack toBeReturned = outputItemStack;
                if (!simulate)
                    outputItemStack = null;
                return toBeReturned;
            } else
                return null;
        }
    }
}
