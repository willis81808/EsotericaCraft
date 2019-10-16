package com.darksundev.esotericacraft.packets;

import java.util.ArrayList;
import java.util.List;

import io.netty.util.IntSupplier;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class PlayerInventoryMessagePacket implements IntSupplier
{
	public List<ItemStack> items;
	
	public PlayerInventoryMessagePacket(List<ItemStack> items)
	{
		this.items = items;
	}
	
	public void writeToBuffer(PacketBuffer buffer)
	{
		// write item count
		buffer.writeInt(items.size());
		
		// write items
		for (ItemStack stack : items)
		{
			buffer.writeItemStack(stack);
		}
	}
	
	public static PlayerInventoryMessagePacket fromBuffer(PacketBuffer buffer)
	{
		// read item count
		int count = buffer.readInt();
		
		// read items
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (int i = 0; i < count; i++)
		{
			ItemStack stack = buffer.readItemStack();
			if (stack != null)
				items.add(stack);
		}
		
		return new PlayerInventoryMessagePacket(items);
	}

	@Override
	public int get() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
}
