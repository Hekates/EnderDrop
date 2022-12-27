package ch.hekates.enderdrop

import ch.hekates.languify.Languify
import ch.hekates.languify.language.LangLoader
import ch.hekates.languify.language.Text
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    val prefix = "${ChatColor.BOLD}${ChatColor.LIGHT_PURPLE}E${ChatColor.BLUE}D${ChatColor.DARK_GRAY} >> ${ChatColor.GRAY}"

    override fun onEnable() {
        saveDefaultConfig()
        plugin = this;

        //Language setup
        Languify.setup(plugin, plugin.dataFolder.toString(), prefix)
        val languageID = config.getString("language")
        LangLoader.loadLanguage(languageID)
        val language = Text.get("language")
        logger.info(Text.get("language.console.loaded").replace("%s", language))

        //Register commands
        getCommand("enderdrop")?.setExecutor(EnderDropCommand())
    }
        companion object{
        lateinit var plugin: Main
        private set
    }
}