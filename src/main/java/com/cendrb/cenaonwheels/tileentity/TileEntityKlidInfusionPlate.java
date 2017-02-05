package com.cendrb.cenaonwheels.tileentity;

import com.cendrb.cenaonwheels.IKlidAcceptor;
import com.cendrb.cenaonwheels.init.*;
import com.cendrb.cenaonwheels.util.COWLogger;
import com.cendrb.cenaonwheels.util.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
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
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        int metadata = getBlockMetadata();
        return new SPacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
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
            currentIngredients = new ArrayList<>();
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
        if (klidAmount >= currentRecipe.getKlidAcceptanceThreshold()) {
            klidLost = klidAmount - efficientKlidAmount;
            klidInfused += efficientKlidAmount;
            worldObj.playSound(null, pos, ModSounds.jes, SoundCategory.BLOCKS, 2f, 0.8f + (worldObj.rand.nextFloat() * 0.4f));
            COWLogger.logDebug("Added " + efficientKlidAmount + ", progress " + klidInfused + "/" + getKlidRequired() + ", klid loss: " + klidLost);
        } else {
            klidLost = klidAmount;
            worldObj.playSound(null, pos, ModSounds.saka, SoundCategory.BLOCKS, 2f, 0.8f + (worldObj.rand.nextFloat() * 0.4f));
            COWLogger.logDebug("Threshold not reached. Klid lost: " + klidLost);
        }
        WorldHelper.releaseKlidAt(worldObj, pos.getX(), pos.getY(), pos.getZ(), klidLost);

        if (klidInfused >= currentRecipe.getRequiredKlid()) {
            COWLogger.logDebug("Infusion complete!");
            worldObj.playSound(null, pos, ModSounds.kana, SoundCategory.BLOCKS, 2f, 1f);
            if (klidInfused > currentRecipe.getRequiredKlid())
                WorldHelper.releaseKlidAt(worldObj, pos.getX(), pos.getY(), pos.getZ(), klidInfused - currentRecipe.getRequiredKlid());
            outputItemStack = currentRecipe.getResult().copy();
            infusionRunning = false;
            currentRecipe = null;
            klidInfused = 0;
            setCurrentIngredients(new ArrayList<ItemStack>());
        }

        IBlockState blockState = worldObj.getBlockState(pos);
        worldObj.notifyBlockUpdate(pos, blockState, blockState, 1);
        worldObj.updateComparatorOutputLevel(pos, ModBlocks.klidInfusionBasicPlate);
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

    public int getComparatorOutput() {
        if (isInfusionReady() && !isInfusionRunning()) {
            return 1;
        } else if (outputItemStack != null) {
            return 15;
        } else if (isInfusionReady() && isInfusionRunning()) {
            return (int) (((double) getKlidInfused() / (double) getKlidRequired()) * 14) + 1;
        } else {
            return 0;
        }
    }

    private void setCurrentIngredients(ArrayList<ItemStack> ingredients) {
        currentIngredients = ingredients;
        IBlockState blockState = worldObj.getBlockState(pos);
        worldObj.notifyBlockUpdate(pos, blockState, blockState, 1);
        worldObj.updateComparatorOutputLevel(pos, ModBlocks.klidInfusionBasicPlate);
    }

    public ArrayList<ItemStack> getCurrentIngredients() {
        return currentIngredients;
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
                        worldObj.playSound(null, pos, ModSounds.heimArmed, SoundCategory.BLOCKS, 2f, 1f);
                        currentRecipe = ModKlidInfusionRecipes.getRecipeFor(theoreticalFutureList);
                        setCurrentIngredients(theoreticalFutureList);
                    }
                    return null;
                } else if (result == KlidInfusionRecipeResult.PartOfTheRecipe) {
                    if (!simulate)
                        setCurrentIngredients(theoreticalFutureList);
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
