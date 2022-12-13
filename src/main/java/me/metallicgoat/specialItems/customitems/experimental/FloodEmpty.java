package me.metallicgoat.specialItems.customitems.experimental;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import de.marcely.bedwars.tools.VarParticle;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class FloodEmpty {

    public static SpecialItemUseHandler getFloodEmptyHandler(){
        System.out.println("Get Empty Handler");

        return new SpecialItemUseHandler() {
            @Override
            public Plugin getPlugin() {
                return ExtraSpecialItemsPlugin.getInstance();
            }

            @Override
            public SpecialItemUseSession openSession(PlayerUseSpecialItemEvent event) {
                final SpecialItemUseSession session = new SpecialItemUseSession(event) {
                    @Override
                    protected void handleStop() {}
                };

                event.setTakingItem(true);
                session.takeItem();

                BukkitTask test = Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> {
                    if(session.isActive()){
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

                return session;
            }
        };
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
