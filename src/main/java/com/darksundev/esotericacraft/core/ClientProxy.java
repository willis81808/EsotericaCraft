package com.darksundev.esotericacraft.core;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.EsotericaCraftPacketHandler;
import com.darksundev.esotericacraft.dimension.DynamicDimension;
import com.darksundev.esotericacraft.packets.PlayerInventoryMessagePacket;
import com.darksundev.esotericacraft.packets.RuneCastMessagePacket;
import com.darksundev.esotericacraft.packets.DimensionDataPacket;
import com.darksundev.esotericacraft.runes.IItemEffect;
import com.darksundev.esotericacraft.runes.RuneManager;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.FMLHandshakeMessages;

@EventBusSubscriber(modid = EsotericaCraft.modid, value = Dist.CLIENT)
public class ClientProxy implements IProxy
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
	
	@Override
	public void init()
	{
		// register dimension created event packet
		EsotericaCraftPacketHandler.registerConsumer(DimensionDataPacket.class,
				// encoder
				(msg, buffer) ->
				{
					msg.encode(buffer);
				},
				// decoder
				(buffer) ->
				{ 
					return DimensionDataPacket.decode(buffer);
				},
				// consumer
				(msg, context) -> {
					// register dimension
					context.get().enqueueWork(() ->
					{
	                    if (DimensionType.getById(msg.id) == null) {
	                    	DynamicDimension.register(msg.name.getPath(), Biomes.THE_VOID);
		                    EsotericaCraft.logger.info(String.format("Registered dimension %s with ID %d", msg.name.getPath(), msg.id));
	                    }
	                    context.get().setPacketHandled(true);
					});
				});
		
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
	                    context.get().setPacketHandled(true);
					});
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
	                    context.get().setPacketHandled(true);
					});
				});
	}
}
