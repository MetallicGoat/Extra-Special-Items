package me.metallicgoat.specialItems.customitems.use;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.tools.Helper;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.customitems.CustomSpecialItemUseSession;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

public class IceBridgerHandler extends CustomSpecialItemUseSession {

  private BukkitTask task;
  public Arena arena;

  public IceBridgerHandler(PlayerUseSpecialItemEvent event) {
    super(event);
  }

  @Override
  public void run(PlayerUseSpecialItemEvent event) {
    this.takeItem();

    this.arena = event.getArena();
    final Location playerLocation = event.getPlayer().getLocation().clone();

    playerLocation.setPitch(0);

    this.task = Bukkit.getScheduler().runTaskTimer(ExtraSpecialItemsPlugin.getInstance(), new Runnable() {
      int i = 2;

      @Override
      public void run() {
        if (this.i <= ConfigValue.ice_bridger_max_distance && isActive()) {
          final int yaw = (int) playerLocation.getYaw() % 180;
          final Location blockLoc = playerLocation.add(playerLocation.getDirection().multiply(this.i)).add(0, -1, 0);
          final Block block = blockLoc.getBlock();
          final World world = block.getWorld();
          final Sound sound = Helper.get().getSoundByName("BLOCK_SNOW_BREAK"); // TODO move to config

          setIce(block);

          if (sound != null)
            world.playSound(blockLoc, sound, 1, 1);

          if ((yaw < 45 || yaw >= 135) && (yaw < -135 || yaw >= -45)) {
            setIce(block.getRelative(1, 0, 0));
            setIce(block.getRelative(-1, 0, 0));
            setIce(block.getRelative(2, 0, 0));
            setIce(block.getRelative(-2, 0, 0));
            setIce(block.getRelative(3, 0, 0));
            setIce(block.getRelative(-3, 0, 0));

          } else {
            setIce(block.getRelative(0, 0, 1));
            setIce(block.getRelative(0, 0, -1));
            setIce(block.getRelative(0, 0, 2));
            setIce(block.getRelative(0, 0, -2));
            setIce(block.getRelative(0, 0, 3));
            setIce(block.getRelative(0, 0, -3));
          }

          this.i++;
        } else {
          stop();
          task.cancel();
        }
      }
    }, 0, 2L);
  }

  private void setIce(Block block) {
    if (this.arena.canPlaceBlockAt(block.getLocation()) && block.getType() == Material.AIR) {
      block.setType(ConfigValue.ice_bridger_material);

      Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> {
        if (this.arena.getStatus() == ArenaStatus.RUNNING)
          block.setType(Material.AIR);

      }, 70L);
    }
  }

  @Override
  protected void handleStop() {
    if (task != null)
      task.cancel();
  }
}
