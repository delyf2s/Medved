package me.delyfss.medved.command

import me.delyfss.medved.Medved
import me.delyfss.medved.base.config.Config
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission

@Command("medved")
class ReloadCommand {

    @CommandPermission("medved.reload")
    @Subcommand("reload")
    fun reload(actor: BukkitCommandActor) {
        Medved.plugin.reloadConfig()
        Medved.mappedConfig = Config(Medved.plugin.config).apply {
            actor.reply("${Medved.PREFIX} Config has been reloaded")
        }
    }
}