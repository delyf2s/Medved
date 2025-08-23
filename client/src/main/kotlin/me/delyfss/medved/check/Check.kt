package me.delyfss.medved.check

import it.unimi.dsi.fastutil.longs.Long2IntRBTreeMap
import me.delyfss.medved.base.entity.Entity
import me.delyfss.medved.base.entity.Frame
import kotlin.time.Duration

open class Check(
    private val entity: Entity,
    private val name: String,
    private var timeout: Duration,
    private val threshold: Int
) {
    private val buffer = Long2IntRBTreeMap()

    fun alert(info: String = "", add: Int = 0, punish: Boolean = run {
        val now = System.currentTimeMillis()
        val it = buffer.long2IntEntrySet().iterator()
        while (it.hasNext()) {
            if (now - it.next().longKey > timeout.inWholeMilliseconds) it.remove()
        }
        buffer.values.sum() + add >= threshold
    }) {
        if (add != 0) buffer[System.currentTimeMillis()] = add
        entity.alert(name, buildString {
            if (info.isNotEmpty()) append(info).append(' ')
            append("${buffer.values.sum()}/$threshold")
        }, punish)
    }

    fun reward(remove: Int) {
        buffer[System.currentTimeMillis()] = -minOf(remove, buffer.values.sum())
    }

    open fun onFrame(frame: Frame) {}
}
