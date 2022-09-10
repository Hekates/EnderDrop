package ch.hekates.enderdrop

import ch.hekates.enderdrop.language.Text
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
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (label.equals("enderdrop", ignoreCase = true) || label.equals("ed", ignoreCase = true)) {
            if (sender !is Player) {
                sender.sendMessage(ChatColor.RED.toString() + Text.get("command.error.no_player"))
                return true
            }
            val player = sender
            if (!player.hasPermission("enderdrop.use")) return true
            if (args.size > 0) {
                if (Bukkit.getPlayer(args[0]) != null) {
                    val target = Bukkit.getPlayer(args[0])
                    val transferAmount = player.inventory.itemInMainHand.amount
                    val transferItem = ItemStack(player.inventory.itemInMainHand.type, transferAmount)
                    val name = transferItem.type.name.replace("_".toRegex(), " ").uppercase(Locale.getDefault())
                    if (target === player) {
                        player.sendMessage("§8>> §7Du kannst dir selbst kein Item überreichen!")
                        return true
                    }
                    if (transferItem.type == Material.AIR) {
                        player.sendMessage("§8>> §cDu hast kein Item in der Hand!")
                        return true
                    }
                    if (target!!.inventory.firstEmpty() != -1) {
                        target.inventory.addItem(transferItem)
                        player.inventory.remove(transferItem)
                        player.sendMessage("§8>> §7Du hast §e" + target.name + " erfolgreich ein Item per §5§lEnder§9Drop §r§7gesendet!")
                        target.sendMessage("§8>> §7Du hast erfolgreich §e§l" + transferAmount + " " + name + " §r§7per §5§lEnder§9Drop §r§7von §e" + player.name + "§7 erhalten!")
                    } else {
                        player.sendMessage("§8>> §cDu kannst dem Spieler: §e§l" + args[0] + " §ckein Item übergeben, da dieser keinen freien Slot im Inventar hat!")
                    }
                } else {
                    player.sendMessage("§8>> §cDer Spieler: §e§l" + args[0] + " §cist nicht online oder existiert nicht!")
                }
            } else {
                player.sendMessage("§8>> §cDu musst eine Person angeben, der du das Item überreichen willst!")
                return true
            }
        }
        return false
    }
}