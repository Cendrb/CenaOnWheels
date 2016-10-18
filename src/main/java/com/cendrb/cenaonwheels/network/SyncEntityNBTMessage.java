package com.cendrb.cenaonwheels.network;

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
public class SyncEntityNBTMessage implements IMessage {

    private int entityId;
    private NBTTagCompound data;

    public SyncEntityNBTMessage()
    {

    }

    public SyncEntityNBTMessage(int targetEntityId, NBTTagCompound data)
    {
        this.entityId = targetEntityId;
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        ByteBufUtils.writeTag(buf, data);
    }

    public static class Handler implements IMessageHandler<SyncEntityNBTMessage, IMessage>
    {
        @Override
        public IMessage onMessage(SyncEntityNBTMessage message, MessageContext ctx) {
            IThreadListener mainThread = Minecraft.getMinecraft();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    Entity entity = Minecraft.getMinecraft().thePlayer.worldObj.getEntityByID(message.entityId);
                    if(entity != null)
                        entity.readFromNBT(message.data);
                    else
                        COWLogger.logWarning("SyncEntityNBTMessage: Received reference to a non-existent entity");
                }
            });
            return null;
        }
    }
}
