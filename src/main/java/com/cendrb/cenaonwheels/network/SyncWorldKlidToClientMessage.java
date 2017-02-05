package com.cendrb.cenaonwheels.network;

import com.cendrb.cenaonwheels.KlidWorldSavedData;
import com.cendrb.cenaonwheels.util.COWLogger;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by cendr_000 on 17.10.2016.
 */
public class SyncWorldKlidToClientMessage implements IMessage {

    private int klidAmount;

    public SyncWorldKlidToClientMessage() {

    }

    public SyncWorldKlidToClientMessage(int klidAmount) {
        this.klidAmount = klidAmount;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        klidAmount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(klidAmount);
    }

    public static class Handler implements IMessageHandler<SyncWorldKlidToClientMessage, IMessage>
    {
        @Override
        public IMessage onMessage(final SyncWorldKlidToClientMessage message, MessageContext ctx) {
            IThreadListener mainThread = Minecraft.getMinecraft();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    KlidWorldSavedData savedKlid = KlidWorldSavedData.getFor(Minecraft.getMinecraft().thePlayer.worldObj);
                    savedKlid.setKlidInTheAtmosphere(message.klidAmount, false); // do not notify clients - this a client
                }
            });
            return null;
        }
    }
}
