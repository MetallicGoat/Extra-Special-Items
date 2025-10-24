package me.metallicgoat.specialItems.api.event;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.arena.ArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.tools.Validate;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Gets called when a player is about to use the {@link me.metallicgoat.specialItems.api.ExtraSpecialItemType#COMMAND} special item.
 * <p>
 * It is not possible to cancel this event. Listen to {@link de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent} instead.
 * </p>
 */
public class PlayerUseSpecialItemCommandEvent extends PlayerEvent implements ArenaEvent {

  private static final HandlerList HANDLERS = new HandlerList();

  private final PlayerUseSpecialItemEvent origin;

  private String command;
  private boolean asConsole;

  public PlayerUseSpecialItemCommandEvent(PlayerUseSpecialItemEvent origin, String command, boolean asConsole) {
    super(origin.getPlayer());

    this.origin = origin;
    this.command = command;
    this.asConsole = asConsole;
  }

  @Override
  public Arena getArena() {
    return this.origin.getArena();
  }

  /**
   * Gets the unformatted command to be executed.
   *
   * @return The unformatted command
   */
  public String getCommand() {
    return this.command;
  }

  /**
   * Sets the unformatted command to be executed.
   *
   * @param command The new unformatted command
   */
  public void setCommand(String command) {
    Validate.notNull(command, "command");

    this.command = command;
  }

  /**
   * Gets whether the command will be executed as console.
   *
   * @return <code>true</code> if executed as console, <code>false</code> if executed as player
   */
  public boolean isAsConsole() {
    return this.asConsole;
  }

  /**
   * Sets whether the command will be executed as console.
   *
   * @param asConsole <code>true</code> to execute as console, <code>false</code> to execute as player
   */
  public void setAsConsole(boolean asConsole) {
    this.asConsole = asConsole;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
