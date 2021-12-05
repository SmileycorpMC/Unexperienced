package net.smileycorp.unexperienced;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BoolMessage implements IMessage {
	
	private boolean value;

	public BoolMessage() {
		this(false);
	}

	public BoolMessage(boolean value) {
		this.value = value;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		value = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(value);
	}
	
	public boolean getValue() {
		return value;
	}

}
