package com.darksundev.esotericacraft;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class EsotericaCraftPacketHandler
{
	private static final String PROTOCOL_VERSION = "1.1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
	    new ResourceLocation(EsotericaCraft.modid, "main"),
	    () -> PROTOCOL_VERSION,
	    PROTOCOL_VERSION::equals,
	    PROTOCOL_VERSION::equals
	);
	/*
public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
	    new ResourceLocation(EsotericaCraft.modid, "main"),
	    () -> PROTOCOL_VERSION,
	    PROTOCOL_VERSION::equals,
	    PROTOCOL_VERSION::equals
	);
	static {
	    // TODO: Temporary hack until Forge fix
	    try {
	        Field channelField = FMLNetworkConstants.class.getDeclaredField("handshakeChannel");
	        channelField.setAccessible(true);
	
	        //SimpleChannel handshakeChannel = (SimpleChannel) channelField.get(null);
	        INSTANCE.messageBuilder(S2CDimensionSync.class, 100)
	                .loginIndex(S2CDimensionSync::getLoginIndex, S2CDimensionSync::setLoginIndex)
	                .decoder(S2CDimensionSync::decode)
	                .encoder(S2CDimensionSync::encode)
	                .buildLoginPacketList(isLocal -> {
	                    if (isLocal) return ImmutableList.of();
	                    
	                    ArrayList<Pair<String, S2CDimensionSync>> list = new ArrayList<Pair<String, S2CDimensionSync>>();
	                    DimensionType.getAll().forEach(d -> { if (d.getId() > 1) list.add(Pair.of(d.getRegistryName().toString(), new S2CDimensionSync(d))); });
	                    return list;
	                    //return ImmutableList.of(Pair.of("Midnight Dim Sync", new S2CDimensionSync(DynamicDimension..midnight())));
	                })
	                .consumer((msg, ctx) -> {
	                    if (DimensionManager.getRegistry().getByValue(msg.id) == null) {
	                        DimensionManager.registerDimensionInternal(msg.id, msg.name, msg.dimension, null, msg.skyLight);
	                    }
	                    ctx.get().setPacketHandled(true);
	                    INSTANCE.reply(new FMLHandshakeMessages.C2SAcknowledge(), ctx.get());
	                })
	                .add();
	    } catch (ReflectiveOperationException e) {
	        EsotericaCraft.logger.error("Failed to add dimension sync to handshake channel", e);
	    }
	}
	 */
	
	private static int packetID = 0;
	
	public static <T> void registerConsumer(Class<T> classType, BiConsumer<T, PacketBuffer> encoder, Function<PacketBuffer, T> decoder, BiConsumer<T, Supplier<Context>> messageConsumer)
	{
		INSTANCE.registerMessage(packetID++, classType, encoder, decoder, messageConsumer);
	}
	
}
