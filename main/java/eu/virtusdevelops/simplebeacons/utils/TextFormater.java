package eu.virtusdevelops.simplebeacons.utils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class TextFormater {

    public static String sFormatText(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colorFormatList(List<String> list){
        List<String> formated = new ArrayList<>();
        for(String string : list){
            formated.add(sFormatText(string));
        }
        return formated;
    }

    public static String formatTime(long time) {
        time /= 1000L;
        final int days = (int)(time / 86400L);
        time -= 86400 * days;
        final int hours = (int)(time / 3600L);
        time -= 3600 * hours;
        final int minutes = (int)(time / 60L);
        time -= 60 * minutes;
        final int seconds = (int)time;
        final StringBuilder sb = new StringBuilder();

            if (days != 0) {
                sb.append(days).append("d ");
            }
            if (hours != 0) {
                sb.append(hours).append("h ");
            }
            if (minutes != 0) {
                sb.append(minutes).append("m ");
            }
            sb.append(seconds).append("s ");


        return sb.toString().trim();
    }
}

