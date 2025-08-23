package me.delyfss.medved.command

import me.delyfss.medved.Medved
import me.delyfss.medved.base.Registry
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission
import revxrsal.commands.bukkit.exception.SenderNotPlayerException

@Command("medved")
class AlertsCommand {

    @CommandPermission("medved.alerts")
    @Subcommand("alerts")
    fun alerts(actor: BukkitCommandActor) {
        val player = actor.asPlayer() ?: throw SenderNotPlayerException()

        if (Registry.enabledAlerts.remove(player)) {
            actor.reply("${Medved.PREFIX} Alerts disabled")
        } else {
            Registry.enabledAlerts.add(player).also {
                actor.reply("${Medved.PREFIX} Alerts enabled")
            }
        }
    }
}