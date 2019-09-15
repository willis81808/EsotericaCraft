package com.darksundev.esotericacraft.lists;

import com.darksundev.esotericacraft.EsotericaCraft;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(EsotericaCraft.modid)
public class ContainerList
{
	@ObjectHolder("test_container")
	public static final ContainerType<TestContainer> TEST_CONTAINER = null;
	
	public static void registerContainers(IForgeRegistry<ContainerType<?>> registry)
	{
		// register containers here?
		registry.register(IForgeContainerType.create((windowId, inv, data) -> new TestContainer(TEST_CONTAINER, windowId)).setRegistryName("test_container"));
	}
	
	public static class TestContainer extends Container
	{

		protected TestContainer(ContainerType<?> type, int id) {
			super(type, id);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean canInteractWith(PlayerEntity playerIn) {
			return true;
		}
		
	}
}
