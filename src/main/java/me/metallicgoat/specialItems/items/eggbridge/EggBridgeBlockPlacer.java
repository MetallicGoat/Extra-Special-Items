package me.metallicgoat.specialItems.items.eggbridge;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.tools.PersistentBlockData;
import me.metallicgoat.specialItems.config.ConfigValue;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class EggBridgeBlockPlacer {
    public EggBridgeBlockPlacer(Block b, DyeColor color, Arena arena) {

        //Is block there?
        if (b.getType().equals(Material.AIR)) {

            //Is block inside region
            if (arena != null && arena.canPlaceBlockAt(b.getLocation())) {
                PlaceBlock(arena, b, color);
            }
        }
    }

    private void PlaceBlock(Arena arena, Block b, DyeColor color){

        final PersistentBlockData data = PersistentBlockData.fromMaterial(ConfigValue.egg_bridger_block_material).getDyedData(color);

        data.place(b, true);
        arena.setBlockPlayerPlaced(b, true);
    }
}
