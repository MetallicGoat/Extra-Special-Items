package me.metallicgoat.specialItems.customitems.builders;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import de.marcely.bedwars.tools.NMSHelper;
import de.marcely.bedwars.tools.Pair;
import de.marcely.bedwars.tools.PersistentBlockData;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.config.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayDeque;
import java.util.Queue;

public class TowerBuilder {

  private static boolean placeLadderLegacy;
  private static PersistentBlockData blockData;
  private final SpecialItemUseSession session;
  private final BlockFace direction;
  private final Block chest;
  private final Queue<Pair<Block, Boolean>> towerBlock = new ArrayDeque<>();
  private BukkitTask task;

  public TowerBuilder(Block chest, SpecialItemUseSession session, BlockFace direction) {
    this.session = session;
    this.direction = direction;
    this.chest = chest;

    addToQueue(-1, 0, -2, false);
    addToQueue(-2, 0, -1, false);
    addToQueue(-2, 0, 0, false);
    addToQueue(-1, 0, 1, false);
    addToQueue(0, 0, 1, false);
    addToQueue(1, 0, 1, false);
    addToQueue(2, 0, 0, false);
    addToQueue(2, 0, -1, false);
    addToQueue(1, 0, -2, false);
    addToQueue(0, 0, 0, true);

    addToQueue(-1, 1, -2, false);
    addToQueue(-2, 1, -1, false);
    addToQueue(-2, 1, 0, false);
    addToQueue(-1, 1, 1, false);
    addToQueue(0, 1, 1, false);
    addToQueue(1, 1, 1, false);
    addToQueue(2, 1, 0, false);
    addToQueue(2, 1, 0, false);
    addToQueue(2, 1, -1, false);
    addToQueue(1, 1, -2, false);
    addToQueue(0, 1, 0, true);

    addToQueue(-1, 2, -2, false);
    addToQueue(-2, 2, -1, false);
    addToQueue(-2, 2, 0, false);
    addToQueue(-1, 2, 1, false);
    addToQueue(0, 2, 1, false);
    addToQueue(1, 2, 1, false);
    addToQueue(2, 2, 0, false);
    addToQueue(2, 2, -1, false);
    addToQueue(1, 2, -2, false);
    addToQueue(0, 2, 0, true);

    addToQueue(0, 3, -2, false);
    addToQueue(-1, 3, -2, false);
    addToQueue(-2, 3, -1, false);
    addToQueue(-2, 3, 0, false);
    addToQueue(-1, 3, 1, false);
    addToQueue(0, 3, 1, false);
    addToQueue(1, 3, 1, false);
    addToQueue(2, 3, 0, false);
    addToQueue(2, 3, -1, false);
    addToQueue(1, 3, -2, false);
    addToQueue(0, 3, 0, true);

    addToQueue(0, 4, -2, false);
    addToQueue(-1, 4, -2, false);
    addToQueue(-2, 4, -1, false);
    addToQueue(-2, 4, 0, false);
    addToQueue(-1, 4, 1, false);
    addToQueue(0, 4, 1, false);
    addToQueue(1, 4, 1, false);
    addToQueue(2, 4, 0, false);
    addToQueue(2, 4, -1, false);
    addToQueue(1, 4, -2, false);
    addToQueue(0, 4, 0, true);

    addToQueue(-2, 5, 1, false);
    addToQueue(-2, 5, 0, false);
    addToQueue(-2, 5, -1, false);
    addToQueue(-2, 5, -2, false);
    addToQueue(-1, 5, 1, false);
    addToQueue(-1, 5, 0, false);
    addToQueue(-1, 5, -1, false);
    addToQueue(-1, 5, -2, false);
    addToQueue(0, 5, 1, false);
    addToQueue(0, 5, -1, false);
    addToQueue(0, 5, -2, false);
    addToQueue(1, 5, 1, false);
    addToQueue(0, 5, 0, true);

    addToQueue(1, 5, 0, false);
    addToQueue(1, 5, -1, false);
    addToQueue(1, 5, -2, false);
    addToQueue(2, 5, 1, false);
    addToQueue(2, 5, 0, false);
    addToQueue(2, 5, -1, false);
    addToQueue(2, 5, -2, false);
    addToQueue(-3, 5, -2, false);
    addToQueue(-3, 5, 1, false);
    addToQueue(-2, 5, 2, false);
    addToQueue(0, 5, 2, false);
    addToQueue(2, 5, 2, false);
    addToQueue(3, 5, -2, false);
    addToQueue(3, 5, 1, false);
    addToQueue(-2, 5, -3, false);
    addToQueue(0, 5, -3, false);
    addToQueue(2, 5, -3, false);

    addToQueue(-3, 6, -2, false);
    addToQueue(-3, 6, -1, false);
    addToQueue(-3, 6, 0, false);
    addToQueue(-3, 6, 1, false);
    addToQueue(-2, 6, 2, false);
    addToQueue(-1, 6, 2, false);
    addToQueue(0, 6, 2, false);
    addToQueue(1, 6, 2, false);
    addToQueue(2, 6, 2, false);
    addToQueue(3, 6, -2, false);
    addToQueue(3, 6, -1, false);
    addToQueue(3, 6, 0, false);
    addToQueue(3, 6, 1, false);
    addToQueue(-2, 6, -3, false);
    addToQueue(-1, 6, -3, false);
    addToQueue(0, 6, -3, false);
    addToQueue(1, 6, -3, false);
    addToQueue(2, 6, -3, false);

    addToQueue(-3, 7, -2, false);
    addToQueue(-3, 7, 1, false);
    addToQueue(-2, 7, 2, false);
    addToQueue(0, 7, 2, false);
    addToQueue(2, 7, 2, false);
    addToQueue(3, 7, -2, false);
    addToQueue(3, 7, 1, false);
    addToQueue(-2, 7, -3, false);
    addToQueue(0, 7, -3, false);
    addToQueue(2, 7, -3, false);
  }

