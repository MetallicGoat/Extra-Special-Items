package me.metallicgoat.specialItems.items.eggbridge;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.utils.XSound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class EggBridgeThrow {
    public void buildEggBridge(PlayerUseSpecialItemEvent e, SpecialItemUseSession session) {
        e.setTakingItem(true);
        session.takeItem();

        final Player player = e.getPlayer();
        final Arena arena = e.getArena();
        final Team team = arena.getPlayerTeam(player);
        final DyeColor col = team != null ? team.getDyeColor():DyeColor.WHITE;
        final Egg egg = player.launchProjectile(Egg.class);

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

            this.task = Bukkit.getScheduler().runTaskTimer(ExtraSpecialItems.getInstance(), this, 0L, 1L);
        }

        public void run() {
            Location eggLocation = egg.getLocation();

            if (!egg.isDead()
                    && playerLocation.distanceSquared(eggLocation) <= ConfigValue.egg_bridger_max_length * ConfigValue.egg_bridger_max_length
                    && playerLocation.getY() - eggLocation.getY() <= ConfigValue.egg_bridger_max_y_variation){

                Bukkit.getScheduler().runTaskLater(ExtraSpecialItems.getInstance(),() -> {
                    if(playerLocation.distanceSquared(eggLocation.clone().add(0, 1, 0)) > 12.25D) {

                        final List<Block> blocks = new ArrayList<>();

                        blocks.add(eggLocation.clone().subtract(0.0D, 2.0D, 0.0D).getBlock());
                        blocks.add(eggLocation.clone().subtract(1.0D, 2.0D, 0.0D).getBlock());
                        blocks.add(eggLocation.clone().subtract(0.0D, 2.0D, 1.0D).getBlock());

                        for(Block block:blocks)
                            new EggBridgeBlockPlacer(block, color, arena);

                        XSound.matchXSound(ConfigValue.egg_bridger_place_sound).play(eggLocation);
                    }
                }, 2L);

            } else {
                session.stop();
                task.cancel();
            }
        }
    }
}
