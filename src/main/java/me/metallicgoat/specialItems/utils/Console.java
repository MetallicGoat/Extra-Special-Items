package me.metallicgoat.specialItems.utils;

import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;

public class Console {

    public static void printConfigWarning(String configName, String warning){
        printSpecializedWarning("Config", "(" + configName + ") " + warning);
    }

    public static void printSpecializedWarning(String type, String warning){
        printWarning("[" + type + "] " + warning);
    }

    public static void printSpecializedInfo(String type, String info){
        printInfo("[" + type + "] " + info);
    }

    public static void printWarning(String warning){
        ExtraSpecialItemsPlugin.getInstance().getLogger().warning(warning);
    }

    public static void printInfo(String info){
        ExtraSpecialItemsPlugin.getInstance().getLogger().info(info);
    }
}
