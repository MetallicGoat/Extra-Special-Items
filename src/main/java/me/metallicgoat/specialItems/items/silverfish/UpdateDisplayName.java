package me.metallicgoat.specialItems.items.silverfish;

import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.message.Message;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.config.ConfigValue;
import org.bukkit.entity.Silverfish;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class UpdateDisplayName {

    public void setDisplayName(Team team, Silverfish silverfish){

        if(ConfigValue.silverfish_life_display_name == null)
            return;

        final String teamName = team.getDisplayName();
        final String color = team.getChatColor().toString();
        final int amountOfTags = ConfigValue.silverfish_life_display_name.getKeys(false).size();
        final String[] displayNames = ConfigValue.silverfish_life_display_name.getKeys(false).toArray(new String[0]);
        final long time = ConfigValue.silverfish_life_duration / amountOfTags;
        final AtomicInteger i = new AtomicInteger(0);

        silverfish.setCustomNameVisible(true);

        new BukkitRunnable() {
            @Override
            public void run(){
                if(SilverfishThrow.silverfishTeamHashMap.containsKey(silverfish) && !silverfish.isDead()){
                    if (i.get() < amountOfTags) {
                        final String unformattedDisplayName = displayNames[i.get()] != null ? ConfigValue.silverfish_life_display_name.getString(displayNames[i.get()]) : "";
                        final String displayName = Message.build(unformattedDisplayName)
                                .placeholder("team-color", color)
                                .placeholder("team-name", teamName)
                                .placeholder("sqr", "■").done();

                        silverfish.setCustomName(displayName);
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

    private static ExtraSpecialItemsPlugin plugin(){
        return ExtraSpecialItemsPlugin.getInstance();
    }
}
