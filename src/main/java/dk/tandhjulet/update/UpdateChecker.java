package dk.tandhjulet.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.bukkit.Bukkit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import com.vdurmont.semver4j.Semver;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.utils.Logger;

public class UpdateChecker {
    private static final String REPO = "Tandhjulet/BandePlugin";
    private static final String LATEST_RELEASE_URL = "https://api.github.com/repos/" + REPO + "/releases";

    private UpdateChecker() {
    }

    static Boolean error = false;
    static String newestVersion;
    static Boolean isUpdateAvailable;

    public static void fetchLatestRelease() {
        Bukkit.getScheduler().runTaskAsynchronously(BandePlugin.getPlugin(), (Runnable) () -> {
            catchBlock: try {
                final HttpURLConnection connection = tryRequest(LATEST_RELEASE_URL);

                if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                    break catchBlock;
                } else if (connection.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR
                        || connection.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN) {
                    break catchBlock;
                }

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    newestVersion = new Gson().fromJson(reader, JsonArray.class).get(0).getAsJsonObject()
                            .get("tag_name").getAsString();

                    checkUpdate(); // print to stdout
                    return; // the request was successful

                } catch (JsonSyntaxException | NumberFormatException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        error = true;
    }

    private static HttpURLConnection tryRequest(String githubUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(githubUrl).openConnection();
        connection.connect();
        return connection;
    }

    public static boolean checkUpdate() {
        if (newestVersion == null) {
            return false;
        }

        Semver sem = new Semver(newestVersion);
        if (sem.isGreaterThan(getPluginVersion())) {
            isUpdateAvailable = true;

            Logger.info("There's an update available for the Bande Plugin!");

            // TODO: Remove this when out of pre-release stage
            Logger.info("Please note that you're currently using a prerelease of the plugin.");
            Logger.info(
                    "Therefore this update COULD (but most-likely wont) break or ruin your current files if not wiped beforehand.");

            Logger.info("Current version: " + UpdateChecker.getPluginVersion());
            Logger.info("Newest version: " + UpdateChecker.getNewestVersion());

            return true;
        } else {
            Logger.info("Running latest version of bande! (" + getPluginVersion() + ")");
        }
        isUpdateAvailable = false;
        return false;
    }

    public static Boolean errored() {
        return error;
    }

    public static String getPluginVersion() {
        return BandePlugin.getPlugin().getDescription().getVersion();
    }

    public static boolean isUpdateAvailable() {
        return isUpdateAvailable;
    }

    public static String getNewestVersion() {
        return newestVersion;
    }
}
