package me.metallicgoat.specialItems.customitems.use;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.tools.Helper;
import me.metallicgoat.specialItems.customitems.CustomSpecialItemUseSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CommandItemHandler extends CustomSpecialItemUseSession {

  private final String command;
  private final boolean console;

  public CommandItemHandler(PlayerUseSpecialItemEvent event, String command, boolean console) {
    super(event);

    this.command = command;
    this.console = console;
  }

  @Override
  public void run(PlayerUseSpecialItemEvent event) {
    event.setTakingItem(true);
    this.takeItem();

    final Player player = event.getPlayer();
    final Location loc = player.getLocation();
    final String commandFormatted = Message.build(command)
        .placeholder("player", player.getName())
        .placeholder("player-display-name", Helper.get().getPlayerDisplayName(player))
        .placeholder("x", (int) loc.getX())
        .placeholder("y", (int) loc.getY())
        .placeholder("z", (int) loc.getZ())
        .placeholder("yaw", (int) loc.getYaw())
        .placeholder("pitch", (int) loc.getPitch())
        .done();

    // run command
    Bukkit.getServer().dispatchCommand(console ? Bukkit.getServer().getConsoleSender() : player, commandFormatted);

    this.stop();
  }

  @Override
  protected void handleStop() {
  }
}