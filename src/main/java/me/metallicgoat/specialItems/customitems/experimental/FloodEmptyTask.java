package me.metallicgoat.specialItems.customitems.experimental;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import de.marcely.bedwars.tools.VarParticle;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class FloodEmptyTask {

    public static void flood(PlayerUseSpecialItemEvent event, SpecialItemUseSession session, Block currentBlock) {
        final Arena arena = event.getArena();

        System.out.println("run");

        removeBlock(currentBlock);

        if(arena.isBlockPlayerPlaced(currentBlock)){
            Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> {
                if(session.isActive()){
                    final List<Block> blocks = new ArrayList<>();
                    blocks.add(currentBlock.getRelative(BlockFace.DOWN));
                    blocks.add(currentBlock.getRelative(BlockFace.UP));
                    blocks.add(currentBlock.getRelative(BlockFace.NORTH));
                    blocks.add(currentBlock.getRelative(BlockFace.SOUTH));
                    blocks.add(currentBlock.getRelative(BlockFace.EAST));
                    blocks.add(currentBlock.getRelative(BlockFace.WEST));

                    for(Block block : blocks){
                        if(block.getType() != Material.AIR && arena.isBlockPlayerPlaced(block)){
                            removeBlock(block);
                            flood(event, session, block);
                        }
                    }
                }
            }, 4L);
        }
    }

    private static void removeBlock(Block block){
        block.setType(Material.AIR);

        final Location location = block.getLocation();
        location.add(.5, .5, .5);
        VarParticle.PARTICLE_SMOKE.play(location);
        location.add(.1, .1, .1);
        VarParticle.PARTICLE_CLOUD.play(location);
    }
}
