package ch.hekates.enderdrop

import ch.hekates.enderdrop.language.LangLoader
import ch.hekates.enderdrop.language.Text
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class Main : JavaPlugin() {
    private var plugin: Main? = null
    public final val PREFIX = "ED"

    override fun onEnable() {
        saveDefaultConfig()
        plugin = this;

        //Language setup
        val languageID = config.getString("language")
        LangLoader.loadLanguage(languageID)
        val language = Text.get("language")
        logger.info(language?.let { Text.get("language.console.loaded").replace("%s", it) })



    }
    fun getPlugin(): Main? {
        return plugin
    }
}