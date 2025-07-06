package me.metallicgoat.specialItems.customitems.handlers;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class EggBridgerHandler extends CustomSpecialItemUseSession implements Listener {

  private static final List<UUID> nerfFallDamage = new CopyOnWriteArrayList<>();

  private BukkitTask task;
  private Egg egg;

  public EggBridgerHandler(PlayerUseSpecialItemEvent event) {
    super(event);
  }

  @Override
  public void run(PlayerUseSpecialItemEvent event) {
    this.takeItem();

    final Player player = event.getPlayer();
    final Arena arena = event.getArena();
    final Team team = arena.getPlayerTeam(player);
    final DyeColor color = team != null ? team.getDyeColor() : DyeColor.WHITE;
    final PersistentBlockData data = PersistentBlockData.fromMaterial(ConfigValue.egg_bridger_block_material).getDyedData(color);

    this.egg = player.launchProjectile(Egg.class);
    this.egg.getVelocity().multiply(1.2);
    this.task = new BridgeBlockPlacerTask(this.egg, player, player.getLocation(), this, arena, data).runTaskTimer(ExtraSpecialItemsPlugin.getInstance(), 0L, 1L);

    nerfFallDamage.add(player.getUniqueId());

    Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> {
      nerfFallDamage.remove(player.getUniqueId());
    }, 60L);

    Bukkit.getPluginManager().registerEvents(this, ExtraSpecialItemsPlugin.getInstance());
  }

  @Override
  protected void handleStop() {
    HandlerList.unregisterAll(this);

    if (this.egg != null && this.egg.isValid())
      egg.remove();

    if (this.task != null)
      this.task.cancel();
  }

  @EventHandler
  public void onCreatureSpawn(PlayerEggThrowEvent event) {
    if (event.getEgg() == this.egg)
      event.setHatching(false);
  }

  @EventHandler
  public void onPlayerDamage(EntityDamageEvent event) {
    if (event.getEntity().getType() != EntityType.PLAYER)
      return;

    if (nerfFallDamage.contains(event.getEntity().getUniqueId())) {
      event.setDamage(Math.min(event.getDamage(), 4));
    }
  }

  private static class BridgeBlockPlacerTask extends BukkitRunnable {
    private final Egg egg;
    private final Player player;
    private final SpecialItemUseSession session;
    private final Arena arena;
    private final PersistentBlockData data;
    private final Location throwLocation;

    public BridgeBlockPlacerTask(Egg egg, Player player, Location throwLocation, SpecialItemUseSession session, Arena arena, PersistentBlockData data) {
      this.egg = egg;
      this.player = player;
      this.session = session;
      this.arena = arena;
      this.data = data;
      this.throwLocation = throwLocation;
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
          if (player.getLocation().getY() < eggLocation.getY() + 2
              && Math.abs(player.getLocation().getX() - eggLocation.getX()) < 2
              && Math.abs(player.getLocation().getZ() - eggLocation.getZ()) < 2) {
            return;
          }

          if (this.throwLocation.distanceSquared(eggLocation.clone().add(0, 1, 0)) > 12.25D) {
            placeBlock(eggLocation.clone().subtract(0.0D, 1.0D, 0.0D).getBlock());
            placeBlock(eggLocation.clone().subtract(1.0D, 1.0D, 0.0D).getBlock());
            placeBlock(eggLocation.clone().subtract(0.0D, 1.0D, 1.0D).getBlock());
            placeBlock(eggLocation.clone().subtract(1.0D, 1.0D, 1.0D).getBlock());

            this.egg.getWorld().playSound(eggLocation, ConfigValue.egg_bridger_place_sound, 1, 1);
          }
        }, 2L);

      } else {
        this.session.stop();
        cancel();
      }
    }

    private void placeBlock(Block block) {
      // Is block there?
      if (!block.getType().equals(Material.AIR))
        return;

      // Is block inside region
      if (this.arena != null && this.arena.canPlaceBlockAt(block.getLocation())) {
        this.data.place(block, true);
        this.arena.setBlockPlayerPlaced(block, true);
      }
    }
  }
}