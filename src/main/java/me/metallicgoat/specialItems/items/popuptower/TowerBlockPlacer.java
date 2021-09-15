package me.metallicgoat.specialItems.items.popuptower;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import me.metallicgoat.specialItems.utils.XBlock;
import me.metallicgoat.specialItems.utils.XMaterial;
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
            if (arena != null && arena.canPlaceBlockAt(b.getLocation())) {
                PlaceBlock(arena, ladder, b, ladderdata, color);
            }
        }
    }

    private void PlaceBlock(Arena arena, boolean ladder, Block b, String ladderdata, DyeColor color){
        //Block block = b.getRelative(x, y, z);
        //BlockPlacer blockPlacer = plugin.blockPlacer;
        assert XMaterial.LADDER.parseMaterial() != null;
        if (!ladder && XMaterial.matchXMaterial(color.name() + "_WOOL").isPresent()) {
            XMaterial woolMat = XMaterial.matchXMaterial(color.name() + "_WOOL").get();
            XBlock.setType(b, woolMat);
        } else {
            b.setType(XMaterial.LADDER.parseMaterial());

            //Make this better (Error if ladder is not successfully placed from above. Ex. on a slab)
            if (b.getType() == XMaterial.LADDER.parseMaterial()) {
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
