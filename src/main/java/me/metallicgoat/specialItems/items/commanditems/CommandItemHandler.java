package me.metallicgoat.specialItems.items.commanditems;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.tools.Helper;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandItemHandler {

    public static SpecialItemUseHandler getCustomItemHandler(String command, boolean console){
        return new SpecialItemUseHandler() {
            @Override
            public Plugin getPlugin() {
                return ExtraSpecialItemsPlugin.getInstance();
            }

            @Override
            public SpecialItemUseSession openSession(PlayerUseSpecialItemEvent event) {
                final SpecialItemUseSession session = new SpecialItemUseSession(event) {
                    @Override
                    protected void handleStop() {}
                };

                event.setTakingItem(true);
                session.takeItem();

                final Player player = event.getPlayer();
                final Location loc = player.getLocation();
                final String commandFormatted = Message.build(command)
                        .placeholder("player", player.getName())
                        .placeholder("player-display-name", Helper.get().getPlayerDisplayName(player))
                        .placeholder("x", (int) loc.getX())
                        .placeholder("y", (int) loc.getY())
                        .placeholder("z", (int) loc.getZ())
                        .placeholder("yaw", (int) loc.getYaw())
                        .placeholder("pitch", (int) loc.getPitch())
                        .done();

                // run command
                Bukkit.getServer().dispatchCommand(console ? Bukkit.getConsoleSender() : player, commandFormatted);

                session.stop();
                return session;
            }
        };
    }
}