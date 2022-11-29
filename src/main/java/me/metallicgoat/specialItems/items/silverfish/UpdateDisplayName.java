package me.metallicgoat.specialItems.items.silverfish;

import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.message.Message;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.config.ConfigValue;
import org.bukkit.entity.Silverfish;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateDisplayName {

    public void setDisplayName(Team team, Silverfish silverfish){

        if(ConfigValue.silverfish_name_tag == null)
            return;

        final String teamName = team.getDisplayName();
        final String color = team.getChatColor().toString();
        final int amountOfTags = ConfigValue.silverfish_name_tag.size();
        final long time = ConfigValue.silverfish_life_duration / amountOfTags;

        silverfish.setCustomNameVisible(true);

        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run(){
                if(SilverfishThrow.silverfishTeamHashMap.containsKey(silverfish) && !silverfish.isDead()){
                    if (i < amountOfTags) {
                        final String unformattedDisplayName = ConfigValue.silverfish_name_tag.get(i);
                        final String displayName = Message.build(unformattedDisplayName != null ? unformattedDisplayName : "")
                                .placeholder("team-color", color)
                                .placeholder("team-name", teamName)
                                .done();

                        silverfish.setCustomName(displayName);
                    }else{
                        cancel();
                        return;
                    }
                    i++;
                }else{
                    cancel();
                }
            }
        }.runTaskTimer(plugin(), 0L, time);
    }

    private static ExtraSpecialItemsPlugin plugin(){
        return ExtraSpecialItemsPlugin.getInstance();
    }
}
