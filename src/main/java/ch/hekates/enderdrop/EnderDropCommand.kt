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

    val bold = ChatColor.BOLD
    val reset = ChatColor.RESET
    val red = ChatColor.RED
    val purple = ChatColor.LIGHT_PURPLE
    val gray = ChatColor.GRAY
    val yellow = ChatColor.YELLOW

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(prefix + ChatColor.RED + Text.get("command.error.no_player"))
            return true
        }

        val player = sender
        if (!player.hasPermission("enderdrop.use")) {
            player.sendMessage(
                prefix + ChatColor.RED + Text.get("command.error.no_permission", false)
                    .replace("%permission", "$bold enderdrop.use$reset$red")
            )
            return true
        }
        //"/enderdrop target"
        if (args.isNotEmpty()) {    //if the player targets another player
            if (Bukkit.getPlayer(args[0]) != null) {    //if the player exists
                val target = Bukkit.getPlayer(args[0])
                val transferAmount = player.inventory.itemInMainHand.amount
                val transferItem = player.inventory.itemInMainHand  // -> ItemStack
                val name = transferItem.type.name.replace("_", " ").uppercase()
                val slot = player.inventory.heldItemSlot

                val enderDropText = "$reset$purple$bold Ender$bold Drop$reset$gray"
                val variableFormat = "" // ?

                if (target == player) {
                    player.sendMessage(prefix + red + Text.get("command.error.self_delivery", false))
                    return true
                }

                if (transferItem.type == Material.AIR) {    //if the player isn't holding anything
                    player.sendMessage(prefix + red + Text.get("command.error.no_item_in_hand", false))
                    return true
                }

                if (target!!.inventory.firstEmpty() != -1) {    //if the target has a free slot

                    val deliveredMap = mapOf<String, String>(
                        "%player" to "yellow${target.name}$reset$gray",
                        "%amount" to "$yellow$transferAmount$reset$gray",
                        "%item" to "$yellow$name$reset$gray",
                        "%enderdrop" to enderDropText
                    ) as HashMap<String, String>

                    val recievedMap = mapOf<String, String>(
                        "%amount" to "$yellow$transferAmount$reset$gray",
                        "%item" to "$yellow$name$reset$gray",
                        "%enderdrop" to enderDropText,
                        "%player" to "$yellow${player.name}$reset$gray"
                    ) as HashMap<String, String>


                    player.inventory.clear(slot)
                    target.inventory.addItem(transferItem)

                    player.sendMessage( Text.get("command.delivered", deliveredMap))
                    target.sendMessage(Text.get("command.recieved", recievedMap))
                } else { // target has no free slot
                    player.sendMessage(prefix + red +
                            Text.get("command.error.other.no_slot", false)
                                .replace("%player", "$yellow${args[0]}$red"))
                }
            } else { // can't reach target
                player.sendMessage(prefix + red +
                        Text.get("command.error.other.offline", false)
                            .replace("%player", "$yellow${args[0]}$red"))
            }
        } else { // no target specified
            player.sendMessage(Text.get("command.error.other.no"))
            return true
        }
        return true
    }
}