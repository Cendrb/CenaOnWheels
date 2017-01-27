package com.cendrb.cenaonwheels.tileentity;

import com.cendrb.cenaonwheels.entity.EntityAkbarCow;
import com.cendrb.cenaonwheels.entity.EntityMegaAkbarCow;
import com.cendrb.cenaonwheels.init.ModSounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by cendr on 27/01/2017.
 */
public class TileEntityHeimCore extends TileEntity implements ITickable {

    private int ticksPassed = -1;
    private boolean armed = false;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("ticksPassed", ticksPassed);
        nbtTagCompound.setBoolean("armed", armed);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey("ticksPassed"))
            ticksPassed = nbtTagCompound.getInteger("ticksPassed");
        if (nbtTagCompound.hasKey("armed"))
            armed = nbtTagCompound.getBoolean("armed");
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

    public boolean isRunning() {
        return ticksPassed != -1;
    }

    public boolean getArmed() {
        return armed;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

    public void updateArmedStatus() {
        if (!worldObj.isRemote && !isRunning() && !armed) {
            if (worldObj.getBlockState(pos.up()).getBlock() == Blocks.TNT) {
                armed = true;
                worldObj.playSound(null, pos, ModSounds.heimArmed, SoundCategory.BLOCKS, 64.0F, 1.0F);
            }
        }
        if (!worldObj.isRemote && !isRunning() && armed) {
            if (!(worldObj.getBlockState(pos.up()).getBlock() == Blocks.TNT)) {
                armed = false;
                ticksPassed = 0;
                worldObj.playSound(null, pos, ModSounds.heimRiot, SoundCategory.BLOCKS, 64.0F, 1.0F);
                worldObj.playSound(null, pos, ModSounds.heimSong, SoundCategory.BLOCKS, 64.0F, 1.0F);
            }
        }
    }

    @Override
    public void update() {
        if (!worldObj.isRemote && ticksPassed != -1) {
            ticksPassed++;

            if (ticksPassed % 3 == 0) {
                Random random = new Random();
                EntityAkbarCow akbarCow = new EntityAkbarCow(worldObj);

                float randomizer = random.nextFloat();

                if (randomizer > 0.5F) {
                    akbarCow.motionY = 1 + random.nextFloat() * 2;
                    akbarCow.motionX = (0.5 - random.nextFloat()) * 4;
                    akbarCow.motionZ = (0.5 - random.nextFloat()) * 4;
                } else {
                    if (randomizer > 0.3F) {
                        akbarCow.motionY = 1 + random.nextFloat() * 2;
                        akbarCow.motionX = (0.5 - random.nextFloat()) * 4 * (0.5 + random.nextFloat());
                        akbarCow.motionZ = (0.5 - random.nextFloat()) * 4 * (0.5 + random.nextFloat());
                    } else {
                        if (randomizer > 0.1F) {
                            akbarCow.motionY = 1 + random.nextFloat() * 2;
                            akbarCow.motionX = (0.5 - random.nextFloat()) * 4 * (1 + random.nextFloat());
                            akbarCow.motionZ = (0.5 - random.nextFloat()) * 4 * (1 + random.nextFloat());
                        } else {
                            akbarCow.motionY = 1 + random.nextFloat() * 2;
                            akbarCow.motionX = (0.5 - random.nextFloat()) * 4 * (1.5 + random.nextFloat());
                            akbarCow.motionZ = (0.5 - random.nextFloat()) * 4 * (1.5 + random.nextFloat());
                        }

                    }

                    akbarCow.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());

                    worldObj.spawnEntityInWorld(akbarCow);
                }
            }

            if (ticksPassed % 300 == 0) {
                Random random = new Random();
                EntityMegaAkbarCow akbarCow = new EntityMegaAkbarCow(worldObj);

                float randomizer = random.nextFloat();

                if (randomizer > 0.5F) {
                    akbarCow.motionY = 1 + random.nextFloat() * 2;
                    akbarCow.motionX = (0.5 - random.nextFloat()) * 4;
                    akbarCow.motionZ = (0.5 - random.nextFloat()) * 4;
                } else {
                    if (randomizer > 0.3F) {
                        akbarCow.motionY = 1 + random.nextFloat() * 2;
                        akbarCow.motionX = (0.5 - random.nextFloat()) * 4 * (0.5 + random.nextFloat());
                        akbarCow.motionZ = (0.5 - random.nextFloat()) * 4 * (0.5 + random.nextFloat());
                    } else {
                        if (randomizer > 0.1F) {
                            akbarCow.motionY = 1 + random.nextFloat() * 2;
                            akbarCow.motionX = (0.5 - random.nextFloat()) * 4 * (1 + random.nextFloat());
                            akbarCow.motionZ = (0.5 - random.nextFloat()) * 4 * (1 + random.nextFloat());
                        } else {
                            akbarCow.motionY = 1 + random.nextFloat() * 2;
                            akbarCow.motionX = (0.5 - random.nextFloat()) * 4 * (1.5 + random.nextFloat());
                            akbarCow.motionZ = (0.5 - random.nextFloat()) * 4 * (1.5 + random.nextFloat());
                        }

                    }

                    akbarCow.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());

                    worldObj.spawnEntityInWorld(akbarCow);
                }
            }

            if (3600 < ticksPassed) {
                Random random = new Random();

                for (int count = 0; count < 1000; count++) {
                    EntityAkbarCow akbarCow = new EntityAkbarCow(worldObj);

                    float randomizer = random.nextFloat();

                    if (randomizer > 0.5F) {
                        akbarCow.motionY = 1 + random.nextFloat() * 40;
                        akbarCow.motionX = (0.5 - random.nextFloat()) * 4;
                        akbarCow.motionZ = (0.5 - random.nextFloat()) * 4;
                    } else {
                        if (randomizer > 0.3F) {
                            akbarCow.motionY = 1 + random.nextFloat() * 40;
                            akbarCow.motionX = (0.5 - random.nextFloat()) * 4 * (0.5 + random.nextFloat());
                            akbarCow.motionZ = (0.5 - random.nextFloat()) * 4 * (0.5 + random.nextFloat());
                        } else {
                            if (randomizer > 0.1F) {
                                akbarCow.motionY = 1 + random.nextFloat() * 40;
                                akbarCow.motionX = (0.5 - random.nextFloat()) * 4 * (1 + random.nextFloat());
                                akbarCow.motionZ = (0.5 - random.nextFloat()) * 4 * (1 + random.nextFloat());
                            } else {
                                akbarCow.motionY = 1 + random.nextFloat() * 40;
                                akbarCow.motionX = (0.5 - random.nextFloat()) * 4 * (1.5 + random.nextFloat());
                                akbarCow.motionZ = (0.5 - random.nextFloat()) * 4 * (1.5 + random.nextFloat());
                            }

                        }

                        akbarCow.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());

                        worldObj.spawnEntityInWorld(akbarCow);
                    }
                }
                worldObj.setBlockToAir(pos);
            }

        }
    }
}

