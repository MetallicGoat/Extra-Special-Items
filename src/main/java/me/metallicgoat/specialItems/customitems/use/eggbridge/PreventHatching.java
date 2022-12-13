package me.metallicgoat.specialItems.customitems.use.eggbridge;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;

public class PreventHatching implements Listener {

    @EventHandler
    public void onCreatureSpawn(PlayerEggThrowEvent e){
        final Arena arena = BedwarsAPI.getGameAPI().getArenaByPlayer(e.getPlayer());

        if(arena != null)
            e.setHatching(false);

    }
}
