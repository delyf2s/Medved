package me.delyfss.medved.listener

import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying
import me.delyfss.medved.base.Registry
import me.delyfss.medved.base.entity.Frame

class FactoryListener : PacketListenerAbstract() {

    override fun onPacketReceive(event: PacketReceiveEvent) {
        Registry.entities[event.user.uuid]?.let { entity ->
            when {
                WrapperPlayClientPlayerFlying.isFlying(event.packetType) -> {
                    entity.flying++

                    val wrapper = WrapperPlayClientPlayerFlying(event)
                    if (wrapper.hasRotationChanged()) {
                        val location = wrapper.location

                        entity.checks.forEach {
                            it.onFrame(Frame(location.yaw - entity.x, location.pitch - entity.y))
                        }

                        entity.x = location.yaw
                        entity.y = location.pitch
                    }
                }
                event.packetType == PacketType.Play.Client.INTERACT_ENTITY -> {
                    if (WrapperPlayClientInteractEntity(event).action == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                        entity.flying = 0
                    }
                }
            }
        }
    }
}
