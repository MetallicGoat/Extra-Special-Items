package me.metallicgoat.specialItems.items.silverfish;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import org.bukkit.plugin.Plugin;

public class SilverfishHandler {

    public static SpecialItemUseHandler getSilverfishHandler(){
        return new SpecialItemUseHandler() {
            @Override
            public Plugin getPlugin() {
                return ExtraSpecialItems.getInstance();
            }

            @Override
            public SpecialItemUseSession openSession(PlayerUseSpecialItemEvent e) {
                SpecialItemUseSession session = new SpecialItemUseSession(e) {
                    @Override
                    protected void handleStop() {
                    }
                };

                SilverfishThrow.throwSilverfish(e, session);

                return session;
            }
        };
    }
}