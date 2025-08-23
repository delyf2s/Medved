package me.delyfss.medved.check.impl

import me.delyfss.medved.check.Check
import me.delyfss.medved.base.entity.Entity
import me.delyfss.medved.base.entity.Frame
import me.delyfss.medved.base.model.ModelService
import kotlin.time.Duration.Companion.minutes

class AimCheck(private val entity: Entity) : Check(entity, "Aim", 20.minutes, 6) {

    override fun onFrame(frame: Frame) {
        if (entity.flying <= 20) {
            entity.frames.add(frame)

            if (entity.frames.size == 100) {
                ModelService.test(entity) {
                    if (it.proba > 0.5) {
                        alert("%.4f".format(it.proba), 2)
                    } else {
                        reward(1)
                    }
                }
                entity.frames.clear()
            }
        }
    }
}