package net.smileycorp.unexperienced.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

public class BoolMessage implements Packet<PacketListener> {

	public BoolMessage() {}

	private boolean value;

	public BoolMessage(boolean value) {
		this.value=value;
	}

	public boolean getValue() {
		return value;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBoolean(value);
	}

	public void read(FriendlyByteBuf buf) {
		value = buf.readBoolean();
	}

	@Override
	public void handle(PacketListener listener) {}

}
