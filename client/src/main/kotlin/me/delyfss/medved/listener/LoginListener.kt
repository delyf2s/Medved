package me.delyfss.medved.listener

import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.UserDisconnectEvent
import com.github.retrooper.packetevents.event.UserLoginEvent
import me.delyfss.medved.base.entity.Entity
import me.delyfss.medved.base.Registry
import org.bukkit.Bukkit

class LoginListener : PacketListenerAbstract() {

    override fun onUserLogin(event: UserLoginEvent) {
        Bukkit.getPlayer(event.user.uuid)?.let {
            Registry.entities[event.user.uuid] = Entity(event.user.name, bukkitPlayer = it)
        }
    }

    override fun onUserDisconnect(event: UserDisconnectEvent) {
        Registry.entities.remove(event.user.uuid)
    }
}
