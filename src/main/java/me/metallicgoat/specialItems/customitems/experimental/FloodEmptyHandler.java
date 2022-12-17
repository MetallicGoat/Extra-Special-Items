package me.metallicgoat.specialItems.customitems.experimental;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.tools.VarParticle;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.customitems.CustomSpecialItemUseSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FloodEmptyHandler extends CustomSpecialItemUseSession {

    private BukkitTask task;
    private List<Block> starterBlocks;
    private int blocksBroken = 0;

    public FloodEmptyHandler(PlayerUseSpecialItemEvent event) {
        super(event);
    }

    @Override
    public void run(PlayerUseSpecialItemEvent event) {
        final Block clickedBlock = event.getClickedBlock();
        final BlockFace face = event.getClickedBlockFace();

        // Block  is null or under another
        if(clickedBlock == null || face == null)
            return;

        // Take item
        event.setTakingItem(true);
        this.takeItem();

        starterBlocks = Collections.singletonList(clickedBlock);

        task = Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> {
            if(this.isActive()){
                final List<Block> newStarters = new ArrayList<>();

                for(Block block : starterBlocks){
                    if(blocksBroken >= 30) {
                        stopTask();
                        break;
                    }

                    if(block.getType() != Material.AIR && event.getArena().isBlockPlayerPlaced(block)){
                        newStarters.addAll(getContacts(block));
                        removeBlock(block);
                        blocksBroken++;
                    }
                }

                // Shuffle so final blocks are taken in a random order
                Collections.shuffle(newStarters);
                starterBlocks = newStarters;
            }
        }, 4L);
    }

    @Override
    protected void handleStop() {
        if(task != null)
            task.cancel();
    }

    private void stopTask(){
        task.cancel();
        this.stop();
    }

    private List<Block> getContacts(Block starter){
        final List<Block> contacts = new ArrayList<>();

        contacts.add(starter.getRelative(BlockFace.NORTH));
        contacts.add(starter.getRelative(BlockFace.SOUTH));
        contacts.add(starter.getRelative(BlockFace.EAST));
        contacts.add(starter.getRelative(BlockFace.WEST));
        contacts.add(starter.getRelative(BlockFace.UP));
        contacts.add(starter.getRelative(BlockFace.DOWN));

        return contacts;
    }

    private void removeBlock(Block block){
        final Location location = block.getLocation();

        block.setType(Material.AIR);
        location.add(.5, .5, .5);
        VarParticle.PARTICLE_SMOKE.play(location);
        location.add(.1, .1, .1);
        VarParticle.PARTICLE_CLOUD.play(location);
    }
}
