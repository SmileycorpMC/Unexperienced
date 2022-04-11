package net.smileycorp.unexperienced.network;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;

public class BoolMessage implements IPacket<INetHandler> {

		public BoolMessage() {}

		private boolean value;

		public BoolMessage(boolean value) {
			this.value=value;
		}

		public boolean getValue() {
			return value;
		}

		@Override
		public void read(PacketBuffer buf) throws IOException {
			value = buf.readBoolean();
		}

		@Override
		public void write(PacketBuffer buf) throws IOException {
			buf.writeBoolean(value);
		}

		@Override
		public void handle(INetHandler handler) {}


}
