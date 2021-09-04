package me.metallicgoat.specialItems.items.popuptower;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Ladder;

public class TowerBlockPlacer {

    //TODO: Make Sound Here

    public TowerBlockPlacer(Block b, DyeColor color, Player p, boolean ladder, String ladderdata) {
        Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(p);

        //Is block there?
        if (b.getType().equals(Material.AIR)) {

            //Is block inside region
            if (arena != null && arena.isInside(b.getLocation())) {
                PlaceBlock(arena, ladder, b, ladderdata, color);
            }
        }
    }

    private void PlaceBlock(Arena arena, boolean ladder, Block b, String ladderdata, DyeColor color){
        //Block block = b.getRelative(x, y, z);
        //BlockPlacer blockPlacer = plugin.blockPlacer;

        if (!ladder) {
            Material woolMat = Material.valueOf(color.name() + "_WOOL");
            b.setType(woolMat);
        } else {
            b.setType(Material.LADDER);

            //Make this better (Error if ladder is not successfully placed from above. Ex. on a slab)
            if (b.getType() == Material.LADDER) {
                BlockState state = b.getState();
                Ladder lad = new Ladder();
                lad.setFacingDirection(BlockFace.valueOf(ladderdata));
                state.setData(lad);
                state.update();
            }
        }
        arena.setBlockPlayerPlaced(b, true);
    }
}
