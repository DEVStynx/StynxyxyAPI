package de.stynxyxy.stynxyxyAPI.util;

import de.stynxyxy.stynxyxyAPI.PaperAPI;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class MessageUtil {
    public final Pattern hexPattern = Pattern.compile("#[a-fA-f0-9]{6}");


    /**
     * @param string the old String to format
     * @return the colorized String without any Prefix
     */
    public String formatString(String string) {
        return translateHex(ChatColor.translateAlternateColorCodes('&',string));
    }

    /**
     * @param string the old String to format
     * @return the colorized String
     */
    public String translateHex(String string) {
        Matcher matcher = hexPattern.matcher(string);
        while (matcher.find()) {
            String color = string.substring(matcher.start(),matcher.end());
            string = string.replace(color, ChatColor.of(color)+"");
            matcher = hexPattern.matcher(string);
        }
        return string;
    }

    public String formatStringwPrefix(String string) {
        return formatString(PaperAPI.prefix+string);
    }
}
