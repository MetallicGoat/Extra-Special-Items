package me.metallicgoat.specialItems.customitems.handlers;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.message.Message;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.customitems.CustomSpecialItemUseSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.scheduler.BukkitTask;

public class SilverfishHandler extends CustomSpecialItemUseSession implements Listener {

  private Arena arena;
  private Team team;
  private Snowball snowball;
  private Silverfish silverfish;
  private BukkitTask task;

  public SilverfishHandler(PlayerUseSpecialItemEvent event) {
    super(event);
  }

  @Override
  public void run(PlayerUseSpecialItemEvent event) {
    this.takeItem();

    final Player player = event.getPlayer();

    this.arena = event.getArena();
    this.team = event.getArena().getPlayerTeam(player);
    this.snowball = player.launchProjectile(Snowball.class);
    this.task = Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> this.snowball.remove(), 8 * 20L);

    Bukkit.getPluginManager().registerEvents(this, ExtraSpecialItemsPlugin.getInstance());
  }

  private void startUpdatingDisplayName() {
    if (ConfigValue.silverfish_name_tag == null || ConfigValue.silverfish_name_tag.isEmpty())
      return;

    final String teamName = team.getDisplayName();
    final String color = team.getBungeeChatColor().toString();
    final int amountOfTags = ConfigValue.silverfish_name_tag.size();
    final long updateTime = ConfigValue.silverfish_life_duration / amountOfTags;

    this.silverfish.setCustomNameVisible(true);

    task = Bukkit.getScheduler().runTaskTimer(ExtraSpecialItemsPlugin.getInstance(), new Runnable() {
      int i = 0;

      @Override
      public void run() {
        if (!silverfish.isValid() || i >= amountOfTags) {
          stop();
          task.cancel();
          return;
        }

        final String unformattedDisplayName = ConfigValue.silverfish_name_tag.get(i);
        final String displayName = Message.build(unformattedDisplayName != null ? unformattedDisplayName : "")
            .placeholder("team-color", color)
            .placeholder("team-name", teamName)
            .placeholder("sqr", "■")
            .done();

        silverfish.setCustomName(displayName);
        i++;
      }
    }, 0, updateTime);
  }

  @Override
  protected void handleStop() {
    HandlerList.unregisterAll(this);

    if (this.task != null)
      this.task.cancel();

    if (this.snowball != null && this.snowball.isValid())
      this.snowball.remove();

    if (this.silverfish != null && this.silverfish.isValid())
      this.silverfish.remove();

  }

  @EventHandler
  public void onProjectileHit(ProjectileHitEvent event) {
    if (event.getEntity() != this.snowball)
      return;

    // No point in removing the snowball
    if (this.task != null)
      this.task.cancel();

    this.silverfish = (Silverfish) this.snowball.getWorld().spawnEntity(this.snowball.getLocation(), EntityType.SILVERFISH);

    startUpdatingDisplayName();
  }

  @EventHandler
  public void onEntityTarget(EntityTargetLivingEntityEvent event) {
    if (event.getEntity() != this.silverfish)
      return;

    if (!(event.getTarget() instanceof Player)) {
      event.setCancelled(true);
      return;
    }

    final Player player = (Player) event.getTarget();

    // Player is on the same team or not actually playing
    if (!this.arena.getPlayers().contains(player) || this.arena.getPlayerTeam(player) == this.team)
      event.setCancelled(true);

  }

  @EventHandler
  public void onSilverfishDeath(EntityDeathEvent event) {
    if (event.getEntity() == this.silverfish)
      stop();
  }

  @EventHandler
  public void onSilverfishBurrow(EntityChangeBlockEvent event) {
    if (event.getEntity() == this.silverfish)
      event.setCancelled(true);
  }

  @EventHandler
  public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
    if (event.getDamager() == this.silverfish) {
      // Stop silverfish attacking team members
      if (event.getEntity() instanceof Player) {
        final Player player = (Player) event.getEntity();
        final Arena playerArena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);

        if (playerArena == null || this.arena != playerArena || this.arena.getPlayerTeam(player) == this.team) {
          event.setCancelled(true);
          return;
        }
      }

      event.setDamage(1.5);

      // Stop player attacking silverfish
    } else if (event.getEntity() == this.silverfish && event.getDamager() instanceof Player) {
      final Player player = (Player) event.getDamager();
      final Arena playerArena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);

      if (playerArena == null || this.arena != playerArena || this.arena.getPlayerTeam(player) == this.team)
        event.setCancelled(true);

    }
  }
}