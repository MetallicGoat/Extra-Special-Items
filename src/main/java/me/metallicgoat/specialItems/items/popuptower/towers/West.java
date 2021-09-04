package me.metallicgoat.specialItems.items.popuptower.towers;

import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.Main;
import me.metallicgoat.specialItems.items.popuptower.TowerBlockPlacer;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedHashMap;

public class West {

    private BukkitTask task;

    public West(Block chest, DyeColor color, Player p, SpecialItemUseSession session) {

        Block placedLoc = chest.getRelative(0, 1, 0);

        LinkedHashMap<Block, Boolean> towerBlock = new LinkedHashMap<>();

        towerBlock.put(placedLoc.getRelative(-2, 0, 1), false);
        towerBlock.put(placedLoc.getRelative(-1, 0, 2), false);
        towerBlock.put(placedLoc.getRelative(0, 0, 2), false);
        towerBlock.put(placedLoc.getRelative(1, 0, 1), false);
        towerBlock.put(placedLoc.getRelative(1, 0, 0), false);
        towerBlock.put(placedLoc.getRelative(1, 0, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 0, -2), false);
        towerBlock.put(placedLoc.getRelative(-1, 0, -2), false);
        towerBlock.put(placedLoc.getRelative(-2, 0, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 0, 0), true);

        towerBlock.put(placedLoc.getRelative(-2, 1, 1), false);
        towerBlock.put(placedLoc.getRelative(-1, 1, 2), false);
        towerBlock.put(placedLoc.getRelative(0, 1, 2), false);
        towerBlock.put(placedLoc.getRelative(1, 1, 1), false);
        towerBlock.put(placedLoc.getRelative(1, 1, 0), false);
        towerBlock.put(placedLoc.getRelative(1, 1, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 1, -2), false);
        towerBlock.put(placedLoc.getRelative(-1, 1, -2), false);
        towerBlock.put(placedLoc.getRelative(-2, 1, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 1, 0), true);

        towerBlock.put(placedLoc.getRelative(-2, 2, 1), false);
        towerBlock.put(placedLoc.getRelative(-1, 2, 2), false);
        towerBlock.put(placedLoc.getRelative(0, 2, 2), false);
        towerBlock.put(placedLoc.getRelative(1, 2, 1), false);
        towerBlock.put(placedLoc.getRelative(1, 2, 0), false);
        towerBlock.put(placedLoc.getRelative(1, 2, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 2, -2), false);
        towerBlock.put(placedLoc.getRelative(-1, 2, -2), false);
        towerBlock.put(placedLoc.getRelative(-2, 2, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 2, 0), true);

        towerBlock.put(placedLoc.getRelative(-2, 3, 0), false);
        towerBlock.put(placedLoc.getRelative(-2, 3, 1), false);
        towerBlock.put(placedLoc.getRelative(-1, 3, 2), false);
        towerBlock.put(placedLoc.getRelative(0, 3, 2), false);
        towerBlock.put(placedLoc.getRelative(1, 3, 1), false);
        towerBlock.put(placedLoc.getRelative(1, 3, 0), false);
        towerBlock.put(placedLoc.getRelative(1, 3, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 3, -2), false);
        towerBlock.put(placedLoc.getRelative(-1, 3, -2), false);
        towerBlock.put(placedLoc.getRelative(-2, 3, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 3, 0), true);

        towerBlock.put(placedLoc.getRelative(-2, 4, 0), false);
        towerBlock.put(placedLoc.getRelative(-2, 4, 1), false);
        towerBlock.put(placedLoc.getRelative(-1, 4, 2), false);
        towerBlock.put(placedLoc.getRelative(0, 4, 2), false);
        towerBlock.put(placedLoc.getRelative(1, 4, 1), false);
        towerBlock.put(placedLoc.getRelative(1, 4, 0), false);
        towerBlock.put(placedLoc.getRelative(1, 4, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 4, -2), false);
        towerBlock.put(placedLoc.getRelative(-1, 4, -2), false);
        towerBlock.put(placedLoc.getRelative(-2, 4, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 4, 0), true);

        towerBlock.put(placedLoc.getRelative(1, 5, 2), false);
        towerBlock.put(placedLoc.getRelative(0, 5, 2), false);
        towerBlock.put(placedLoc.getRelative(-1, 5, 2), false);
        towerBlock.put(placedLoc.getRelative(-2, 5, 2), false);
        towerBlock.put(placedLoc.getRelative(1, 5, 1), false);
        towerBlock.put(placedLoc.getRelative(0, 5, 1), false);
        towerBlock.put(placedLoc.getRelative(-1, 5, 1), false);
        towerBlock.put(placedLoc.getRelative(-2, 5, 1), false);
        towerBlock.put(placedLoc.getRelative(1, 5, 0), false);
        towerBlock.put(placedLoc.getRelative(-1, 5, 0), false);
        towerBlock.put(placedLoc.getRelative(-2, 5, 0), false);
        towerBlock.put(placedLoc.getRelative(1, 5, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 5, -1), false);
        towerBlock.put(placedLoc.getRelative(-1, 5, -1), false);
        towerBlock.put(placedLoc.getRelative(-2, 5, -1), false);
        towerBlock.put(placedLoc.getRelative(0, 5, 0), true);

        towerBlock.put(placedLoc.getRelative(1, 5, -2), false);
        towerBlock.put(placedLoc.getRelative(0, 5, -2), false);
        towerBlock.put(placedLoc.getRelative(-1, 5, -2), false);
        towerBlock.put(placedLoc.getRelative(-2, 5, -2), false);
        towerBlock.put(placedLoc.getRelative(-2, 5, 3), false);
        towerBlock.put(placedLoc.getRelative(1, 5, 3), false);
        towerBlock.put(placedLoc.getRelative(2, 5, 2), false);
        towerBlock.put(placedLoc.getRelative(2, 5, 0), false);
        towerBlock.put(placedLoc.getRelative(2, 5, -2), false);
        towerBlock.put(placedLoc.getRelative(-2, 5, -3), false);
        towerBlock.put(placedLoc.getRelative(1, 5, -3), false);
        towerBlock.put(placedLoc.getRelative(-3, 5, 2), false);
        towerBlock.put(placedLoc.getRelative(-3, 5, 0), false);
        towerBlock.put(placedLoc.getRelative(-3, 5, -2), false);


        towerBlock.put(placedLoc.getRelative(-2, 6, 3), false);
        towerBlock.put(placedLoc.getRelative(-1, 6, 3), false);
        towerBlock.put(placedLoc.getRelative(0, 6, 3), false);
        towerBlock.put(placedLoc.getRelative(1, 6, 3), false);
        towerBlock.put(placedLoc.getRelative(2, 6, 2), false);
        towerBlock.put(placedLoc.getRelative(2, 6, 1), false);
        towerBlock.put(placedLoc.getRelative(2, 6, 0), false);
        towerBlock.put(placedLoc.getRelative(2, 6, -1), false);
        towerBlock.put(placedLoc.getRelative(2, 6, -2), false);
        towerBlock.put(placedLoc.getRelative(-2, 6, -3), false);
        towerBlock.put(placedLoc.getRelative(-1, 6, -3), false);
        towerBlock.put(placedLoc.getRelative(0, 6, -3), false);
        towerBlock.put(placedLoc.getRelative(1, 6, -3), false);
        towerBlock.put(placedLoc.getRelative(-3, 6, 2), false);
        towerBlock.put(placedLoc.getRelative(-3, 6, 1), false);
        towerBlock.put(placedLoc.getRelative(-3, 6, 0), false);
        towerBlock.put(placedLoc.getRelative(-3, 6, -1), false);
        towerBlock.put(placedLoc.getRelative(-3, 6, -2), false);

        towerBlock.put(placedLoc.getRelative(-2, 7, 3), false);
        towerBlock.put(placedLoc.getRelative(1, 7, 3), false);
        towerBlock.put(placedLoc.getRelative(2, 7, 2), false);
        towerBlock.put(placedLoc.getRelative(2, 7, 0), false);
        towerBlock.put(placedLoc.getRelative(2, 7, -2), false);
        towerBlock.put(placedLoc.getRelative(-2, 7, -3), false);
        towerBlock.put(placedLoc.getRelative(1, 7, -3), false);
        towerBlock.put(placedLoc.getRelative(-3, 7, 2), false);
        towerBlock.put(placedLoc.getRelative(-3, 7, 0), false);
        towerBlock.put(placedLoc.getRelative(-3, 7, -2), false);


        task = Bukkit.getScheduler().runTaskTimer(plugin(), () -> {
            for (int i = 0; i < 2; i++) {
                if (!towerBlock.isEmpty()) {
                    Block block = towerBlock.entrySet().stream().findFirst().get().getKey();
                    if (Bukkit.getServer().getClass().getPackage().getName().contains("v1_8")) {
                        chest.getWorld().playSound(chest.getLocation(), Sound.valueOf("CHICKEN_EGG_POP"), 1.0F, 0.5F);
                    } else {
                        chest.getWorld().playSound(chest.getLocation(), Sound.valueOf("ENTITY_CHICKEN_EGG"), 1.0F, 0.5F);
                    }

                    new TowerBlockPlacer(block, color, p, towerBlock.get(block), "EAST");
                    towerBlock.remove(block);

                } else {
                    task.cancel();
                    session.stop();
                    return;
                }
            }
        }, 0L, 1);
    }
    private static Main plugin(){
        return Main.getInstance();
    }
}


