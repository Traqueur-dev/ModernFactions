package fr.traqueur.modernfactions.api.updater;

import fr.traqueur.modernfactions.api.FactionsLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

/**
 * This class is used to check if the plugin is up to date
 */
public class Updater {

    /**
     * Check if the plugin is up to date and log a warning if it's not
     */
    public static void checkUpdates(JavaPlugin plugin, String repository) {
        if(!Updater.isUpToDate(plugin, repository)) {
            FactionsLogger.warning("ModernFactions is not up to date, the latest version is " + Updater.fetchLatestVersion(repository));
        }
    }

    /**
     * Get the version of the plugin
     * @return The version of the plugin
     */
    public static String getVersion(JavaPlugin plugin) {
        return plugin.getDescription().getVersion();
    }

    /**
     * Check if the plugin is up to date
     * @return True if the plugin is up to date, false otherwise
     */
    public static boolean isUpToDate(JavaPlugin plugin, String repository) {
        try {
            String latestVersion = fetchLatestVersion(repository);
            return getVersion(plugin).equals(latestVersion);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the latest version of the plugin
     * @return The latest version of the plugin
     */
    public static String fetchLatestVersion(String repo) {
        try {
            URL url = URI.create("https://api.github.com/repos/Traqueur-dev/"+ repo +"/releases/latest").toURL();
            String responseString = getString(url);
            int tagNameIndex = responseString.indexOf("\"tag_name\"");
            int start = responseString.indexOf('\"', tagNameIndex + 10) + 1;
            int end = responseString.indexOf('\"', start);
            return responseString.substring(start, end);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the latest version of the plugin
     * @return The latest version of the plugin
     */
    private static String getString(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
        } finally {
            connection.disconnect();
        }

        return response.toString();
    }
}