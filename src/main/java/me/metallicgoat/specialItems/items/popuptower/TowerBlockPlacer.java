package me.metallicgoat.specialItems.items.popuptower;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import de.marcely.bedwars.tools.Helper;
import de.marcely.bedwars.tools.Pair;
import de.marcely.bedwars.tools.PersistentBlockData;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.utils.XSound;
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

    private static BukkitTask task;
    private static final Material ladderMaterial = Helper.get().getMaterialByName("LADDER");

    public TowerBlockPlacer(Queue<Pair<Block, Boolean>> towerBlock, SpecialItemUseSession session, BlockFace face) {

        if((session != null && session.getEvent() == null) || session == null || !session.isActive()){
            return;
        }

        final Arena arena = session.getEvent().getArena();
        final Team team = arena.getPlayerTeam(session.getEvent().getPlayer());
        final DyeColor color = team != null ? team.getDyeColor():DyeColor.WHITE;

        task = Bukkit.getScheduler().runTaskTimer(plugin(), () -> {
            if(session.isActive()) {
                for (int i = 0; i < ConfigValue.tower_block_placed_per_interval; i++) {
                    if (towerBlock.peek() != null) {

                        final Pair<Block, Boolean> blockBooleanPair = towerBlock.poll();

                        //Get next block
                        final Block block = blockBooleanPair.getKey();

                        //Is block there?
                        if (block != null && block.getType().equals(Material.AIR)) {
                            //Is block inside region
                            if (arena.canPlaceBlockAt(block.getLocation())) {
                                XSound.matchXSound(ConfigValue.tower_place_place_sound).play(block.getLocation());
                                PlaceBlock(arena, Boolean.TRUE.equals(blockBooleanPair.getValue()), block, face, color);
                            }
                        }
                    } else {
                        task.cancel();
                        session.stop();
                        return;
                    }
                }
            }else{
                task.cancel();
            }
        }, 0L, ConfigValue.tower_block_place_interval);
    }

    private void PlaceBlock(Arena arena, boolean isLadder, Block b, BlockFace face, DyeColor color){

        if(ConfigValue.dye_tower_ukraine){
            if(((int) b.getLocation().getY()) % 2 != 0)
                color = DyeColor.BLUE;
            else
                color = DyeColor.YELLOW;
        }

        if (!isLadder) {
            final PersistentBlockData data = PersistentBlockData.fromMaterial(ConfigValue.tower_block_material).getDyedData(color);
            data.place(b, true);
        } else {
            if(ladderMaterial == null)
                return;

            b.setType(ladderMaterial);

            if (b.getType() == ladderMaterial) {
                final BlockState state = b.getState();
                final Ladder lad = new Ladder();
                lad.setFacingDirection(face.getOppositeFace());
                state.setData(lad);
                state.update();
            }
        }
        arena.setBlockPlayerPlaced(b, true);
    }

    private static ExtraSpecialItems plugin(){
        return ExtraSpecialItems.getInstance();
    }
}
