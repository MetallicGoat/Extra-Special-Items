package me.metallicgoat.specialItems.items.popuptower;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.spawner.Spawner;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.items.popuptower.towers.East;
import me.metallicgoat.specialItems.items.popuptower.towers.North;
import me.metallicgoat.specialItems.items.popuptower.towers.South;
import me.metallicgoat.specialItems.items.popuptower.towers.West;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TowerPlace{

    public void buildTower(PlayerUseSpecialItemEvent e, SpecialItemUseSession session){
        final Player player = e.getPlayer();
        Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(player);

        if(e.getClickedBlock() == null){
            e.setTakingItem(false);
            session.stop();
            return;
        }else if(e.getClickedBlockFace() == BlockFace.DOWN){
            e.setTakingItem(false);
            session.stop();
            return;
        }else {
            assert arena != null;
            if(!CheckArea(arena, e.getClickedBlock().getRelative(0, 1, 0))){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessage()));
                e.setTakingItem(false);
                session.stop();
                return;
            }
        }

        e.setTakingItem(true);
        session.takeItem();

        final Block clicked = e.getClickedBlock();
        final DyeColor col = BedwarsAPI.getGameAPI().getArenaByPlayer(player).getPlayerTeam(player).getDyeColor();

        ChooseTower(player, clicked, col, session);
    }

    private void ChooseTower(Player player, Block clicked, DyeColor col, SpecialItemUseSession session){
        double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if (45.0D <= rotation && rotation < 135.0D) {
            new South(clicked, col, player, session);
        } else if (225.0D <= rotation && rotation < 315.0D) {
            new North(clicked, col, player, session);
        } else if (135.0D <= rotation && rotation < 225.0D) {
            new West(clicked, col, player,  session);
        } else if (0.0D <= rotation && rotation < 45.0D || 315.0D <= rotation && rotation < 360.0D) {
            new East(clicked, col, player, session);
        }
    }

    //Checks if the popuptower is placed inside/near team spawn area, and item spawners
    private boolean CheckArea(Arena arena, Block b){

        final int sRadius = getSRadius();
        final int tRadius = getTRadius();

        for(Spawner s : arena.getSpawners()){
            Location sLoc = s.getLocation().toLocation(arena.getGameWorld());
            Location bLoc = b.getLocation();
            if ((sLoc.getX() <= bLoc.getX() + sRadius && sLoc.getY() <= bLoc.getY() + sRadius && sLoc.getZ() <= bLoc.getZ() + sRadius)
                    && (sLoc.getX() >= bLoc.getX() - sRadius && sLoc.getY() >= bLoc.getY() - sRadius && sLoc.getZ() >= bLoc.getZ() - sRadius)) {
                return false;
            }
        }
        for(Team team : arena.getEnabledTeams()){
            Location tLoc = arena.getTeamSpawn(team).toLocation(arena.getGameWorld());
            Location bLoc = b.getLocation();
            if ((tLoc.getX() <= bLoc.getX() + tRadius && tLoc.getY() <= bLoc.getY() + tRadius && tLoc.getZ() <= bLoc.getZ() + tRadius)
                    && (tLoc.getX() >= bLoc.getX() - tRadius && tLoc.getY() >= bLoc.getY() - tRadius && tLoc.getZ() >= bLoc.getZ() - tRadius)) {
                return false;
            }
        }
        return true;
    }
    private int getSRadius(){
        return 3;
                //Main.getConfigManager().getItemSpawnerRadius();
    }
    private int getTRadius(){
        return 6;
                //Main.getConfigManager().getTeamSpawnRadius();
    }
    private String getMessage(){
        return "&cYou can not use this item here!";
                //Main.getConfigManager().getAntiPlaceMessage();
    }
}
