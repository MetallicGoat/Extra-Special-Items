package me.metallicgoat.specialItems.items.silverfish;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
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

    public static void throwSilverfish(PlayerUseSpecialItemEvent event, SpecialItemUseSession session) {
        final Player player = event.getPlayer();

        event.setTakingItem(true);
        session.takeItem();

        final Snowball snowball = player.launchProjectile(Snowball.class);
        snowballs.add(snowball);

        final BukkitScheduler scheduler = plugin().getServer().getScheduler();
        scheduler.runTaskLater(plugin(), () -> snowballs.remove(snowball), 150L);

        session.stop();
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event){
        if(event.getEntity().getType() == EntityType.SNOWBALL && snowballs.contains((Snowball) event.getEntity())){
            final Player player = (Player) event.getEntity().getShooter();
            final Snowball snowball = (Snowball) event.getEntity();
            final Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);

            if(arena != null && snowballs.contains(snowball)){
                final Team team = arena.getPlayerTeam(player);
                final Location loc = event.getEntity().getLocation();
                final Silverfish silverfish = (Silverfish) loc.getWorld().spawnEntity(loc, EntityType.SILVERFISH);

                assert team != null;
                new UpdateDisplayName().setDisplayName(team, silverfish);

                silverfishTeamHashMap.put(silverfish, team);
                arenaSilverfishHashMap.put(arena, silverfish);
            }
        }
    }


    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event){
        if(event.getEntity() instanceof Silverfish && event.getTarget() instanceof Player){
            final Player player = (Player) event.getTarget();
            final Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);
            final Silverfish silverfish = (Silverfish) event.getEntity();
            if(arena != null && silverfishTeamHashMap.containsKey(silverfish)){
                final Team team = arena.getPlayerTeam(player);

                if(team != null && team.equals(silverfishTeamHashMap.get(silverfish))){
                    event.setCancelled(true);
                }
            }
        }
    }

    // Possible puff of smoke
    @EventHandler
    public void onSilverfishDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Silverfish){
            final Silverfish silverfish = (Silverfish) event.getEntity();
            silverfishTeamHashMap.remove(silverfish);
        }
    }

    @EventHandler
    public void onSilverfishBurrow(EntityChangeBlockEvent event){
        if (event.getEntity() instanceof Silverfish
                && silverfishTeamHashMap.containsKey((Silverfish) event.getEntity())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Silverfish){
            final Silverfish silverfish = (Silverfish) event.getDamager();
            if(silverfishTeamHashMap.containsKey(silverfish)){
                event.setDamage(1.5);
            }
        }else if(event.getDamager() instanceof Player && event.getEntity() instanceof Silverfish){
            final Player player = (Player) event.getDamager();
            final Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);
            if(arena != null){
                final Team playerTeam = arena.getPlayerTeam(player);
                final Team silverfishTeam = silverfishTeamHashMap.get((Silverfish) event.getEntity());
                if(playerTeam == silverfishTeam){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onRoundEnd(RoundEndEvent event){
        final Arena arena = event.getArena();
        arenaSilverfishHashMap.forEach((arena1, silverfish) -> {
            if(arena == arena1){
                silverfishTeamHashMap.remove(silverfish);
            }
        });
        arenaSilverfishHashMap.remove(arena);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event){
        final Collection<Arena> arenas = BedwarsAPI.getGameAPI().getArenaByLocation(event.getLocation());
        if(arenas != null && event.getEntity() instanceof Silverfish){
            event.setCancelled(false);
            final Silverfish silverfish = (Silverfish) event.getEntity();
            final BukkitScheduler scheduler = plugin().getServer().getScheduler();

            scheduler.runTaskLater(plugin(), () -> {
                silverfish.remove();
                silverfishTeamHashMap.remove(silverfish);
                arenas.forEach(arena -> arenaSilverfishHashMap.remove(arena, silverfish));
            }, ConfigValue.silverfish_life_duration);
        }
    }

    private static ExtraSpecialItemsPlugin plugin(){
        return ExtraSpecialItemsPlugin.getInstance();
    }
}
