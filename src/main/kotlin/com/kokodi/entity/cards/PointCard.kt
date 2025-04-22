package com.kokodi.entity.cards

import com.kokodi.entity.GameSession
import com.kokodi.service.util.ActionExecutor
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("POINT")
class PointCard(id: Long? = null, name: String, value: Int, description: String) : Card(id, name, value, description) {
    override fun action(gameSession: GameSession, actionExecutor: ActionExecutor) {
        actionExecutor.executeAddPointsAction(gameSession, value)
    }
}