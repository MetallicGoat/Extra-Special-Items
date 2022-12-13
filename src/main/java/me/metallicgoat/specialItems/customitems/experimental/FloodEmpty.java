package me.metallicgoat.specialItems.customitems.experimental;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import org.bukkit.plugin.Plugin;

public class FloodEmpty {

    public static SpecialItemUseHandler getFloodEmptyHandler(){
        System.out.println("Get Empty Handler");

        return new SpecialItemUseHandler() {
            @Override
            public Plugin getPlugin() {
                return ExtraSpecialItemsPlugin.getInstance();
            }

            @Override
            public SpecialItemUseSession openSession(PlayerUseSpecialItemEvent e) {
                final SpecialItemUseSession session = new SpecialItemUseSession(e) {
                    @Override
                    protected void handleStop() {}
                };

                session.takeItem();
                System.out.println("Test");

                FloodEmptyTask.flood(e, session, e.getClickedBlock());

                return session;
            }
        };
    }
}
