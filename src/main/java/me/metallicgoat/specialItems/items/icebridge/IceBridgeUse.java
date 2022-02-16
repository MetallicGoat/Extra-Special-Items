package me.metallicgoat.specialItems.items.icebridge;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import de.marcely.bedwars.tools.Helper;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import me.metallicgoat.specialItems.utils.XSound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;

public class IceBridgeUse {

    private BukkitTask task;

    public void createIceBridge(PlayerUseSpecialItemEvent e, SpecialItemUseSession session){

        e.setTakingItem(true);
        session.takeItem();

        final Player player = e.getPlayer();
        final double length = plugin().getConfig().getDouble("Ice-Bridger.Max-Distance");
        final Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(e.getPlayer());
        final BukkitScheduler scheduler = plugin().getServer().getScheduler();

        if(arena != null){

            final AtomicInteger i = new AtomicInteger(2);
            final Location l = player.getLocation();

            task = scheduler.runTaskTimer(plugin(), () -> {
                if(i.get() <= length && session.isActive()){

                    final Location lookingStraight = new Location(l.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), 0);
                    final int yaw = (int) l.getYaw() % 180;
                    final Location blockLoc = lookingStraight.add(lookingStraight.getDirection().multiply(i.get())).add(0, -1, 0);
                    final Block block = blockLoc.getBlock();
                    final World world = block.getWorld();

                    setIce(arena, block);

                    XSound.BLOCK_SNOW_BREAK.play(player);

                    if ((yaw < 45 || yaw >= 135) && (yaw < -135 || yaw >= -45)) {
                        setIce(arena, world.getBlockAt(blockLoc.add(1, 0, 0)));
                        setIce(arena, world.getBlockAt(blockLoc.add(-1, 0, 0)));
                        setIce(arena, world.getBlockAt(blockLoc.add(2, 0, 0)));
                        setIce(arena, world.getBlockAt(blockLoc.add(-2, 0, 0)));
                        setIce(arena, world.getBlockAt(blockLoc.add(3, 0, 0)));
                        setIce(arena, world.getBlockAt(blockLoc.add(-3, 0, 0)));

                    }else{
                        setIce(arena, world.getBlockAt(blockLoc.add(0, 0, 1)));
                        setIce(arena, world.getBlockAt(blockLoc.add(0, 0, -1)));
                        setIce(arena, world.getBlockAt(blockLoc.add(0, 0, 2)));
                        setIce(arena, world.getBlockAt(blockLoc.add(0, 0, -2)));
                        setIce(arena, world.getBlockAt(blockLoc.add(0, 0, 3)));
                        setIce(arena, world.getBlockAt(blockLoc.add(0, 0, -3)));
                    }
                    i.getAndIncrement();
                }else{

                    //clear bridge

                    session.stop();
                    task.cancel();
                }

            }, 0L, 2L);

        }

    }
    private void setIce(Arena arena, Block block){
        if(arena.canPlaceBlockAt(block.getLocation()) && block.getType() == Material.AIR){
            block.setType(Helper.get().getMaterialByName("ICE"));

            final BukkitScheduler scheduler = plugin().getServer().getScheduler();
            scheduler.runTaskLater(plugin(), () -> {
                if(arena.getStatus() == ArenaStatus.RUNNING) {
                    block.setType(Material.AIR);
                }
            }, 70L);
        }
    }

    private static ExtraSpecialItems plugin(){
        return ExtraSpecialItems.getInstance();
    }
}
