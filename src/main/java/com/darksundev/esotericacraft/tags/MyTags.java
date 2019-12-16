package com.darksundev.esotericacraft.tags;

import com.darksundev.esotericacraft.Registrar;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;

public class MyTags
{
	 public static final Tag<Block> TERRACOTTA = makeWrapperTag("terracotta");
	 public static final Tag<Block> SANDSTONE = makeWrapperTag("sandstone");
	 public static final Tag<Block> BAMBOO = makeWrapperTag("bamboo");
	 
	 private static Tag<Block> makeWrapperTag(String id)
	 {
		 return new BlockTags.Wrapper(Registrar.location(id));
	 }
}
