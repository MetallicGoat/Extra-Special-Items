package me.metallicgoat.specialItems.items.eggbridge;

import de.marcely.bedwars.api.arena.Arena;
import me.metallicgoat.specialItems.utils.XBlock;
import me.metallicgoat.specialItems.utils.XMaterial;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Optional;

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
        Optional<XMaterial> material = XMaterial.matchXMaterial(color.name() + "_WOOL");
        if (material.isPresent()) {
            XBlock.setType(b, material.get());
            arena.setBlockPlayerPlaced(b, true);
        }
    }
}
