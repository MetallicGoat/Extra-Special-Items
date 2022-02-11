package me.metallicgoat.specialItems.items.silverfish;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SilverfishThrow implements Listener {
    public static HashMap <Silverfish, Team> silverfishTeamHashMap = new HashMap<>();
    private static final HashMap <Arena, Silverfish> arenaSilverfishHashMap = new HashMap<>();
    private static final ArrayList<Snowball> snowballs = new ArrayList<>();

    public static void throwSilverfish(PlayerUseSpecialItemEvent e, SpecialItemUseSession session) {
        final Player player = e.getPlayer();

        e.setTakingItem(true);
        session.takeItem();

        Snowball snowball = player.launchProjectile(Snowball.class);
        snowballs.add(snowball);

        BukkitScheduler scheduler = plugin().getServer().getScheduler();
        scheduler.runTaskLater(plugin(), () -> snowballs.remove(snowball), 150L);

        session.stop();
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e){
        if(e.getEntity().getShooter() instanceof Player && e.getEntity() instanceof Snowball){
            Player player = (Player) e.getEntity().getShooter();
            Snowball snowball = (Snowball) e.getEntity();
            Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);
            if(arena != null && snowballs.contains(snowball)){
                Team team = arena.getPlayerTeam(player);
                Location loc = e.getEntity().getLocation();
                Silverfish silverfish = (Silverfish) loc.getWorld().spawnEntity(loc, EntityType.SILVERFISH);

                assert team != null;
                new UpdateDisplayName().setDisplayName(team, silverfish);

                silverfishTeamHashMap.put(silverfish, team);
                arenaSilverfishHashMap.put(arena, silverfish);
            }
        }
    }


    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent e){
        if(e.getEntity() instanceof Silverfish && e.getTarget() instanceof Player){
            Player player = (Player) e.getTarget();
            Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);
            Silverfish silverfish = (Silverfish) e.getEntity();
            if(arena != null && silverfishTeamHashMap.containsKey(silverfish)){
                Team team = arena.getPlayerTeam(player);
                assert team != null;
                if(team.equals(silverfishTeamHashMap.get(silverfish))){
                    e.setCancelled(true);
                }
            }
        }
    }

    //Possible puff of smoke
    @EventHandler
    public void onSilverfishDeath(EntityDeathEvent e){
        if(e.getEntity() instanceof Silverfish){
            Silverfish silverfish = (Silverfish) e.getEntity();
            silverfishTeamHashMap.remove(silverfish);
        }
    }

    @EventHandler
    public void onSilverfishBurrow(EntityChangeBlockEvent e){
        if (e.getEntity() instanceof Silverfish
                && silverfishTeamHashMap.containsKey((Silverfish) e.getEntity())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Silverfish){
            Silverfish silverfish = (Silverfish) e.getDamager();
            if(silverfishTeamHashMap.containsKey(silverfish)){
                e.setDamage(1.5);
            }
        }else if(e.getDamager() instanceof Player && e.getEntity() instanceof Silverfish){
            Player player = (Player) e.getDamager();
            Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);
            if(arena != null){
                Team playerTeam = arena.getPlayerTeam(player);
                Team silverfishTeam = silverfishTeamHashMap.get((Silverfish) e.getEntity());
                if(playerTeam == silverfishTeam){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onRoundEnd(RoundEndEvent e){
        Arena arena = e.getArena();
        arenaSilverfishHashMap.forEach((arena1, silverfish) -> {
            if(arena == arena1){
                silverfishTeamHashMap.remove(silverfish);
            }
        });
        arenaSilverfishHashMap.remove(arena);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent e){
        Collection<Arena> arenas = BedwarsAPI.getGameAPI().getArenaByLocation(e.getLocation());
        if(arenas != null && e.getEntity() instanceof Silverfish){
            Silverfish silverfish = (Silverfish) e.getEntity();
            e.setCancelled(false);
            BukkitScheduler scheduler = plugin().getServer().getScheduler();
            long time = plugin().getConfig().getLong("Silverfish.Life-Duration");
            scheduler.runTaskLater(plugin(), () -> {
                silverfish.remove();
                silverfishTeamHashMap.remove(silverfish);
                arenas.forEach(arena -> arenaSilverfishHashMap.remove(arena, silverfish));
            }, time);
        }
    }

    private static ExtraSpecialItems plugin(){
        return ExtraSpecialItems.getInstance();
    }
}
