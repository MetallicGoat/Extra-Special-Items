package me.metallicgoat.specialItems.items.silverfish;

import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.message.Message;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import org.bukkit.entity.Silverfish;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class UpdateDisplayName {

    public void setDisplayName(Team team, Silverfish silverfish){
        silverfish.setCustomNameVisible(true);

        final String teamName = team.getDisplayName();
        final String c = "&" + team.getChatColor().getChar();
        final long time = plugin().getConfig().getLong("Silverfish.Life-Duration") / 5;
        final AtomicInteger i = new AtomicInteger(2);

        new BukkitRunnable() {
            @Override
            public void run(){
                if(SilverfishThrow.silverfishTeamHashMap.containsKey(silverfish) && !silverfish.isDead()){
                    if (i.get() < 5) {
                        silverfish.setCustomName(displayName(i.get(), teamName, c));
                    }else{
                        cancel();
                        return;
                    }
                    i.getAndIncrement();
                }else{
                    cancel();
                }
            }
        }.runTaskTimer(plugin(), 0L, time);
    }

    private static String displayName(int version, String teamName, String color){
        final String config = "Silverfish.Display-Name." + (version + 1);
        final String unformattedString = plugin().getConfig().getString(config);
        return Message.build(unformattedString).placeholder("team-color", color).placeholder("team-name", teamName).done();
    }


    private static ExtraSpecialItems plugin(){
        return ExtraSpecialItems.getInstance();
    }
}
