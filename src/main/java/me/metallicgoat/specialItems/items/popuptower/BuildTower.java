package me.metallicgoat.specialItems.items.popuptower;

import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import de.marcely.bedwars.tools.Pair;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayDeque;
import java.util.Queue;

public class BuildTower {

    private final BlockFace direction;
    private final Block chest;
    private final Queue<Pair<Block, Boolean>> towerBlock = new ArrayDeque<>();

    public BuildTower(Block chest, SpecialItemUseSession session, BlockFace direction) {
        this.direction = direction;
        this.chest = chest;

        addToQueue(-1, 0, -2, false);
        addToQueue(-2, 0, -1, false);
        addToQueue(-2, 0, 0, false);
        addToQueue(-1, 0, 1, false);
        addToQueue(0, 0, 1, false);
        addToQueue(1, 0, 1, false);
        addToQueue(2, 0, 0, false);
        addToQueue(2, 0, -1, false);
        addToQueue(1, 0, -2, false);
        addToQueue(0, 0, 0, true);

        addToQueue(-1, 1, -2, false);
        addToQueue(-2, 1, -1, false);
        addToQueue(-2, 1, 0, false);
        addToQueue(-1, 1, 1, false);
        addToQueue(0, 1, 1, false);
        addToQueue(1, 1, 1, false);
        addToQueue(2, 1, 0, false);
        addToQueue(2, 1, 0, false);
        addToQueue(2, 1, -1, false);
        addToQueue(1, 1, -2, false);
        addToQueue(0, 1, 0, true);

        addToQueue(-1, 2, -2, false);
        addToQueue(-2, 2, -1, false);
        addToQueue(-2, 2, 0, false);
        addToQueue(-1, 2, 1, false);
        addToQueue(0, 2, 1, false);
        addToQueue(1, 2, 1, false);
        addToQueue(2, 2, 0, false);
        addToQueue(2, 2, -1, false);
        addToQueue(1, 2, -2, false);
        addToQueue(0, 2, 0, true);

        addToQueue(0, 3, -2, false);
        addToQueue(-1, 3, -2, false);
        addToQueue(-2, 3, -1, false);
        addToQueue(-2, 3, 0, false);
        addToQueue(-1, 3, 1, false);
        addToQueue(0, 3, 1, false);
        addToQueue(1, 3, 1, false);
        addToQueue(2, 3, 0, false);
        addToQueue(2, 3, -1, false);
        addToQueue(1, 3, -2, false);
        addToQueue(0, 3, 0, true);

        addToQueue(0, 4, -2, false);
        addToQueue(-1, 4, -2, false);
        addToQueue(-2, 4, -1, false);
        addToQueue(-2, 4, 0, false);
        addToQueue(-1, 4, 1, false);
        addToQueue(0, 4, 1, false);
        addToQueue(1, 4, 1, false);
        addToQueue(2, 4, 0, false);
        addToQueue(2, 4, -1, false);
        addToQueue(1, 4, -2, false);
        addToQueue(0, 4, 0, true);

        addToQueue(-2, 5, 1, false);
        addToQueue(-2, 5, 0, false);
        addToQueue(-2, 5, -1, false);
        addToQueue(-2, 5, -2, false);
        addToQueue(-1, 5, 1, false);
        addToQueue(-1, 5, 0, false);
        addToQueue(-1, 5, -1, false);
        addToQueue(-1, 5, -2, false);
        addToQueue(0, 5, 1, false);
        addToQueue(0, 5, -1, false);
        addToQueue(0, 5, -2, false);
        addToQueue(1, 5, 1, false);
        addToQueue(0, 5, 0, true);

        addToQueue(1, 5, 0, false);
        addToQueue(1, 5, -1, false);
        addToQueue(1, 5, -2, false);
        addToQueue(2, 5, 1, false);
        addToQueue(2, 5, 0, false);
        addToQueue(2, 5, -1, false);
        addToQueue(2, 5, -2, false);
        addToQueue(-3, 5, -2, false);
        addToQueue(-3, 5, 1, false);
        addToQueue(-2, 5, 2, false);
        addToQueue(0, 5, 2, false);
        addToQueue(2, 5, 2, false);
        addToQueue(3, 5, -2, false);
        addToQueue(3, 5, 1, false);
        addToQueue(-2, 5, -3, false);
        addToQueue(0, 5, -3, false);
        addToQueue(2, 5, -3, false);


        addToQueue(-3, 6, -2, false);
        addToQueue(-3, 6, -1, false);
        addToQueue(-3, 6, 0, false);
        addToQueue(-3, 6, 1, false);
        addToQueue(-2, 6, 2, false);
        addToQueue(-1, 6, 2, false);
        addToQueue(0, 6, 2, false);
        addToQueue(1, 6, 2, false);
        addToQueue(2, 6, 2, false);
        addToQueue(3, 6, -2, false);
        addToQueue(3, 6, -1, false);
        addToQueue(3, 6, 0, false);
        addToQueue(3, 6, 1, false);
        addToQueue(-2, 6, -3, false);
        addToQueue(-1, 6, -3, false);
        addToQueue(0, 6, -3, false);
        addToQueue(1, 6, -3, false);
        addToQueue(2, 6, -3, false);

        addToQueue(-3, 7, -2, false);
        addToQueue(-3, 7, 1, false);
        addToQueue(-2, 7, 2, false);
        addToQueue(0, 7, 2, false);
        addToQueue(2, 7, 2, false);
        addToQueue(3, 7, -2, false);
        addToQueue(3, 7, 1, false);
        addToQueue(-2, 7, -3, false);
        addToQueue(0, 7, -3, false);
        addToQueue(2, 7, -3, false);

        new TowerBlockPlacer(towerBlock, session, direction, chest.getY());
    }

    private void addToQueue(int x, int height, int y, boolean ladder){
        Block block = null;
        switch(direction) {
            case NORTH:
                block = chest.getRelative(x, height, y);
                break;
            case SOUTH:
                block = chest.getRelative(x * -1, height, y * -1);
                break;
            case EAST:
                block = chest.getRelative(y * -1, height, x);
                break;
            case WEST:
                block = chest.getRelative(y, height, x * -1);
                break;
        }

        if(block == null)
            return;

        Pair<Block, Boolean> pair = new Pair<>(block, ladder);
        towerBlock.add(pair);
    }
}
