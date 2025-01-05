package me.metallicgoat.specialItems.customitems.handlers;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.tools.Helper;
import de.marcely.bedwars.tools.NMSHelper;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.customitems.CustomSpecialItemUseSession;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SlingShotHandler extends CustomSpecialItemUseSession {

  private static final Map<UUID, Long> cooldowns = new HashMap<>();
  private static final Map<UUID, BukkitTask> cooldownTasks = new HashMap<>();

  public SlingShotHandler(PlayerUseSpecialItemEvent event) {
    super(event);
  }

  @Override
  public void run(PlayerUseSpecialItemEvent event) {
    Player player = event.getPlayer();
    UUID playerId = player.getUniqueId();

    // Check if player is in cooldown
    if (isInCooldown(playerId)) {
      long remainingSeconds = getCooldownTimeLeft(playerId);
      player.sendMessage(Message.build(ConfigValue.slingshot_cooldown_message.replace("%seconds%", String.valueOf(remainingSeconds))).done());
      return;
    }

    this.takeItem();

    // Apply launch velocity
    Vector direction = player.getLocation().getDirection();
    direction.multiply(ConfigValue.slingshot_force);

    // Limitar o boost vertical
    double yBoost = direction.getY() + ConfigValue.slingshot_height_boost;
    direction.setY(Math.min(yBoost, ConfigValue.slingshot_max_y_boost));

    player.setVelocity(direction);

    // Set cooldown
    setCooldown(playerId);

    // Show cooldown in action bar
    startCooldownTask(player);

    // Play sound effect
    Sound sound = ConfigValue.slingshot_use_sound;
    if (sound != null)
      player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
  }

  private boolean isInCooldown(UUID playerId) {
    if (!cooldowns.containsKey(playerId))
      return false;

    long cooldownEnd = cooldowns.get(playerId);
    if (System.currentTimeMillis() >= cooldownEnd) {
      cooldowns.remove(playerId);
      return false;
    }

    return true;
  }

  private long getCooldownTimeLeft(UUID playerId) {
    if (!cooldowns.containsKey(playerId))
      return 0;

    long cooldownEnd = cooldowns.get(playerId);
    return (cooldownEnd - System.currentTimeMillis()) / 1000;
  }

  private void setCooldown(UUID playerId) {
    cooldowns.put(playerId, System.currentTimeMillis() + (ConfigValue.slingshot_cooldown_seconds * 1000L));
  }

  private String formatCooldownBar(double secondsLeft) {
    int filledBars = (int) Math.ceil((secondsLeft / ConfigValue.slingshot_cooldown_seconds) * ConfigValue.slingshot_cooldown_bars);
    StringBuilder bar = new StringBuilder();

    // Add filled bars in green
    bar.append("§a");
    for (int i = 0; i < filledBars; i++) {
      bar.append("┇");
    }

    // Add empty bars in red
    if (filledBars < ConfigValue.slingshot_cooldown_bars) {
      bar.append("§c");
      for (int i = filledBars; i < ConfigValue.slingshot_cooldown_bars; i++) {
        bar.append("┇");
      }
    }

    return bar.toString();
  }

  private void startCooldownTask(Player player) {
    UUID playerId = player.getUniqueId();

    // Cancel existing task if any
    BukkitTask existingTask = cooldownTasks.get(playerId);
    if (existingTask != null) {
      existingTask.cancel();
    }

    BukkitTask task = new BukkitRunnable() {
      double secondsLeft = ConfigValue.slingshot_cooldown_seconds;

      @Override
      public void run() {
        if (secondsLeft <= 0 || !isInCooldown(playerId)) {
          NMSHelper.get().showActionbar(player, "");
          cooldowns.remove(playerId);
          cooldownTasks.remove(playerId);
          this.cancel();
          return;
        }

        String cooldownBar = formatCooldownBar(secondsLeft);
        NMSHelper.get().showActionbar(player, Message.build("&f" + ConfigValue.slingshot_icon_name + " &r" + cooldownBar + " &f" + String.format("%.1f", secondsLeft).replace('.', ',') + "s").done());
        secondsLeft -= 0.1;
      }
    }.runTaskTimer(ExtraSpecialItemsPlugin.getInstance(), 0L, 2L);

    cooldownTasks.put(playerId, task);
  }

  @Override
  protected void handleStop() {
    // Cancel all cooldown tasks
    cooldownTasks.values().forEach(BukkitTask::cancel);
    cooldowns.clear();
    cooldownTasks.clear();
  }
}