  public static void init() {
    placeLadderLegacy = NMSHelper.get().getVersion() < 13;
    blockData = PersistentBlockData.fromMaterial(ConfigValue.tower_block_material);
  }

  private void addToQueue(int x, int height, int y, boolean ladder) {
    Block block = null;
    switch (direction) {
      case NORTH:
        block = chest.getRelative(x, height, y);
        break;
      case SOUTH:
        block = chest.getRelative(x * -1, height, y * -1);
        break;
      case EAST:
        block = chest.getRelative(y * -1, height, x);
        break;
      case WEST:
        block = chest.getRelative(y, height, x * -1);
        break;
    }

    if (block == null)
      return;

    towerBlock.add(new Pair<>(block, ladder));
  }

  public void build() {
    if (session.getEvent() == null)
      return;

    final Arena arena = session.getEvent().getArena();
    final Team team = arena.getPlayerTeam(session.getEvent().getPlayer());
    final DyeColor color = team != null ? team.getDyeColor() : DyeColor.WHITE;

    task = Bukkit.getScheduler().runTaskTimer(ExtraSpecialItemsPlugin.getInstance(), () -> {
      if (!session.isActive()) {
        task.cancel();
        return;
      }

      for (int i = 0; i < ConfigValue.tower_block_placed_per_interval; i++) {
        if (towerBlock.peek() == null) {
          task.cancel();
          session.stop();
          return;
        }

        final Pair<Block, Boolean> blockBooleanPair = towerBlock.poll();
        final Block block = blockBooleanPair.getKey();

        // Is block there?
        if (block == null || !block.getType().equals(Material.AIR))
          continue;

        // Is block inside region
        if (arena.canPlaceBlockAt(block.getLocation())) {
          block.getLocation().getWorld().playSound(block.getLocation(), ConfigValue.tower_place_place_sound, 1, 1);
          placeBlock(arena, blockBooleanPair.getValue(), block, color);
        }
      }

    }, 0L, ConfigValue.tower_block_place_interval);
  }

  private void placeBlock(Arena arena, boolean isLadder, Block block, DyeColor color) {
    // Re-Dye
    if (ConfigValue.dye_tower_ukraine)
      color = block.getY() - chest.getY() > 3 ? DyeColor.BLUE : DyeColor.YELLOW;

    // Place Block
    if (!isLadder) {
      final PersistentBlockData data = blockData.getDyedData(color);
      data.place(block, true);
    } else {
      block.setType(Material.LADDER);

      // We do not want to initialize legacy material support (causes lag)
      if (!placeLadderLegacy) {
        final Ladder ladder = (Ladder) block.getBlockData();
        ladder.setFacing(direction);
        block.setBlockData(ladder);

      } else {
        final BlockState state = block.getState();
        final org.bukkit.material.Ladder legacyLadder = new org.bukkit.material.Ladder();
        legacyLadder.setFacingDirection(direction.getOppositeFace());
        state.setData(legacyLadder);
        state.update();

      }
    }

    arena.setBlockPlayerPlaced(block, true);
  }

  public void cancel() {
    if (task != null)
      task.cancel();
  }
}
