package me.delyfss.medved.base.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import me.delyfss.medved.Medved
import me.delyfss.medved.check.Check
import me.delyfss.medved.check.impl.AimCheck
import me.delyfss.medved.base.Registry
import org.bukkit.Bukkit
import org.bukkit.entity.Player

@JsonClass(generateAdapter = true)
data class Entity(
    val name: String,
    @Deprecated("Used in data collection") var sus: Boolean = false,
    val frames: MutableList<Frame> = ObjectArrayList(600),
    @Json(ignore = true) var flying: Int = 21,
    @Json(ignore = true) var x: Float = 0f,
    @Json(ignore = true) var y: Float = 0f,
    @Json(ignore = true) val bukkitPlayer: Player? = null,
    @Json(ignore = true) val checks: ObjectArrayList<Check> = ObjectArrayList(),
) {
    init {
        checks.add(AimCheck(this@Entity))
    }

    fun alert(check: String, info: String, punish: Boolean = false) {
        val message ="${Medved.PREFIX} $name §b$check §7$info"

        Registry.enabledAlerts.forEach { it.sendMessage(message) }

        if (punish) {
            punish()
        }
    }

    private fun punish() {
       Bukkit.getScheduler().runTask(Medved.plugin, Runnable {
           Bukkit.dispatchCommand(
               Bukkit.getConsoleSender(),
               Medved.mappedConfig.punishment.replace("%player%", name)
           )
       })
    }
}
