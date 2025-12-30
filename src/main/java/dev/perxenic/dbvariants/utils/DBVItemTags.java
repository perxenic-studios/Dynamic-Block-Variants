package dev.perxenic.dbvariants.utils;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static dev.perxenic.dbvariants.DBVariants.dbvLoc;

public class DBVItemTags {
    public static final TagKey<Item> CHEST_MATERIAL = ItemTags.create(dbvLoc("chest_material"));
}
