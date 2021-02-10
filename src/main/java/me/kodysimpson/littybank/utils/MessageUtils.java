package me.kodysimpson.littybank.utils;

import me.kodysimpson.simpapi.colors.ColorTranslator;

public class MessageUtils {

    private static String prefix = "&#33b1bd&l[&#33bdb4&lLittyBanks&#33b1bd&l]";
    private static String messagecolor = "&#1de02a";

    public static String message(String message) {
        return ColorTranslator.translateColorCodes(prefix + " " + messagecolor + message);
    }

}
