package com.cendrb.cenaonwheels.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by cendr_000 on 15.10.2016.
 */
public class SpawnParticleMessage implements IMessage {

    private EnumParticleTypes particle;
    private Vec3d spawnPos;
    private Vec3d direction;

    public SpawnParticleMessage() {

    }

    public SpawnParticleMessage(EnumParticleTypes particle, Vec3d spawnPos, Vec3d direction) {
        this.particle = particle;
        this.spawnPos = spawnPos;
        this.direction = direction;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        particle = EnumParticleTypes.values()[buf.readInt()];
        spawnPos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        direction = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(particle.ordinal());
        buf.writeDouble(spawnPos.xCoord);
        buf.writeDouble(spawnPos.yCoord);
        buf.writeDouble(spawnPos.zCoord);
        buf.writeDouble(direction.xCoord);
        buf.writeDouble(direction.yCoord);
        buf.writeDouble(direction.zCoord);
    }

    public static class Handler implements IMessageHandler<SpawnParticleMessage, IMessage> {
        @Override
        public IMessage onMessage(final SpawnParticleMessage message, MessageContext ctx) {
            IThreadListener mainThread = Minecraft.getMinecraft();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().thePlayer.worldObj.spawnParticle(message.particle, message.spawnPos.xCoord, message.spawnPos.yCoord, message.spawnPos.zCoord, message.direction.xCoord, message.direction.yCoord, message.direction.zCoord);
                }
            });
            return null;
        }
    }
}
