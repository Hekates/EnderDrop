package ch.hekates.enderdrop.language;

import ch.hekates.enderdrop.Languify;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LangLoader {
    public static void loadLanguage(String language) {

        Languify.setLanguage(language);

        Plugin plugin = Languify.getPlugin();

        String path = Languify.getFileDirectory();

        Logger log = plugin.getLogger();

        List<File> langFiles = new ArrayList<File>();
        File file = new File(path + "/lang/" + language + ".json");

        if (file.exists()) {
            log.info("The specified language file is located in the right directory... proceeding.");
        } else {
            log.warning("The specified language file is not located in the right directory... generating a new one.");
            plugin.saveResource("lang\\" + language + ".json", false);
        }
    }
}
