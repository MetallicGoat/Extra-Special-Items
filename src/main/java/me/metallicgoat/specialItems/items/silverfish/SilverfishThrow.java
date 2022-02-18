package me.metallicgoat.specialItems.items.silverfish;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import me.metallicgoat.specialItems.config.ConfigValue;
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

        final Snowball snowball = player.launchProjectile(Snowball.class);
        snowballs.add(snowball);

        final BukkitScheduler scheduler = plugin().getServer().getScheduler();
        scheduler.runTaskLater(plugin(), () -> snowballs.remove(snowball), 150L);

        session.stop();
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e){
        if(e.getEntity().getType() == EntityType.SNOWBALL && snowballs.contains((Snowball) e.getEntity())){
            final Player player = (Player) e.getEntity().getShooter();
            final Snowball snowball = (Snowball) e.getEntity();
            final Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);

            if(arena != null && snowballs.contains(snowball)){
                final Team team = arena.getPlayerTeam(player);
                final Location loc = e.getEntity().getLocation();
                final Silverfish silverfish = (Silverfish) loc.getWorld().spawnEntity(loc, EntityType.SILVERFISH);

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
            final Player player = (Player) e.getTarget();
            final Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);
            final Silverfish silverfish = (Silverfish) e.getEntity();
            if(arena != null && silverfishTeamHashMap.containsKey(silverfish)){
                final Team team = arena.getPlayerTeam(player);

                if(team != null && team.equals(silverfishTeamHashMap.get(silverfish))){
                    e.setCancelled(true);
                }
            }
        }
    }

    // Possible puff of smoke
    @EventHandler
    public void onSilverfishDeath(EntityDeathEvent e){
        if(e.getEntity() instanceof Silverfish){
            final Silverfish silverfish = (Silverfish) e.getEntity();
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
            final Silverfish silverfish = (Silverfish) e.getDamager();
            if(silverfishTeamHashMap.containsKey(silverfish)){
                e.setDamage(1.5);
            }
        }else if(e.getDamager() instanceof Player && e.getEntity() instanceof Silverfish){
            final Player player = (Player) e.getDamager();
            final Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);
            if(arena != null){
                final Team playerTeam = arena.getPlayerTeam(player);
                final Team silverfishTeam = silverfishTeamHashMap.get((Silverfish) e.getEntity());
                if(playerTeam == silverfishTeam){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onRoundEnd(RoundEndEvent e){
        final Arena arena = e.getArena();
        arenaSilverfishHashMap.forEach((arena1, silverfish) -> {
            if(arena == arena1){
                silverfishTeamHashMap.remove(silverfish);
            }
        });
        arenaSilverfishHashMap.remove(arena);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent e){
        final Collection<Arena> arenas = BedwarsAPI.getGameAPI().getArenaByLocation(e.getLocation());
        if(arenas != null && e.getEntity() instanceof Silverfish){
            e.setCancelled(false);
            final Silverfish silverfish = (Silverfish) e.getEntity();
            final BukkitScheduler scheduler = plugin().getServer().getScheduler();

            scheduler.runTaskLater(plugin(), () -> {
                silverfish.remove();
                silverfishTeamHashMap.remove(silverfish);
                arenas.forEach(arena -> arenaSilverfishHashMap.remove(arena, silverfish));
            }, ConfigValue.silverfish_life_duration);
        }
    }

    private static ExtraSpecialItems plugin(){
        return ExtraSpecialItems.getInstance();
    }
}
