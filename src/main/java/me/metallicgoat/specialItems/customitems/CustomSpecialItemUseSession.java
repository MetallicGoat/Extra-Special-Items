package me.metallicgoat.specialItems.customitems;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;

public abstract class CustomSpecialItemUseSession extends SpecialItemUseSession {

    public CustomSpecialItemUseSession(PlayerUseSpecialItemEvent event){
        super(event);
    }

    public abstract void run(PlayerUseSpecialItemEvent event);
}
