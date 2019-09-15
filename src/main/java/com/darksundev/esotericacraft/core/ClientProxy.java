package com.darksundev.esotericacraft.core;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.EsotericaCraftPacketHandler;
import com.darksundev.esotericacraft.packets.PlayerInventoryMessagePacket;
import com.darksundev.esotericacraft.packets.RuneCastMessagePacket;
import com.darksundev.esotericacraft.runes.IItemEffect;
import com.darksundev.esotericacraft.runes.RuneManager;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EsotericaCraft.modid, value = Dist.CLIENT)
public class ClientProxy
{
	@SubscribeEvent
	public static void onDisplayTooltip(ItemTooltipEvent event)
	{
		CompoundNBT data = event.getItemStack().getTag();
		// check for any rune effect tags
		if (data != null)
		{
			for (String key : data.keySet())
			{
				if (RuneManager.nbtTagMap.containsKey(key))
				{
					// execute rune effect, and pass this item to the event
					((IItemEffect)RuneManager.nbtTagMap.get(key)).displayTooltip(event);
				}
			}
		}
	}
	
	public static void init()
	{
		// register rune particle packet
		EsotericaCraftPacketHandler.registerConsumer(RuneCastMessagePacket.class,
				// encoder
				(msg, buffer) ->
				{
					msg.writeToBuffer(buffer);
				},
				// decoder
				(buffer) ->
				{ 
					return RuneCastMessagePacket.fromBuffer(buffer);
				},
				// consumer
				(msg, context) -> {
					context.get().enqueueWork(() ->
					{
						World w = Minecraft.getInstance().world;
						msg.spawnParticle(w);
					});
					context.get().setPacketHandled(true);
				});

		// reset inventory packet received
		EsotericaCraftPacketHandler.registerConsumer(PlayerInventoryMessagePacket.class,
				// encoder
				(msg, buffer) ->
				{
					msg.writeToBuffer(buffer);
				},
				// decoder
				(buffer) ->
				{
					return PlayerInventoryMessagePacket.fromBuffer(buffer);
				},
				// consumer
				(msg, context) ->
				{
					context.get().enqueueWork(() ->
					{
						Minecraft.getInstance().player.container.setAll(msg.items);
					});
					context.get().setPacketHandled(true);
				});
	}
}
