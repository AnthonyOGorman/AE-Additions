package extracells.network.packet.part;

import extracells.network.packet.IPacketHandlerServer;
import extracells.network.packet.Packet;
import extracells.network.packet.PacketBufferEC;
import extracells.network.packet.PacketId;
import extracells.part.gas.PartGasTerminal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fluids.Fluid;

import java.io.IOException;

public class PacketTerminalSelectFluidServer extends Packet {
	Fluid fluid;
	PartGasTerminal terminalFluid;

	public PacketTerminalSelectFluidServer(Fluid fluid, PartGasTerminal terminalFluid) {
		this.fluid = fluid;
		this.terminalFluid = terminalFluid;
	}

	@Override
	public void writeData(PacketBufferEC data) throws IOException {
		data.writePart(terminalFluid);
		data.writeFluid(fluid);
	}

	@Override
	public PacketId getPacketId() {
		return PacketId.TERMINAL_SELECT_FLUID;
	}

	public static class Handler implements IPacketHandlerServer {
		@Override
		public void onPacketData(PacketBufferEC data, EntityPlayerMP player) throws IOException {
			PartGasTerminal terminalFluid = data.readPart(player.world);
			Fluid fluid = data.readFluid();
			if (fluid == null || terminalFluid == null) {
				return;
			}

			terminalFluid.setCurrentFluid(fluid);
		}
	}
}
