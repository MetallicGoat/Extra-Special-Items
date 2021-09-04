package me.metallicgoat.specialItems.items.eggbridge;

import de.marcely.bedwars.api.arena.Arena;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class EggBridgeBlockPlacer {
    public EggBridgeBlockPlacer(Block b, DyeColor color, Arena arena) {

        //Is block there?
        if (b.getType().equals(Material.AIR)) {

            //Is block inside region
            if (arena != null && arena.isInside(b.getLocation())) {
                PlaceBlock(arena, b, color);
            }
        }
    }

    private void PlaceBlock(Arena arena, Block b, DyeColor color){

        Material woolMat = Material.valueOf(color.name() + "_WOOL");
        b.setType(woolMat);

        arena.setBlockPlayerPlaced(b, true);
    }
}
