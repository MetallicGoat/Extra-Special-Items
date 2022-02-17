package me.metallicgoat.specialItems;

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
        ExtraSpecialItems.getInstance().getLogger().warning(warning);
    }

    public static void printInfo(String info){
        ExtraSpecialItems.getInstance().getLogger().info(info);
    }
}
