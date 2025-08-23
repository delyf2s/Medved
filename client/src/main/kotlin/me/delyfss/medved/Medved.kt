package me.delyfss.medved

import com.github.retrooper.packetevents.PacketEvents
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
import me.delyfss.medved.command.AlertsCommand
import me.delyfss.medved.command.ReloadCommand
import me.delyfss.medved.base.config.Config
import me.delyfss.medved.listener.FactoryListener
import me.delyfss.medved.listener.LoginListener
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.bukkit.BukkitLamp
import me.delyfss.medved.base.Registry

class Medved : JavaPlugin() {

    companion object {
        const val PREFIX = "§7[§fMedved§7]"
        lateinit var plugin: JavaPlugin
        lateinit var mappedConfig: Config
    }
    
    @Suppress("UnstableApiUsage")
    override fun onEnable() {
        plugin = this

        saveDefaultConfig()
        mappedConfig = Config(config)

        Registry

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this))
        PacketEvents.getAPI().apply {
            settings.fullStackTrace(true)
                .kickOnPacketException(true)
                .checkForUpdates(false)
                .reEncodeByDefault(false)
                .debug(false)
            init()
        }
        PacketEvents.getAPI().eventManager.registerListeners(FactoryListener(), LoginListener())

        BukkitLamp.builder(this).build().apply {
            register(AlertsCommand(), ReloadCommand())
        }
    }

    override fun onDisable() {
        PacketEvents.getAPI().terminate()
    }
}