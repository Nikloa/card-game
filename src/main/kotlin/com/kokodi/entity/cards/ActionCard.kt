package com.kokodi.entity.cards

import com.kokodi.entity.GameSession
import com.kokodi.service.util.ActionExecutor
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
@DiscriminatorValue("ACTION")
class ActionCard(
    id: Long? = null,
    name: String,
    value: Int,
    description: String,
    @Enumerated(EnumType.STRING)
    val type: Type
) : Card(id, name, value, description) {

    enum class Type {
        BLOCK, STEAL, DOUBLE_DOWN
    }

    override fun action(gameSession: GameSession, actionExecutor: ActionExecutor) {
        when (this.type) {
            Type.BLOCK -> actionExecutor.executeBlockAction(gameSession, value)
            Type.STEAL -> actionExecutor.executeStealAction(gameSession, value)
            Type.DOUBLE_DOWN -> actionExecutor.executeDoubleDownAction(gameSession, value)
        }
    }


}