package me.metallicgoat.specialItems.customitems.use;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import de.marcely.bedwars.tools.PersistentBlockData;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.customitems.CustomSpecialItemUseSession;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class EggBridgerHandler extends CustomSpecialItemUseSession {

    public EggBridgerHandler(PlayerUseSpecialItemEvent event) {
        super(event);
    }

    private BukkitTask task;

    @Override
    public void run(PlayerUseSpecialItemEvent event) {
        event.setTakingItem(true);
        this.takeItem();

        final Player player = event.getPlayer();
        final Arena arena = event.getArena();
        final Team team = arena.getPlayerTeam(player);
        final DyeColor color = team != null ? team.getDyeColor() : DyeColor.WHITE;
        final Egg egg = player.launchProjectile(Egg.class);

         task = new BridgeBlockPlacerTask(egg, player.getLocation(), this, arena, color)
                .runTaskTimer(ExtraSpecialItemsPlugin.getInstance(), 0L, 1L);
    }

    @Override
    protected void handleStop() {
        if(task != null)
            task.cancel();
    }

    static class BridgeBlockPlacerTask extends BukkitRunnable {
        private final Egg egg;
        private final SpecialItemUseSession session;
        private final Arena arena;
        private final DyeColor color;
        private final Location playerLocation;

        public BridgeBlockPlacerTask(Egg egg, Location playerLocation, SpecialItemUseSession session, Arena arena, DyeColor color) {
            this.egg = egg;
            this.session = session;
            this.arena = arena;
            this.color = color;
            this.playerLocation = playerLocation;
        }

        public void run() {
            final Location eggLocation = egg.getLocation();

            if (!egg.isDead()
                    && playerLocation.distance(eggLocation) <= ConfigValue.egg_bridger_max_length
                    && playerLocation.getY() - eggLocation.getY() <= ConfigValue.egg_bridger_max_y_variation){

                Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(),() -> {
                    if(playerLocation.distanceSquared(eggLocation.clone().add(0, 1, 0)) > 12.25D) {

                        final List<Block> blocks = new ArrayList<>();

                        blocks.add(eggLocation.clone().subtract(0.0D, 2.0D, 0.0D).getBlock());
                        blocks.add(eggLocation.clone().subtract(1.0D, 2.0D, 0.0D).getBlock());
                        blocks.add(eggLocation.clone().subtract(0.0D, 2.0D, 1.0D).getBlock());

                        for(Block block:blocks)
                            placeBlock(block, color, arena);

                        eggLocation.getWorld().playSound(eggLocation, ConfigValue.egg_bridger_place_sound, 1, 1);
                    }
                }, 2L);

            } else {
                session.stop();
                cancel();
            }
        }

        public void placeBlock(Block b, DyeColor color, Arena arena) {
            // Is block there?
            if (!b.getType().equals(Material.AIR))
                return;

            // Is block inside region
            if (arena != null && arena.canPlaceBlockAt(b.getLocation())) {
                final PersistentBlockData data = PersistentBlockData.fromMaterial(ConfigValue.egg_bridger_block_material).getDyedData(color);

                data.place(b, true);
                arena.setBlockPlayerPlaced(b, true);
            }
        }
    }
}