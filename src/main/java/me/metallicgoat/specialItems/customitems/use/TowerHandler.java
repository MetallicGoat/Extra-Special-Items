package me.metallicgoat.specialItems.customitems.use;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import me.metallicgoat.specialItems.customitems.CustomSpecialItemUseSession;
import me.metallicgoat.specialItems.customitems.builders.TowerBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TowerHandler extends CustomSpecialItemUseSession {

  private TowerBuilder builder;

  public TowerHandler(PlayerUseSpecialItemEvent event) {
    super(event);
  }

  @Override
  public void run(PlayerUseSpecialItemEvent event) {
    final Player player = event.getPlayer();
    final Arena arena = event.getArena();
    final Block clicked = event.getClickedBlock();
    final BlockFace blockFace = event.getClickedBlockFace();

    // Check if placeable
    if (clicked == null ||
        blockFace == null ||
        blockFace == BlockFace.DOWN ||
        clicked.getRelative(blockFace).getType() != Material.AIR ||
        !arena.canPlaceBlockAt(clicked.getRelative(blockFace))) {

      event.setTakingItem(false);
      this.stop();
      return;
    }

    // Take item
    this.takeItem();

    final Block relative = clicked.getRelative(blockFace);
    double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;

    if (rotation < 0.0D)
      rotation += 360.0D;

    if (45.0D <= rotation && rotation < 135.0D) {
      builder = new TowerBuilder(relative, this, BlockFace.SOUTH);
    } else if (225.0D <= rotation && rotation < 315.0D) {
      builder = new TowerBuilder(relative, this, BlockFace.NORTH);
    } else if (135.0D <= rotation && rotation < 225.0D) {
      builder = new TowerBuilder(relative, this, BlockFace.WEST);
    } else { // if (0.0D <= rotation && rotation < 45.0D || 315.0D <= rotation && rotation < 360.0D) {
      builder = new TowerBuilder(relative, this, BlockFace.EAST);
    }

    builder.build();
  }

  @Override
  protected void handleStop() {
    if (builder != null)
      builder.cancel();
  }
}