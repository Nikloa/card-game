package com.kokodi.controller

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WireMockTest(httpPort = 0)
@AutoConfigureMockMvc(addFilters = false)
class GameControllerIntTest : AbstractIT() {

    private val url = "/api/games"

    @AfterEach
    fun cleanUp() {
        executeScript("script/delete_test_data.sql")
    }

    @Test
    fun testExecuteScript() {
        println("\n--------------------------------------\n")
        println("Executing script: script/turn_block_game.sql")
        executeScript("script/turn_block_game.sql")
        printTableData("cards")
        println("\n--------------------------------------\n")
    }

    fun printTableData(table: String) {
        val sql = "select * from $table"

        val jdbcTemplate = JdbcTemplate(ds)
        val rows = jdbcTemplate.queryForList(sql)

        for (row in rows) {
            println(row)
        }
    }

    @Test
    @WithMockUser(username = "Alex")
    fun `createGameSession should return CREATED status and GameDto`() {
        var response = readJson("src/test/resources/response/NewGameResponse.json")

        executeScript("script/new_game.sql")

        val actual = mockMvc.perform(postRequest("$url/new", ""))
            .andExpect(
                status().isCreated
            ).andReturn().response
        actual.characterEncoding = "UTF-8"

        val actualAsTree = objectMapper.readTree(actual.contentAsString)

        response = response.replace("{id}", actualAsTree.get("players")[0].get("playerId").asText())


        assertEquals(objectMapper.readTree(response), actualAsTree)
    }

    @Test
    @WithMockUser(username = "Neo")
    fun `joinGameSession should return OK status and GameDto`() {
        val sessionId = 1
        var response = readJson("src/test/resources/response/JoinGameResponse.json")

        executeScript("script/join_game.sql")

        val actual = mockMvc.perform(postRequest("$url/$sessionId/join", ""))
            .andExpect(
                status().isOk
            ).andReturn().response
        actual.characterEncoding = "UTF-8"

        val actualAsTree = objectMapper.readTree(actual.contentAsString)

        response = response.replace("{id}", actualAsTree.get("players")[1].get("playerId").asText())

        assertEquals(objectMapper.readTree(response), actualAsTree)
    }

    @Test
    @WithMockUser(username = "Neo")
    fun `startGameSession should return OK status and GameDto`() {
        val sessionId = 1
        val response = readJson("src/test/resources/response/StartGameResponse.json")

        executeScript("script/start_game.sql")

        val actual = mockMvc.perform(postRequest("$url/$sessionId/start", ""))
            .andExpect(
                status().isOk
            ).andReturn().response
        actual.characterEncoding = "UTF-8"

        val actualAsTree = objectMapper.readTree(actual.contentAsString)

        assertEquals(objectMapper.readTree(response), actualAsTree)
    }

    @Test
    @WithMockUser(username = "Alex")
    fun `stepGameSession with next Block Card should return OK status and GameDto`() {
        val sessionId = 1
        val response = readJson("src/test/resources/response/TurnBlockGameResponse.json")

        executeScript("script/turn_block_game.sql")

        val actual = mockMvc.perform(postRequest("$url/$sessionId/turn", ""))
            .andExpect(
                status().isOk
            ).andReturn().response
        actual.characterEncoding = "UTF-8"

        val actualAsTree = objectMapper.readTree(actual.contentAsString)

        assertEquals(objectMapper.readTree(response), actualAsTree)
    }

    @Test
    @WithMockUser(username = "Alex")
    fun `stepGameSession with next DoubleDown Card should return OK status and GameDto`() {
        val sessionId = 1
        val response = readJson("src/test/resources/response/TurnDoubleDownGameResponse.json")

        executeScript("script/turn_double_down_game.sql")

        val actual = mockMvc.perform(postRequest("$url/$sessionId/turn", ""))
            .andExpect(
                status().isOk
            ).andReturn().response
        actual.characterEncoding = "UTF-8"

        val actualAsTree = objectMapper.readTree(actual.contentAsString)

        assertEquals(objectMapper.readTree(response), actualAsTree)
    }

    @Test
    @WithMockUser(username = "Alex")
    fun `stepGameSession with next Point Card should return OK status and GameDto`() {
        val sessionId = 1
        val response = readJson("src/test/resources/response/TurnPointGameResponse.json")

        executeScript("script/turn_point_game.sql")

        val actual = mockMvc.perform(postRequest("$url/$sessionId/turn", ""))
            .andExpect(
                status().isOk
            ).andReturn().response
        actual.characterEncoding = "UTF-8"

        val actualAsTree = objectMapper.readTree(actual.contentAsString)

        assertEquals(objectMapper.readTree(response), actualAsTree)
    }

    @Test
    @WithMockUser(username = "Alex")
    fun `stepGameSession with next Steal Card should return OK status and GameDto`() {
        val sessionId = 1
        val response = readJson("src/test/resources/response/TurnStealGameResponse.json")

        executeScript("script/turn_steal_game.sql")

        val actual = mockMvc.perform(postRequest("$url/$sessionId/turn", ""))
            .andExpect(
                status().isOk
            ).andReturn().response
        actual.characterEncoding = "UTF-8"

        val actualAsTree = objectMapper.readTree(actual.contentAsString)

        assertEquals(objectMapper.readTree(response), actualAsTree)
    }

    @Test
    @WithMockUser(username = "Alex")
    fun `stealAction with previous Steal Card should return OK status and GameDto`() {
        val sessionId = 1
        val playerId = 2
        val response = readJson("src/test/resources/response/TurnStealPlayerGameResponse.json")

        executeScript("script/turn_steal_player_game.sql")

        val actual = mockMvc.perform(postRequest("$url/$sessionId/turn/$playerId", ""))
            .andExpect(
                status().isOk
            ).andReturn().response
        actual.characterEncoding = "UTF-8"

        val actualAsTree = objectMapper.readTree(actual.contentAsString)

        assertEquals(objectMapper.readTree(response), actualAsTree)
    }
}