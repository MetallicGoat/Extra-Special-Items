package me.metallicgoat.specialItems.items.popuptower;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TowerPlace{
    public void buildTower(PlayerUseSpecialItemEvent e, SpecialItemUseSession session){
        final Player player = e.getPlayer();
        final Arena arena = e.getArena();
        final Block clicked = e.getClickedBlock();
        final BlockFace blockFace = e.getClickedBlockFace();

        //Check if placeable
        if(clicked == null || blockFace == null || blockFace == BlockFace.DOWN || !arena.canPlaceBlockAt(clicked.getRelative(blockFace))){
            e.setTakingItem(false);
            session.stop();
            return;
        }

        //Take item
        e.setTakingItem(true);
        session.takeItem();

        //Choose the correct tower to build, then build it
        ChooseTower(player, clicked.getRelative(blockFace), session);
    }

    private void ChooseTower(Player player, Block clicked, SpecialItemUseSession session){
        double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if (45.0D <= rotation && rotation < 135.0D) {
            new BuildTower(clicked, session, BlockFace.SOUTH);
        } else if (225.0D <= rotation && rotation < 315.0D) {
            new BuildTower(clicked, session, BlockFace.NORTH);
        } else if (135.0D <= rotation && rotation < 225.0D) {
            new BuildTower(clicked, session, BlockFace.WEST);
        } else if (0.0D <= rotation && rotation < 45.0D || 315.0D <= rotation && rotation < 360.0D) {
            new BuildTower(clicked, session, BlockFace.EAST);
        }
    }
}
