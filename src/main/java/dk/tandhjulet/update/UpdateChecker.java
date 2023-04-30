package dk.tandhjulet.update;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import com.vdurmont.semver4j.Semver;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.utils.Logger;

public class UpdateChecker {
    private UpdateChecker() {
    }

    static Boolean error = false;
    static String newestVersion;
    static Boolean isUpdateAvailable;
    static Boolean isNewestVersionPrerelease;
    static Boolean isCurrentVersionPrerelease;

    public static boolean checkUpdate() {
        try {
            GitHub gh = GitHub.connectAnonymously();
            GHRepository bandePlugin = gh.getRepository("Tandhjulet/BandePlugin");

            newestVersion = bandePlugin.listTags().toArray()[0].getName();
            isNewestVersionPrerelease = bandePlugin.getReleaseByTagName(newestVersion).isPrerelease();

            try {
                isCurrentVersionPrerelease = bandePlugin.getReleaseByTagName(getPluginVersion()).isPrerelease();
            } catch (Exception e) {
                isCurrentVersionPrerelease = false;
            }

            Semver sem = new Semver(newestVersion);
            if (sem.isGreaterThan(getPluginVersion())) {
                isUpdateAvailable = true;
                return true;
            } else {
                Logger.info("Running latest version of bande! (" + getPluginVersion() + ")");
            }

        } catch (Exception e) {
            Logger.severe("Unable to connect to GitHub to check for any updates...");
            e.printStackTrace();
            error = true;
        }
        isUpdateAvailable = false;
        return false;
    }

    public static Boolean isCurrentVersionPrerelease() {
        return isCurrentVersionPrerelease;
    }

    public static Boolean isNewestVersionPrerelease() {
        return isNewestVersionPrerelease;
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
