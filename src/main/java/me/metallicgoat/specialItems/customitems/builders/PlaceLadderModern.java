package me.metallicgoat.specialItems.customitems.builders;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Ladder;

public class PlaceLadderModern {

  // Do this in a separate class, so we do not trigger ClassNotFound on old versions

  public static void placeLadder(Block block, BlockFace direction) {
    final Ladder ladder = (Ladder) block.getBlockData();

    ladder.setFacing(direction);
    block.setBlockData(ladder);
  }
}
