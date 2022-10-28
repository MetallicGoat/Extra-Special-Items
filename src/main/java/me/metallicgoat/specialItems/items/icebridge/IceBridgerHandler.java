package me.metallicgoat.specialItems.items.icebridge;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import org.bukkit.plugin.Plugin;

public class IceBridgerHandler {

    public static SpecialItemUseHandler getIceBridgeHandler(){
        return new SpecialItemUseHandler() {
            @Override
            public Plugin getPlugin() {
                return ExtraSpecialItemsPlugin.getInstance();
            }

            @Override
            public SpecialItemUseSession openSession(PlayerUseSpecialItemEvent e) {
                final SpecialItemUseSession session = new SpecialItemUseSession(e) {
                    @Override
                    protected void handleStop() {
                    }
                };

                IceBridgeUse iceBridge = new IceBridgeUse();
                iceBridge.createIceBridge(e, session);

                return session;
            }
        };
    }
}
