package me.metallicgoat.specialItems.customitems;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.customitems.use.SilverfishHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerEggThrowEvent;

import java.util.Collection;

public class Events  implements Listener {

    // EGG BRIDGER
    @EventHandler
    public void onCreatureSpawn(PlayerEggThrowEvent e){
        final Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(e.getPlayer());

        if(arena != null)
            e.setHatching(false);

    }

    // SILVERFISH
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getType() == EntityType.SNOWBALL && SilverfishHandler.snowballs.contains((Snowball) event.getEntity())) {
            final Player player = (Player) event.getEntity().getShooter();
            final Snowball snowball = (Snowball) event.getEntity();
            final Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);

            if (arena != null && SilverfishHandler.snowballs.contains(snowball)) {
                final Team team = arena.getPlayerTeam(player);
                final Location loc = event.getEntity().getLocation();
                final Silverfish silverfish = (Silverfish) loc.getWorld().spawnEntity(loc, EntityType.SILVERFISH);

                if (team == null)
                    return;

                SilverfishHandler.updateDisplayName(team, silverfish);
                SilverfishHandler.silverfishTeamHashMap.put(silverfish, team);
                SilverfishHandler.arenaSilverfishHashMap.put(arena, silverfish);
            }
        }
    }

    // SILVERFISH
    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (event.getEntity() instanceof Silverfish && event.getTarget() instanceof Player) {
            final Player player = (Player) event.getTarget();
            final Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);
            final Silverfish silverfish = (Silverfish) event.getEntity();
            if (arena != null && SilverfishHandler.silverfishTeamHashMap.containsKey(silverfish)) {
                final Team team = arena.getPlayerTeam(player);

                if (team != null && team.equals(SilverfishHandler.silverfishTeamHashMap.get(silverfish))) {
                    event.setCancelled(true);
                }
            }
        }
    }

    // SILVERFISH Possible puff of smoke
    @EventHandler
    public void onSilverfishDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Silverfish) {
            final Silverfish silverfish = (Silverfish) event.getEntity();
            SilverfishHandler.silverfishTeamHashMap.remove(silverfish);
        }
    }

    // SILVERFISH
    @EventHandler
    public void onSilverfishBurrow(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof Silverfish
                && SilverfishHandler.silverfishTeamHashMap.containsKey((Silverfish) event.getEntity())) {
            event.setCancelled(true);
        }
    }

    // SILVERFISH
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Silverfish) {
            final Silverfish silverfish = (Silverfish) event.getDamager();
            if (SilverfishHandler.silverfishTeamHashMap.containsKey(silverfish)) {
                event.setDamage(1.5);
            }
        } else if (event.getDamager() instanceof Player && event.getEntity() instanceof Silverfish) {
            final Player player = (Player) event.getDamager();
            final Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);
            if (arena != null) {
                final Team playerTeam = arena.getPlayerTeam(player);
                final Team silverfishTeam = SilverfishHandler.silverfishTeamHashMap.get((Silverfish) event.getEntity());
                if (playerTeam == silverfishTeam) {
                    event.setCancelled(true);
                }
            }
        }
    }

    // SILVERFISH
    @EventHandler
    public void onRoundEnd(RoundEndEvent event) {
        final Arena arena = event.getArena();
        SilverfishHandler.arenaSilverfishHashMap.forEach((arena1, silverfish) -> {
            if (arena == arena1) {
                SilverfishHandler.silverfishTeamHashMap.remove(silverfish);
            }
        });
        SilverfishHandler.arenaSilverfishHashMap.remove(arena);
    }

    // SILVERFISH
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        final Collection<Arena> arenas = BedwarsAPI.getGameAPI().getArenaByLocation(event.getLocation());
        if (arenas != null && event.getEntity() instanceof Silverfish) {
            event.setCancelled(false);
            final Silverfish silverfish = (Silverfish) event.getEntity();

            Bukkit.getScheduler().runTaskLater(ExtraSpecialItemsPlugin.getInstance(), () -> {
                silverfish.remove();
                SilverfishHandler.silverfishTeamHashMap.remove(silverfish);
                arenas.forEach(arena -> SilverfishHandler.arenaSilverfishHashMap.remove(arena, silverfish));
            }, ConfigValue.silverfish_life_duration);
        }
    }
}
