package me.metallicgoat.specialItems.items.popuptower;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import de.marcely.bedwars.tools.Pair;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import me.metallicgoat.specialItems.utils.XBlock;
import me.metallicgoat.specialItems.utils.XMaterial;
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

    private BukkitTask task;

    public TowerBlockPlacer(Queue<Pair<Block, Boolean>> towerBlock, SpecialItemUseSession session, BlockFace face) {

        if(session == null || !session.isActive()){
            return;
        }

        final Arena arena = session.getEvent().getArena();
        final Team team = arena.getPlayerTeam(session.getEvent().getPlayer());
        final DyeColor color = team != null ? team.getDyeColor():DyeColor.WHITE;

        long time = plugin().getConfig().getLong("PopUpTower.Block-Place-Interval");
        int amountToPlace = plugin().getConfig().getInt("PopUpTower.Blocks-Placed-Per-Interval");
        String sound = plugin().getConfig().getString("PopUpTower.Sound");

        task = Bukkit.getScheduler().runTaskTimer(plugin(), () -> {
            if(session.isActive()) {
                for (int i = 0; i < amountToPlace; i++) {
                    if (towerBlock.peek() != null) {

                        Pair<Block, Boolean> blockBooleanPair = towerBlock.poll();

                        //Get next block
                        Block block = blockBooleanPair.getKey();

                        //Is block there?
                        if (block.getType().equals(Material.AIR)) {
                            //Is block inside region
                            if (arena.canPlaceBlockAt(block.getLocation())) {
                                XSound.valueOf(sound).play(block.getLocation());
                                PlaceBlock(arena, blockBooleanPair.getValue(), block, face, color);
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
        }, 0L, time);
    }

    private void PlaceBlock(Arena arena, boolean ladder, Block b, BlockFace face, DyeColor color){

        assert XMaterial.LADDER.parseMaterial() != null;
        if (!ladder && XMaterial.matchXMaterial(color.name() + "_WOOL").isPresent()) {
            XMaterial woolMat = XMaterial.matchXMaterial(color.name() + "_WOOL").get();
            XBlock.setType(b, woolMat);
        } else {
            b.setType(XMaterial.LADDER.parseMaterial());

            //Make this better (Error if ladder is not successfully placed from above. Ex. on a slab)
            if (b.getType() == XMaterial.LADDER.parseMaterial()) {
                BlockState state = b.getState();
                Ladder lad = new Ladder();
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
