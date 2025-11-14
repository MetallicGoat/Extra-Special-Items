package me.metallicgoat.specialItems.customitems.handlers;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.tools.NMSHelper;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.customitems.CustomSpecialItemUseSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EndlessBoostHandler extends CustomSpecialItemUseSession {

  private static final Set<UUID> cooldownPlayers = new HashSet<>();

  public EndlessBoostHandler(PlayerUseSpecialItemEvent event) {
    super(event);
  }

  @Override
  public void run(PlayerUseSpecialItemEvent event) {
    try {
      final Player player = event.getPlayer();

      // must be using elytra
      if (!player.isGliding())
        return;

      // check cooldown
      if (!ConfigValue.endless_boost_cooldown_duration.isZero()) {
        final boolean hasCooldown = !this.cooldownPlayers.add(player.getUniqueId());

        if (hasCooldown)
          return;

        final double sec = ConfigValue.endless_boost_cooldown_duration.toMillis() / 1000D;

        setVisualCooldown(
            player,
            ConfigValue.endless_boost_icon_material,
            sec);

        Bukkit.getScheduler().runTaskLater(
            ExtraSpecialItemsPlugin.getInstance(),
            () -> this.cooldownPlayers.remove(player.getUniqueId()),
            (long) (20D * sec));
      }

      // apply boost
      NMSHelper.get().useFireworkBoost(player, ConfigValue.endless_boost_icon_material);
    } finally {
      stop();
    }
  }

  @Override
  protected void handleStop() {
  }

  private static void setVisualCooldown(Player player, ItemStack is, double cooldownSeconds) {
    final int cooldownTicks = (int) (cooldownSeconds * 20);

    player.setCooldown(is.getType(), cooldownTicks);
  }
}
