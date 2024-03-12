package dev.ua.ikeepcalm.utils;

import dev.ua.ikeepcalm.LordOfTheMinecraft;

import java.util.List;
import java.util.Objects;

public class LocalizationUtil {

    public static String getLocalizedString(String key) {
        return Objects.requireNonNull(LordOfTheMinecraft.instance.getLangConfig().getString(key));
    }

    public static String getLocalizedString(String node, String key) {
        return Objects.requireNonNull(LordOfTheMinecraft.instance.getLangConfig().getConfigurationSection(node)).getString(key);
    }

    public static String getLocalizedString(String node, String subNode, String key) {
        return (Objects.requireNonNull(Objects.requireNonNull(LordOfTheMinecraft.instance.getLangConfig().getConfigurationSection(node)).getConfigurationSection(subNode))).getString(key);
    }

    public static List<String> getLocalizedList(String node,String key) {
        return Objects.requireNonNull(LordOfTheMinecraft.instance.getLangConfig().getConfigurationSection(node)).getStringList(key);
    }

}
