package me.metallicgoat.specialItems.customitems;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.api.event.ExtraItemBlockPlaceEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

public abstract class CustomSpecialItemUseSession extends SpecialItemUseSession {

  public CustomSpecialItemUseSession(PlayerUseSpecialItemEvent event) {
    super(event);
  }

  public abstract void run(PlayerUseSpecialItemEvent event);


  public boolean isPlaceable(Block block) {
    // Is block there?
    if (block.getType().isSolid())
      return false;

    // Is block inside region
    final Arena arena = getEvent().getArena();

    if (arena == null || !arena.canPlaceBlockAt(block.getLocation()))
      return false;

    // Ask API
    if (ExtraItemBlockPlaceEvent.getHandlerList().getRegisteredListeners().length != 0) {
      final ExtraItemBlockPlaceEvent event = new ExtraItemBlockPlaceEvent(arena, block, this.getEvent().getSpecialItem());
      Bukkit.getPluginManager().callEvent(event);

      if (event.isCancelled())
        return false;
    }

    return true;
  }
}
