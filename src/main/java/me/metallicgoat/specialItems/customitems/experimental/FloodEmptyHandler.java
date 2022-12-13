package me.metallicgoat.specialItems.customitems.experimental;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.tools.VarParticle;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.customitems.CustomSpecialItemUseSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class FloodEmptyHandler extends CustomSpecialItemUseSession {

    private BukkitTask task;

    public FloodEmptyHandler(PlayerUseSpecialItemEvent event) {
        super(event);
    }

    @Override
    public void run(PlayerUseSpecialItemEvent event) {
        event.setTakingItem(true);
        this.takeItem();

        task = Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> {
            if(this.isActive()){
                final List<Block> blocks = new ArrayList<>();
                //blocks.add(currentBlock.getRelative(BlockFace.DOWN));
                //blocks.add(currentBlock.getRelative(BlockFace.UP));
                //blocks.add(currentBlock.getRelative(BlockFace.NORTH));
                //blocks.add(currentBlock.getRelative(BlockFace.SOUTH));
                //blocks.add(currentBlock.getRelative(BlockFace.EAST));
                //blocks.add(currentBlock.getRelative(BlockFace.WEST));

                for(Block block : blocks){
                    if(block.getType() != Material.AIR && event.getArena().isBlockPlayerPlaced(block)){
                        //removeBlock(block);
                        //flood(event, session, block);
                    }
                }
            }
        }, 4L);
    }

    @Override
    protected void handleStop() {
        if(task != null)
            task.cancel();
    }

    private void removeBlock(Block block){
        block.setType(Material.AIR);

        final Location location = block.getLocation();
        location.add(.5, .5, .5);
        VarParticle.PARTICLE_SMOKE.play(location);
        location.add(.1, .1, .1);
        VarParticle.PARTICLE_CLOUD.play(location);
    }
}
