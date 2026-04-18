package me.metallicgoat.specialItems.customitems.handlers;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.tools.PersistentBlockData;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.customitems.CustomSpecialItemUseSession;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EggBridgerHandler extends CustomSpecialItemUseSession implements Listener {

  private BridgeBlockPlacerTask task;

  public EggBridgerHandler(PlayerUseSpecialItemEvent event) {
    super(event);
  }

  @Override
  public boolean isStoppedWithPlayerQuit() {
    return false;
  }

  @Override
  public void run(PlayerUseSpecialItemEvent event) {
    this.takeItem();

    final Player player = event.getPlayer();
    final Arena arena = event.getArena();
    final Team team = arena.getPlayerTeam(player);
    final DyeColor color = team != null ? team.getDyeColor() : DyeColor.WHITE;
    final PersistentBlockData data = PersistentBlockData.fromMaterial(ConfigValue.egg_bridger_block_material).getDyedData(color);

    // spawn egg
    final Egg egg = player.launchProjectile(Egg.class);
    egg.getVelocity().multiply(1.2);

    // start task
    this.task = new BridgeBlockPlacerTask(egg, player, player.getLocation(), this, arena, data);
    this.task.start();

    Bukkit.getPluginManager().registerEvents(this, ExtraSpecialItemsPlugin.getInstance());
  }

  @Override
  protected void handleStop() {
    if (this.task == null)
      return;

    HandlerList.unregisterAll(this);
    this.task.clean();
    this.task = null;
  }

  @EventHandler
  public void onCreatureSpawn(PlayerEggThrowEvent event) {
    if (event.getEgg() == this.task.egg)
      event.setHatching(false);
  }

  @EventHandler
  public void onPlayerDamage(EntityDamageEvent event) {
    if (event.getEntity() == this.task.player)
      event.setDamage(Math.min(event.getDamage(), ConfigValue.egg_bridger_clutch_fall_damage_cap));
  }

  private static class BridgeBlockPlacerTask extends BukkitRunnable {
    private final Egg egg;
    private final Player player;
    private final CustomSpecialItemUseSession session;
    private final Arena arena;
    private final PersistentBlockData data;
    private final Location throwLocation;

    public BridgeBlockPlacerTask(Egg egg, Player player, Location throwLocation, CustomSpecialItemUseSession session, Arena arena, PersistentBlockData data) {
      this.egg = egg;
      this.player = player;
      this.session = session;
      this.arena = arena;
      this.data = data;
      this.throwLocation = throwLocation;
    }

    public void start() {
      runTaskTimer(ExtraSpecialItemsPlugin.getInstance(), 0L, 1L);
    }

    public void clean() {
      cancel();
      this.egg.remove();
    }

    @Override
    public void run() {
      final Location eggLocation = this.egg.getLocation();

      if (this.egg.isValid()
          && this.throwLocation.distanceSquared(eggLocation) <= ConfigValue.egg_bridger_max_length * ConfigValue.egg_bridger_max_length
          && this.throwLocation.getY() - eggLocation.getY() <= ConfigValue.egg_bridger_max_y_variation) {

        // Slight delay so the egg does not fall on the new blocks
        Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> {
          // Don't check session, since egg may hit, but some blocks still may be placed
          if (this.arena.getStatus() != ArenaStatus.RUNNING)
            return;

          // Down spawn blocks that may spawn in the player
          final Location playerLoc = this.player.getLocation();

          if (playerLoc.getY() < eggLocation.getY() + 2
              && Math.abs(playerLoc.getX() - eggLocation.getX()) < 2
              && Math.abs(playerLoc.getZ() - eggLocation.getZ()) < 2) {
            return;
          }

          if (this.throwLocation.distanceSquared(eggLocation.clone().add(0, 1, 0)) > 12.25D) {
            placeBlock(eggLocation.clone().subtract(0.0D, 1.0D, 0.0D).getBlock());
            placeBlock(eggLocation.clone().subtract(1.0D, 1.0D, 0.0D).getBlock());
            placeBlock(eggLocation.clone().subtract(0.0D, 1.0D, 1.0D).getBlock());
            placeBlock(eggLocation.clone().subtract(1.0D, 1.0D, 1.0D).getBlock());

            ConfigValue.egg_bridger_place_sound.play(eggLocation);
          }
        }, 2L);

      } else {
        this.session.stop();
        clean();
      }
    }

    private void placeBlock(Block block) {
      if (!this.session.isPlaceable(block))
        return;

      this.data.place(block, true);
      this.arena.setBlockPlayerPlaced(block, true);
    }
  }
}