package me.metallicgoat.specialItems.items.popuptower;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import de.marcely.bedwars.tools.Helper;
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
import org.bukkit.material.Ladder;
import org.bukkit.scheduler.BukkitTask;

import java.util.Queue;

public class TowerBlockPlacer {

    private int currLevel = 1;
    private BukkitTask task;
    private final Material ladderMaterial = Helper.get().getMaterialByName("LADDER");
    private final PersistentBlockData blockData = PersistentBlockData.fromMaterial(ConfigValue.tower_block_material);

    public TowerBlockPlacer(Queue<Pair<Block, Boolean>> towerBlock, SpecialItemUseSession session, BlockFace face) {
        if (session == null || session.getEvent() == null || !session.isActive())
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
                    PlaceBlock(arena, Boolean.TRUE.equals(blockBooleanPair.getValue()), block, face, color);
                }
            }

            currLevel++;
        }, 0L, ConfigValue.tower_block_place_interval);
    }

    private void PlaceBlock(Arena arena, boolean isLadder, Block block, BlockFace face, DyeColor color) {

        // Re-Dye
        if (ConfigValue.dye_tower_ukraine)
            color = currLevel > 4 ? DyeColor.BLUE : DyeColor.YELLOW;

        // Place Block
        if (!isLadder) {
            final PersistentBlockData data = blockData.getDyedData(color);
            data.place(block, true);
        } else {
            if (ladderMaterial == null)
                return;

            block.setType(ladderMaterial);

            if (block.getType() == ladderMaterial) {
                final BlockState state = block.getState();
                final Ladder lad = new Ladder();
                lad.setFacingDirection(face.getOppositeFace());
                state.setData(lad);
                state.update();
            }
        }
        arena.setBlockPlayerPlaced(block, true);
    }
}
