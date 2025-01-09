package me.metallicgoat.specialItems.customitems.handlers;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.tools.NMSHelper;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.customitems.CustomSpecialItemUseSession;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


public class SlingShotHandler extends CustomSpecialItemUseSession {

  private static final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();
  private static final Map<UUID, BukkitTask> cooldownTasks = new ConcurrentHashMap<>();

  private final UUID playerId;

  public SlingShotHandler(PlayerUseSpecialItemEvent event) {
    super(event);

    this.playerId = event.getPlayer().getUniqueId();
  }

  @Override
  public void run(PlayerUseSpecialItemEvent event) {
    final Player player = event.getPlayer();
    final double remainingSeconds = getCooldownTimeLeft(this.playerId);

    // Check if player is in cooldown
    if (remainingSeconds > 0) {
      Message.build(ConfigValue.slingshot_cooldown_message)
          .placeholder("seconds", (int) remainingSeconds)
          .send(player);

      return;
    }

    this.takeItem();

    // Apply launch velocity
    final Vector direction = player.getLocation().getDirection();
    direction.multiply(ConfigValue.slingshot_force);

    // Limitar o boost vertical
    final double yBoost = direction.getY() + ConfigValue.slingshot_height_boost;
    direction.setY(Math.min(yBoost, ConfigValue.slingshot_max_y_boost));

    player.setVelocity(direction);

    // Set cooldown
    cooldowns.put(this.playerId, System.currentTimeMillis() + (ConfigValue.slingshot_cooldown_seconds * 1000L));

    // Show cooldown in action bar
    startCooldownTask(player);

    // Play sound effect
    if (ConfigValue.slingshot_use_sound != null)
      player.playSound(player.getLocation(), ConfigValue.slingshot_use_sound, 1.0f, 1.0f);

  }

  private double getCooldownTimeLeft(UUID playerId) {
    if (!cooldowns.containsKey(playerId))
      return 0;

    final long cooldownEnd = cooldowns.get(playerId);

    if (cooldownEnd <= 0) {
      cooldowns.remove(playerId);
      return 0;
    }

    return (cooldownEnd - System.currentTimeMillis()) / 1000D;
  }


  private String formatCooldownBar(double secondsLeft) {
    final int filledBars = (int) Math.ceil((secondsLeft / ConfigValue.slingshot_cooldown_seconds) * ConfigValue.slingshot_cooldown_bars);
    final StringBuilder bar = new StringBuilder();

    // Add filled bars in green
    bar.append(ChatColor.GREEN);

    for (int i = 0; i < filledBars; i++)
      bar.append("┇");

    // Add empty bars in red
    if (filledBars < ConfigValue.slingshot_cooldown_bars) {
      bar.append(ChatColor.RED);

      for (int i = filledBars; i < ConfigValue.slingshot_cooldown_bars; i++)
        bar.append("┇");

    }

    return bar.toString();
  }

  private void startCooldownTask(Player player) {
    BukkitTask task = cooldownTasks.get(playerId);

    // Cancel existing task if any
    if (task != null)
      task.cancel();

    task = new BukkitRunnable() {
      @Override
      public void run() {
        final double secondsLeft = getCooldownTimeLeft(playerId);

        if (secondsLeft <= 0) {
          NMSHelper.get().showActionbar(player, "");
          cooldowns.remove(playerId);
          cooldownTasks.remove(playerId);
          this.cancel();
          return;
        }

        final String cooldownBar = formatCooldownBar(secondsLeft);
        final String seconds = ConfigValue.slingshot_cooldown_seconds_format.format(secondsLeft);

        NMSHelper.get().showActionbar(player,
            Message.build(ConfigValue.slingshot_action_bar)
                .placeholder("cooldown-bar", cooldownBar)
                .placeholder("seconds", seconds)
                .placeholder("item-name", ConfigValue.slingshot_icon_name)
                .done()
        );
      }
    }.runTaskTimer(ExtraSpecialItemsPlugin.getInstance(), 0L, 2L);

    cooldownTasks.put(this.playerId, task);
  }

  @Override
  protected void handleStop() {
    final BukkitTask task = cooldownTasks.get(this.playerId);

    if (task != null)
      task.cancel();

    cooldownTasks.remove(this.playerId);
    cooldowns.remove(this.playerId);
  }
}
