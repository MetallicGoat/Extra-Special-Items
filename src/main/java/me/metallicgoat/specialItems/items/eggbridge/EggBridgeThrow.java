package me.metallicgoat.specialItems.items.eggbridge;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.Main;
import me.metallicgoat.specialItems.utils.XSound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class EggBridgeThrow {
    public void buildEggBridge(PlayerUseSpecialItemEvent e, SpecialItemUseSession session) {
        final Player player = e.getPlayer();
        Arena arena = e.getArena();

        e.setTakingItem(true);
        session.takeItem();

        final DyeColor col = arena.getPlayerTeam(player).getDyeColor();

        Egg egg = player.launchProjectile(Egg.class);

        new BridgeBlockPlacerTask(egg, player.getLocation(), session, arena, col);
    }

    private static class BridgeBlockPlacerTask implements Runnable {
        private final Egg egg;
        private final SpecialItemUseSession session;
        private final Arena arena;
        private final DyeColor color;
        private final Location playerLocation;
        private final BukkitTask task;

        public BridgeBlockPlacerTask(Egg egg, Location playerLocation, SpecialItemUseSession session, Arena arena, DyeColor color) {
            this.egg = egg;
            this.session = session;
            this.arena = arena;
            this.color = color;
            this.playerLocation = playerLocation;
            this.task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this, 0L, 1L);
        }

        //TODO: try player y + 1 (Block y -1) (Distance config)

        public void run() {
            Location eggLocation = egg.getLocation().add(0, 1, 0);
            if (!egg.isDead() && playerLocation.distance(egg.getLocation()) <= 30.0D && playerLocation.getY() - egg.getLocation().getY() <= 18.0D) {
                if (playerLocation.distance(eggLocation) > 3.5D) {
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(),() -> {

                        Block block1 = eggLocation.clone().subtract(0.0D, 3.0D, 0.0D).getBlock();
                        new EggBridgeBlockPlacer(block1, color, arena);

                        Block block2 = eggLocation.clone().subtract(1.0D, 3.0D, 0.0D).getBlock();
                        new EggBridgeBlockPlacer(block2, color, arena);

                        Block block3 = eggLocation.clone().subtract(0.0D, 3.0D, 1.0D).getBlock();
                        new EggBridgeBlockPlacer(block3, color, arena);

                        XSound.ENTITY_CHICKEN_EGG.play(eggLocation);

                    }, 2L);
                }

            } else {
                session.stop();
                task.cancel();
            }
        }
    }
}
