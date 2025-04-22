package com.kokodi.service.util

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "game")
class GameProperties {
    var maxPlayers: Int = 4
    var minPlayers: Int = 2
    var maxScore: Int = 30
}