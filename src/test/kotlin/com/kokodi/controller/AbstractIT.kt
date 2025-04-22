package com.kokodi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.io.File
import java.time.LocalDate
import javax.sql.DataSource
import kotlin.text.Charsets.UTF_8

/**
 * Абстрактный класс для интеграционных тестов.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
abstract class AbstractIT {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var ds: DataSource

    protected val objectMapper: ObjectMapper = ObjectMapper().findAndRegisterModules()

    companion object {
        /**
         * Use only for start Spring application context!
         */

        @JvmStatic
        val wireMockServer: WireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort())

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
            wireMockServer.start()

        }

    }


    /**
     * Метод создания RequestBuilder для GET-запроса.
     *
     * @param url URL GET-запроса
     * @return MockHttpServletRequestBuilder для дальнейшего построения запроса
     */
    protected fun getRequest(url: String): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders
            .request(HttpMethod.GET, url)
            .characterEncoding(UTF_8.toString())
            .contentType(MediaType.APPLICATION_JSON)
    }

    /**
     * Метод создания RequestBuilder для POST-запроса.
     *
     * @param url URL POST-запроса
     * @return MockHttpServletRequestBuilder для дальнейшего построения запроса
     */
    protected fun postRequest(url: String, body: String): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders
            .request(HttpMethod.POST, url)
            .characterEncoding(UTF_8.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
    }

    /**
     * Метод создания RequestBuilder для PATCH-запроса.
     *
     * @param url URL PATCH-запроса
     * @return MockHttpServletRequestBuilder для дальнейшего построения запроса
     */
    protected fun patchRequest(url: String, body: String): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders
            .request(HttpMethod.PATCH, url)
            .characterEncoding(UTF_8.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
    }

    /**
     * Метод для чтение JSON-файла.
     *
     * @param file путь к файлу от корня контента.
     */
    protected fun readJson(file: String): String = File(file).bufferedReader().readText()

    /**
     * Метод для обработки исключения.
     *
     * @param jsonPath путь к JSON-файлу исключения от корня контента.
     * @param result результат, полученный после выполнения запроса.
     */
    protected fun `Perform exception`(jsonPath: String, result: ResultActions) {
        val actual = result.andReturn().response.contentAsString
        var expected = readJson(jsonPath)
        val actualAsTree = objectMapper.readTree(actual)
        expected = expected.replace("\${timestamp}", actualAsTree.get("timestamp").asText())
        Assertions.assertEquals(objectMapper.readTree(expected), actualAsTree)
    }

    /**
     * Метод для исполнения SQL-скрипта.
     *
     * @param path путь к SQL-скрипту от корня источника.
     */
    protected fun executeScript(path: String) {
        val populator = ResourceDatabasePopulator()
        populator.addScript(ClassPathResource(path))
        populator.execute(ds)
    }

}
