package com.kokodi.entity.cards

import com.kokodi.entity.GameSession
import com.kokodi.service.util.ActionExecutor
import jakarta.persistence.*

@Entity
@Table(name = "cards")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "card_type")
abstract class Card(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_id_seq")
    @SequenceGenerator(name = "card_id_seq", sequenceName = "card_id_seq", allocationSize = 1)
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "card_value", nullable = false)
    val value: Int,

    @Column(name = "description", nullable = false)
    val description: String,
) {
    abstract fun action(gameSession: GameSession, actionExecutor: ActionExecutor)
}


