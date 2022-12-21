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
import kotlin.collections.HashMap


class EnderDropCommand : CommandExecutor {
    val config = Main.plugin.config
    val prefix = Main.plugin.prefix

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(prefix + ChatColor.RED + Text.get("command.error.no_player"))
            return true
        }
        val player = sender
        if (!player.hasPermission("enderdrop")) {
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

                    val deliveredMap =  mapOf<String, String>(
                        "%player" to "${ChatColor.YELLOW}${target.name}${ChatColor.RESET} ${ChatColor.GRAY}",
                        "%amount" to "${ChatColor.YELLOW}${transferAmount}${ChatColor.RESET} ${ChatColor.GRAY}",
                        "%item" to "${ChatColor.YELLOW}${name}${ChatColor.RESET} ${ChatColor.GRAY}",
                        "%enderdrop" to enderDropText
                    ) as HashMap<String, String>

                    val recievedMap = mapOf<String, String>(
                        "%transferamount" to "§e§l$transferAmount",
                        "%itemname" to "$name§r§7",
                        "%enderdrop" to enderDropText,
                        "%sender" to "§e${player.name}§7"
                    ) as HashMap<String, String>


                    target.inventory.addItem(transferItem)
                    player.inventory.remove(transferItem)

                    player.sendMessage( Text.get("command.delivered", deliveredMap))
                    target.sendMessage(Text.get("command.recieved", recievedMap))
                } else {
                    player.sendMessage(prefix + ChatColor.RED +
                            Text.get("command.error.other.no_slot", false)
                                .replace("%player", "§e§l${args[0]}§c"))
                }
            } else {
                player.sendMessage(prefix + ChatColor.RED +
                        Text.get("command.error.other.offline", false)
                            .replace("%player", "§e§l${args[0]}§c"))
            }
        } else {
            player.sendMessage(Text.get("command.error.other.no"))
            return true
        }
        return false
    }
}