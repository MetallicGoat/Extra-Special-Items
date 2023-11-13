package me.metallicgoat.specialItems.customitems.use;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.message.Message;
import java.util.ArrayList;
import java.util.HashMap;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.customitems.CustomSpecialItemUseSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitRunnable;

public class SilverfishHandler extends CustomSpecialItemUseSession {

  public static final HashMap<Arena, Silverfish> arenaSilverfishHashMap = new HashMap<>();
  public static final ArrayList<Snowball> snowballs = new ArrayList<>();
  public static HashMap<Silverfish, Team> silverfishTeamHashMap = new HashMap<>();

  public SilverfishHandler(PlayerUseSpecialItemEvent event) {
    super(event);
  }

  public static void updateDisplayName(Team team, Silverfish silverfish) {
    if (ConfigValue.silverfish_name_tag == null || ConfigValue.silverfish_name_tag.isEmpty())
      return;

    final String teamName = team.getDisplayName();
    final String color = team.getChatColor().toString();
    final int amountOfTags = ConfigValue.silverfish_name_tag.size();
    final long time = ConfigValue.silverfish_life_duration / amountOfTags;

    silverfish.setCustomNameVisible(true);

    new BukkitRunnable() {
      int i = 0;

      @Override
      public void run() {
        if (silverfishTeamHashMap.containsKey(silverfish) && !silverfish.isDead()) {
          if (i < amountOfTags) {
            final String unformattedDisplayName = ConfigValue.silverfish_name_tag.get(i);
            final String displayName = Message.build(unformattedDisplayName != null ? unformattedDisplayName : "")
                .placeholder("team-color", color)
                .placeholder("team-name", teamName)
                .done();

            silverfish.setCustomName(displayName);
          } else {
            cancel();
            return;
          }
          i++;
        } else {
          cancel();
        }
      }
    }.runTaskTimer(ExtraSpecialItemsPlugin.getInstance(), 0L, time);
  }

  @Override
  public void run(PlayerUseSpecialItemEvent event) {
    this.takeItem();

    final Player player = event.getPlayer();
    final Snowball snowball = player.launchProjectile(Snowball.class);
    snowballs.add(snowball);

    Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> snowballs.remove(snowball), 150L);

    this.stop(); // TODO dont stop here
  }

  @Override
  protected void handleStop() {
  }
}