package me.metallicgoat.specialItems.customitems.handlers;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.tools.Helper;
import me.metallicgoat.specialItems.api.event.PlayerUseSpecialItemCommandEvent;
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
    this.takeItem();

    final Player player = event.getPlayer();
    final Location loc = player.getLocation();

    final PlayerUseSpecialItemCommandEvent apiEvent = new PlayerUseSpecialItemCommandEvent(event, this.command, this.console);
    Bukkit.getPluginManager().callEvent(apiEvent);

    final String commandFormatted = Message.build(apiEvent.getCommand())
        .placeholder("player", player.getName())
        .placeholder("player-display-name", Helper.get().getPlayerDisplayName(player))
        .placeholder("x", (int) loc.getX())
        .placeholder("y", (int) loc.getY())
        .placeholder("z", (int) loc.getZ())
        .placeholder("yaw", (int) loc.getYaw())
        .placeholder("pitch", (int) loc.getPitch())
        .done();

    // run command
    Bukkit.getServer().dispatchCommand(apiEvent.isAsConsole() ? Bukkit.getServer().getConsoleSender() : player, commandFormatted);

    this.stop();
  }

  @Override
  protected void handleStop() {
  }
}