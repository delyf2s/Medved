package me.delyfss.medved.base.config

import org.bukkit.configuration.file.FileConfiguration

class Config(config: FileConfiguration) {
    var punishment: String = config.getString("punishment") ?: ""
}
