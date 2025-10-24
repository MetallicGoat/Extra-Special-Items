package me.metallicgoat.specialItems.api;

import de.marcely.bedwars.api.game.specialitem.SpecialItem;
import de.marcely.bedwars.tools.Validate;
import me.metallicgoat.specialItems.customitems.CustomSpecialItem;
import org.jetbrains.annotations.Nullable;

/**
 * Enum representing different types of extra special items.
 */
public enum ExtraSpecialItemType {

  /**
   * Runs a custom command on use.
   * <p>
   *   There may be multiple items of this type.
   * </p>
   */
  COMMAND,

  /**
   * Launches an egg that places blocks on its way.
   */
  EGG_BRIDGER,

  /**
   * Places ice below the player.
   */
  ICE_BRIDGER,

  /**
   * Launches a snowball that spawns a silverfish wherever it lands.
   */
  SILVERFISH,

  /**
   * Launches the player in the direction they are looking.
   */
  SLINGSHOT,

  /**
   * Builds a tower around the player.
   */
  TOWER;

  private SpecialItem specialItem;

  /**
   * Get the SpecialItem associated with this type.
   *
   * @return the SpecialItem. Is <code>null</code> for {@link #hasMultipleItems()}
   */
  @Nullable
  public SpecialItem getItem() {
    return this.specialItem;
  }

  /**
   * Get whether there may be multiple items of this type.
   * <p>
   *   Only {@link #COMMAND} may have multiple items.
   * </p>
   *
   * @return true if there may be multiple items of this type, false otherwise
   */
  public boolean hasMultipleItems() {
    return this == COMMAND;
  }

  /**
   * Get the ExtraSpecialItemType from a SpecialItem.
   *
   * @param item the SpecialItem to get the type from
   * @return the ExtraSpecialItemType, or <code>null</code> if the item is not from this addon
   */
  @Nullable
  public static ExtraSpecialItemType from(SpecialItem item) {
    Validate.notNull(item, "item");

    if (!(item instanceof CustomSpecialItem))
      return null;

    return ((CustomSpecialItem) item).getType();
  }
}
