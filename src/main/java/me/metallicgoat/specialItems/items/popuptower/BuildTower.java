package me.metallicgoat.specialItems.items.popuptower;

import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.LinkedHashMap;

public class BuildTower {

    private static BlockFace direction;
    private static Block chest;
    private static final LinkedHashMap<Block, Boolean> towerBlock = new LinkedHashMap<>();

    public BuildTower(Block chest, SpecialItemUseSession session, BlockFace direction) {
        BuildTower.direction = direction;
        BuildTower.chest = chest;

        addToMap(-1, 0, -2, false);
        addToMap(-2, 0, -1, false);
        addToMap(-2, 0, 0, false);
        addToMap(-1, 0, 1, false);
        addToMap(0, 0, 1, false);
        addToMap(1, 0, 1, false);
        addToMap(2, 0, 0, false);
        addToMap(2, 0, -1, false);
        addToMap(1, 0, -2, false);
        addToMap(0, 0, 0, true);

        addToMap(-1, 1, -2, false);
        addToMap(-2, 1, -1, false);
        addToMap(-2, 1, 0, false);
        addToMap(-1, 1, 1, false);
        addToMap(0, 1, 1, false);
        addToMap(1, 1, 1, false);
        addToMap(2, 1, 0, false);
        addToMap(2, 1, 0, false);
        addToMap(2, 1, -1, false);
        addToMap(1, 1, -2, false);
        addToMap(0, 1, 0, true);

        addToMap(-1, 2, -2, false);
        addToMap(-2, 2, -1, false);
        addToMap(-2, 2, 0, false);
        addToMap(-1, 2, 1, false);
        addToMap(0, 2, 1, false);
        addToMap(1, 2, 1, false);
        addToMap(2, 2, 0, false);
        addToMap(2, 2, -1, false);
        addToMap(1, 2, -2, false);
        addToMap(0, 2, 0, true);

        addToMap(0, 3, -2, false);
        addToMap(-1, 3, -2, false);
        addToMap(-2, 3, -1, false);
        addToMap(-2, 3, 0, false);
        addToMap(-1, 3, 1, false);
        addToMap(0, 3, 1, false);
        addToMap(1, 3, 1, false);
        addToMap(2, 3, 0, false);
        addToMap(2, 3, -1, false);
        addToMap(1, 3, -2, false);
        addToMap(0, 3, 0, true);

        addToMap(0, 4, -2, false);
        addToMap(-1, 4, -2, false);
        addToMap(-2, 4, -1, false);
        addToMap(-2, 4, 0, false);
        addToMap(-1, 4, 1, false);
        addToMap(0, 4, 1, false);
        addToMap(1, 4, 1, false);
        addToMap(2, 4, 0, false);
        addToMap(2, 4, -1, false);
        addToMap(1, 4, -2, false);
        addToMap(0, 4, 0, true);

        addToMap(-2, 5, 1, false);
        addToMap(-2, 5, 0, false);
        addToMap(-2, 5, -1, false);
        addToMap(-2, 5, -2, false);
        addToMap(-1, 5, 1, false);
        addToMap(-1, 5, 0, false);
        addToMap(-1, 5, -1, false);
        addToMap(-1, 5, -2, false);
        addToMap(0, 5, 1, false);
        addToMap(0, 5, -1, false);
        addToMap(0, 5, -2, false);
        addToMap(1, 5, 1, false);
        addToMap(0, 5, 0, true);

        addToMap(1, 5, 0, false);
        addToMap(1, 5, -1, false);
        addToMap(1, 5, -2, false);
        addToMap(2, 5, 1, false);
        addToMap(2, 5, 0, false);
        addToMap(2, 5, -1, false);
        addToMap(2, 5, -2, false);
        addToMap(-3, 5, -2, false);
        addToMap(-3, 5, 1, false);
        addToMap(-2, 5, 2, false);
        addToMap(0, 5, 2, false);
        addToMap(2, 5, 2, false);
        addToMap(3, 5, -2, false);
        addToMap(3, 5, 1, false);
        addToMap(-2, 5, -3, false);
        addToMap(0, 5, -3, false);
        addToMap(2, 5, -3, false);


        addToMap(-3, 6, -2, false);
        addToMap(-3, 6, -1, false);
        addToMap(-3, 6, 0, false);
        addToMap(-3, 6, 1, false);
        addToMap(-2, 6, 2, false);
        addToMap(-1, 6, 2, false);
        addToMap(0, 6, 2, false);
        addToMap(1, 6, 2, false);
        addToMap(2, 6, 2, false);
        addToMap(3, 6, -2, false);
        addToMap(3, 6, -1, false);
        addToMap(3, 6, 0, false);
        addToMap(3, 6, 1, false);
        addToMap(-2, 6, -3, false);
        addToMap(-1, 6, -3, false);
        addToMap(0, 6, -3, false);
        addToMap(1, 6, -3, false);
        addToMap(2, 6, -3, false);

        addToMap(-3, 7, -2, false);
        addToMap(-3, 7, 1, false);
        addToMap(-2, 7, 2, false);
        addToMap(0, 7, 2, false);
        addToMap(2, 7, 2, false);
        addToMap(3, 7, -2, false);
        addToMap(3, 7, 1, false);
        addToMap(-2, 7, -3, false);
        addToMap(0, 7, -3, false);
        addToMap(2, 7, -3, false);

        new TowerBlockPlacer(towerBlock, session, direction);
    }

    private static void addToMap(int x, int height, int y, boolean ladder){
        switch(direction) {
            case NORTH:
                towerBlock.put(chest.getRelative(x, height, y), ladder);
                break;
            case SOUTH:
                towerBlock.put(chest.getRelative(x * -1, height, y * -1), ladder);
                break;
            case EAST:
                towerBlock.put(chest.getRelative(y * -1, height, x), ladder);
                break;
            case WEST:
                towerBlock.put(chest.getRelative(y, height, x * -1), ladder);
                break;
        }
    }
}
