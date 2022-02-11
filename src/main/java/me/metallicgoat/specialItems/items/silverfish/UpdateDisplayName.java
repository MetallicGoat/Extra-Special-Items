package me.metallicgoat.specialItems.items.silverfish;

import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.message.Message;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import org.bukkit.entity.Silverfish;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateDisplayName {

    public void setDisplayName(Team team, Silverfish silverfish){
        final int[] i = {0};
        silverfish.setCustomNameVisible(true);
        String teamName = team.getDisplayName();
        String c = "&" + team.getChatColor().getChar();
        long time = plugin().getConfig().getLong("Silverfish.Life-Duration") / 5;
        new BukkitRunnable() {
            @Override
            public void run(){
                if(SilverfishThrow.silverfishTeamHashMap.containsKey(silverfish) && !silverfish.isDead()){
                    if (i[0] < 5) {
                        silverfish.setCustomName(displayName(i[0], teamName, c));
                    }else{
                        cancel();
                        return;
                    }
                    i[0]++;
                }else{
                    cancel();
                }
            }
        }.runTaskTimer(plugin(), 0L, time);
    }

    private static String displayName(int version, String teamName, String color){
        String config = "Silverfish.Display-Name." + (version + 1);
        String unformattedString = plugin().getConfig().getString(config);
        return Message.build(unformattedString).placeholder("team-color", color).placeholder("team-name", teamName).done();
    }


    private static ExtraSpecialItems plugin(){
        return ExtraSpecialItems.getInstance();
    }
}
