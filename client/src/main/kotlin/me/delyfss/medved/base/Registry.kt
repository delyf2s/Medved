package me.delyfss.medved.base

import it.unimi.dsi.fastutil.objects.*
import me.delyfss.medved.base.entity.Entity
import org.bukkit.entity.Player
import java.util.UUID

object Registry {
    val entities: Object2ObjectMap<UUID, Entity> = Object2ObjectMaps.synchronize(Object2ObjectOpenHashMap())
    val enabledAlerts: ReferenceSet<Player> = ReferenceOpenHashSet()
}
