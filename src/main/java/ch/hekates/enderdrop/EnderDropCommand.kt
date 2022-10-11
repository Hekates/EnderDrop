package ch.hekates.enderdrop

import ch.hekates.languify.language.Text
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*


class EnderDropCommand : CommandExecutor {
    val config = Main.plugin.config
    val prefix = Main.plugin.prefix

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(prefix + ChatColor.RED + Text.get("command.error.no_player"))
            return true
        }
        val player = sender
        if (!player.hasPermission("hup.enderdrop")) {
            player.sendMessage(
                prefix + ChatColor.RED + Text.get("command.error.no_permission", false)
                    .replace("%permission", "${ChatColor.BOLD}hup.enderdrop${ChatColor.RESET}${ChatColor.RED}")
            )
            return true
        }
        if (args.size > 0) {    //if the player targets another player
            if (Bukkit.getPlayer(args[0]) != null) {    //if the player exists
                val target = Bukkit.getPlayer(args[0])
                val transferAmount = player.inventory.itemInMainHand.amount
                val transferItem = ItemStack(player.inventory.itemInMainHand.type, transferAmount)
                val name = transferItem.type.name.replace("_", " ").uppercase()

                val enderDropText = "${ChatColor.RESET}${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}Ender${ChatColor.BLUE}Drop${ChatColor.RESET}${ChatColor.GRAY}"
                val variableFormat = ""

                if (target == player) {
                    player.sendMessage(Text.get("command.error.self_delivery"))
                    return true
                }
                if (transferItem.type == Material.AIR) {    //if the player isn't holding anything
                    player.sendMessage(prefix + ChatColor.RED + Text.get("command.error.no_item_in_hand", false))
                    return true
                }
                if (target!!.inventory.firstEmpty() != -1) {    //if the target has a free slot
                    target.inventory.addItem(transferItem)
                    player.inventory.remove(transferItem)
                    player.sendMessage(
                        Text.get("command.delivered")
                            .replace("%player", "${ChatColor.YELLOW}${target.name}${ChatColor.RESET} ${ChatColor.GRAY}")
                            .replace("%amount", "${ChatColor.YELLOW}${transferAmount}${ChatColor.RESET} ${ChatColor.GRAY}")
                            .replace("%item", "${ChatColor.YELLOW}${name}${ChatColor.RESET} ${ChatColor.GRAY}")
                            .replace("%enderdrop", enderDropText)
                    )
                    target.sendMessage(
                        Text.get("command.recieved")
                            .replace("%transferamount".toRegex(), "§e§l$transferAmount")
                            .replace("%itemname".toRegex(), "$name§r§7")
                            .replace("%enderdrop".toRegex(), enderDropText)
                            .replace("%sender".toRegex(), "§e" + player.name + "§7")
                    )
                } else {
                    player.sendMessage(
                        prefix + "§cDu kannst dem Spieler: §e§l"
                                + args[0] + " §ckein Item übergeben, da dieser keinen freien Slot im Inventar hat!"
                    )
                }
            } else {
                player.sendMessage(prefix + "§cDer Spieler: §e§l" + args[0] + " §cist nicht online oder existiert nicht!")
            }
        } else {
            player.sendMessage("$prefix§cDu musst eine Person angeben, der du das Item überreichen willst!")
            return true
        }
        return false
    }
}