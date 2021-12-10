package me.metallicgoat.specialItems.items.popuptower.towers;

import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.items.popuptower.TowerBlockPlacer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.LinkedHashMap;

public class South {

    public South(Block chest, SpecialItemUseSession session) {

        Block placedLoc = chest.getRelative(0, 1, 0);

        LinkedHashMap<Block, Boolean> towerBlock = new LinkedHashMap<>();

        towerBlock.put(placedLoc.getRelative(1, 0, 2), false);
        towerBlock.put(placedLoc.getRelative(2, 0, 1), false);
        towerBlock.put(placedLoc.getRelative(2, 0, 0), false);
        towerBlock.put(placedLoc.getRelative(1, 0, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 0, -1), false);
        towerBlock.put(placedLoc.getRelative(-1, 0, -1), false);
        towerBlock.put(placedLoc.getRelative(-2, 0, 0), false);
        towerBlock.put(placedLoc.getRelative(-2, 0, 1), false);
        towerBlock.put(placedLoc.getRelative(-1, 0, 2), false);
        towerBlock.put(placedLoc.getRelative(0, 0, 0), true);

        towerBlock.put(placedLoc.getRelative(1, 1, 2), false);
        towerBlock.put(placedLoc.getRelative(2, 1, 1), false);
        towerBlock.put(placedLoc.getRelative(2, 1, 0), false);
        towerBlock.put(placedLoc.getRelative(1, 1, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 1, -1), false);
        towerBlock.put(placedLoc.getRelative(-1, 1, -1), false);
        towerBlock.put(placedLoc.getRelative(-2, 1, 0), false);
        towerBlock.put(placedLoc.getRelative(-2, 1, 1), false);
        towerBlock.put(placedLoc.getRelative(-1, 1, 2), false);
        towerBlock.put(placedLoc.getRelative(0, 1, 0), true);

        towerBlock.put(placedLoc.getRelative(1, 2, 2), false);
        towerBlock.put(placedLoc.getRelative(2, 2, 1), false);
        towerBlock.put(placedLoc.getRelative(2, 2, 0), false);
        towerBlock.put(placedLoc.getRelative(1, 2, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 2, -1), false);
        towerBlock.put(placedLoc.getRelative(-1, 2, -1), false);
        towerBlock.put(placedLoc.getRelative(-2, 2, 0), false);
        towerBlock.put(placedLoc.getRelative(-2, 2, 1), false);
        towerBlock.put(placedLoc.getRelative(-1, 2, 2), false);
        towerBlock.put(placedLoc.getRelative(0, 2, 0), true);

        towerBlock.put(placedLoc.getRelative(0, 3, 2), false);
        towerBlock.put(placedLoc.getRelative(1, 3, 2), false);
        towerBlock.put(placedLoc.getRelative(2, 3, 1), false);
        towerBlock.put(placedLoc.getRelative(2, 3, 0), false);
        towerBlock.put(placedLoc.getRelative(1, 3, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 3, -1), false);
        towerBlock.put(placedLoc.getRelative(-1, 3, -1), false);
        towerBlock.put(placedLoc.getRelative(-2, 3, 0), false);
        towerBlock.put(placedLoc.getRelative(-2, 3, 1), false);
        towerBlock.put(placedLoc.getRelative(-1, 3, 2), false);
        towerBlock.put(placedLoc.getRelative(0, 3, 0), true);

        towerBlock.put(placedLoc.getRelative(0, 4, 2), false);
        towerBlock.put(placedLoc.getRelative(1, 4, 2), false);
        towerBlock.put(placedLoc.getRelative(2, 4, 1), false);
        towerBlock.put(placedLoc.getRelative(2, 4, 0), false);
        towerBlock.put(placedLoc.getRelative(1, 4, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 4, -1), false);
        towerBlock.put(placedLoc.getRelative(-1, 4, -1), false);
        towerBlock.put(placedLoc.getRelative(-2, 4, 0), false);
        towerBlock.put(placedLoc.getRelative(-2, 4, 1), false);
        towerBlock.put(placedLoc.getRelative(-1, 4, 2), false);
        towerBlock.put(placedLoc.getRelative(0, 4, 0), true);

        towerBlock.put(placedLoc.getRelative(2, 5, -1), false);
        towerBlock.put(placedLoc.getRelative(2, 5, 0), false);
        towerBlock.put(placedLoc.getRelative(2, 5, 1), false);
        towerBlock.put(placedLoc.getRelative(2, 5, 2), false);
        towerBlock.put(placedLoc.getRelative(1, 5, -1), false);
        towerBlock.put(placedLoc.getRelative(1, 5, 0), false);
        towerBlock.put(placedLoc.getRelative(1, 5, 1), false);
        towerBlock.put(placedLoc.getRelative(1, 5, 2), false);
        towerBlock.put(placedLoc.getRelative(0, 5, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 5, 1), false);
        towerBlock.put(placedLoc.getRelative(0, 5, 2), false);
        towerBlock.put(placedLoc.getRelative(-1, 5, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 5, 0), true);

        towerBlock.put(placedLoc.getRelative(-1, 5, 0), false);
        towerBlock.put(placedLoc.getRelative(-1, 5, 1), false);
        towerBlock.put(placedLoc.getRelative(-1, 5, 2), false);
        towerBlock.put(placedLoc.getRelative(-2, 5, -1), false);
        towerBlock.put(placedLoc.getRelative(-2, 5, 0), false);
        towerBlock.put(placedLoc.getRelative(-2, 5, 1), false);
        towerBlock.put(placedLoc.getRelative(-2, 5, 2), false);
        towerBlock.put(placedLoc.getRelative(3, 5, 2), false);
        towerBlock.put(placedLoc.getRelative(3, 5, -1), false);
        towerBlock.put(placedLoc.getRelative(2, 5, -2), false);
        towerBlock.put(placedLoc.getRelative(0, 5, -2), false);
        towerBlock.put(placedLoc.getRelative(-2, 5, -2), false);
        towerBlock.put(placedLoc.getRelative(-3, 5, 2), false);
        towerBlock.put(placedLoc.getRelative(-3, 5, -1), false);
        towerBlock.put(placedLoc.getRelative(2, 5, 3), false);
        towerBlock.put(placedLoc.getRelative(0, 5, 3), false);
        towerBlock.put(placedLoc.getRelative(-2, 5, 3), false);

        towerBlock.put(placedLoc.getRelative(3, 6, 2), false);
        towerBlock.put(placedLoc.getRelative(3, 6, 1), false);
        towerBlock.put(placedLoc.getRelative(3, 6, 0), false);
        towerBlock.put(placedLoc.getRelative(3, 6, -1), false);
        towerBlock.put(placedLoc.getRelative(2, 6, -2), false);
        towerBlock.put(placedLoc.getRelative(1, 6, -2), false);
        towerBlock.put(placedLoc.getRelative(0, 6, -2), false);
        towerBlock.put(placedLoc.getRelative(-1, 6, -2), false);
        towerBlock.put(placedLoc.getRelative(-2, 6, -2), false);
        towerBlock.put(placedLoc.getRelative(-3, 6, 2), false);
        towerBlock.put(placedLoc.getRelative(-3, 6, 1), false);
        towerBlock.put(placedLoc.getRelative(-3, 6, 0), false);
        towerBlock.put(placedLoc.getRelative(-3, 6, -1), false);
        towerBlock.put(placedLoc.getRelative(2, 6, 3), false);
        towerBlock.put(placedLoc.getRelative(1, 6, 3), false);
        towerBlock.put(placedLoc.getRelative(0, 6, 3), false);
        towerBlock.put(placedLoc.getRelative(-1, 6, 3), false);
        towerBlock.put(placedLoc.getRelative(-2, 6, 3), false);

        towerBlock.put(placedLoc.getRelative(3, 7, 2), false);
        towerBlock.put(placedLoc.getRelative(3, 7, -1), false);
        towerBlock.put(placedLoc.getRelative(2, 7, -2), false);
        towerBlock.put(placedLoc.getRelative(0, 7, -2), false);
        towerBlock.put(placedLoc.getRelative(-2, 7, -2), false);
        towerBlock.put(placedLoc.getRelative(-3, 7, 2), false);
        towerBlock.put(placedLoc.getRelative(-3, 7, -1), false);
        towerBlock.put(placedLoc.getRelative(2, 7, 3), false);
        towerBlock.put(placedLoc.getRelative(0, 7, 3), false);
        towerBlock.put(placedLoc.getRelative(-2, 7, 3), false);

        new TowerBlockPlacer(towerBlock, session, BlockFace.NORTH);
    }
}
