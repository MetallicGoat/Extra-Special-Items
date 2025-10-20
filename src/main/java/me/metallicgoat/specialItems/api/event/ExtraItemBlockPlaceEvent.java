package me.metallicgoat.specialItems.api.event;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.arena.ArenaEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItem;
import me.metallicgoat.specialItems.api.ExtraSpecialItemType;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a block is being placed by an extra special item.
 */
public class ExtraItemBlockPlaceEvent extends Event implements ArenaEvent, Cancellable {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Arena arena;
  private final Block block;
  private final SpecialItem item;

  private boolean cancelled = false;

  public ExtraItemBlockPlaceEvent(Arena arena, Block block, SpecialItem item) {
    this.arena = arena;
    this.block = block;
    this.item = item;
  }

  /**
   * Gets the arena in which the block is placed.
   *
   * @return the arena
   */
  @Override
  public Arena getArena() {
    return this.arena;
  }

  /**
   * Gets the block at which something is being placed.
   *
   * @return the block being placed at
   */
  public Block getBlock() {
    return this.block;
  }

  /**
   * Gets the special item which is placing the block.
   *
   * @return the special item
   */
  public SpecialItem getItem() {
    return this.item;
  }

  /**
   * Gets the type of the special item which is placing the block.
   *
   * @return the special item type
   */
  public ExtraSpecialItemType getItemType() {
    return ExtraSpecialItemType.from(this.item);
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(boolean cancel) {
    this.cancelled = cancel;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
