package me.metallicgoat.specialItems.customitems.use;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.tools.Helper;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.customitems.CustomSpecialItemUseSession;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;

public class IceBridgerHandler extends CustomSpecialItemUseSession {

    private BukkitTask task;

    public IceBridgerHandler(PlayerUseSpecialItemEvent event) {
        super(event);
    }

    @Override
    public void run(PlayerUseSpecialItemEvent event) {
        event.setTakingItem(true);
        this.takeItem();

        final Player player = event.getPlayer();
        final Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(event.getPlayer());

        if(arena != null){

            final AtomicInteger i = new AtomicInteger(2);
            final Location l = player.getLocation();

            task = Bukkit.getScheduler().runTaskTimer(ExtraSpecialItemsPlugin.getInstance(), () -> {
                if(i.get() <= ConfigValue.ice_bridger_max_distance && this.isActive()){

                    final Location lookingStraight = new Location(l.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), 0);
                    final int yaw = (int) l.getYaw() % 180;
                    final Location blockLoc = lookingStraight.add(lookingStraight.getDirection().multiply(i.get())).add(0, -1, 0);
                    final Block block = blockLoc.getBlock();
                    final World world = block.getWorld();
                    final Sound sound = Helper.get().getSoundByName("BLOCK_SNOW_BREAK"); // TODO move to config

                    setIce(arena, block);

                    if(sound != null)
                        world.playSound(blockLoc, sound, 1, 1);

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
                    this.stop();
                    task.cancel();
                }
            }, 0L, 2L);
        }
    }

    private void setIce(Arena arena, Block block){
        if(arena.canPlaceBlockAt(block.getLocation()) && block.getType() == Material.AIR){
            block.setType(ConfigValue.ice_bridger_material);

            Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> {
                if(arena.getStatus() == ArenaStatus.RUNNING) {
                    block.setType(Material.AIR);
                }
            }, 70L);
        }
    }

    @Override
    protected void handleStop() {
        if(task != null)
            task.cancel();
    }
}
