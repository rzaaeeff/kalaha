package com.bol.kalaha.api.controller

import com.bol.kalaha.api.model.dto.GameDto
import com.bol.kalaha.api.model.exception.GameNotFoundException
import com.bol.kalaha.api.service.GameService
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.benas.randombeans.EnhancedRandomBuilder
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

class GameControllerSpec extends Specification {

    private GameService service
    private GameController controller
    private MockMvc mockMvc

    void setup() {
        service = Mock(GameService)
        controller = new GameController(service)

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ErrorHandler())
                .build()
    }

    def random = EnhancedRandomBuilder.aNewEnhancedRandom()
    def objectMapper = new ObjectMapper().findAndRegisterModules()

    def exceptionResponseFormat = '''
        {
            "code": "%s",
            "message": %s
        }
    '''

    def notFoundResponse = String.format(exceptionResponseFormat, "kalaha.exception.game-not-found", null)
    def unexpectedResponse = String.format(exceptionResponseFormat, "kalaha.exception.unexpected", null)

    def "creates a new game - 201"(url) {
        given:
        def gameDto = random.nextObject(GameDto.class)
        def expected = objectMapper.writeValueAsString(gameDto)

        when:
        def result = mockMvc.perform(post(url)).andReturn()

        then:
        1 * service.create() >> gameDto
        def response = result.response
        response.getStatus() == HttpStatus.CREATED.value()
        JSONAssert.assertEquals(expected, response.getContentAsString(), false)

        where:
        url << ["/v1/games", "/v1/games/"]
    }

    def "retrieves game - 200"() {
        given:
        def gameId = random.nextObject(String.class)
        def gameDto = random.nextObject(GameDto.class)
        def expected = objectMapper.writeValueAsString(gameDto)

        when:
        def result = mockMvc.perform(get("/v1/games/$gameId")).andReturn()

        then:
        1 * service.get(gameId) >> gameDto

        def response = result.response
        response.getStatus() == HttpStatus.OK.value()
        JSONAssert.assertEquals(expected, response.getContentAsString(), false)
    }

    def "cannot retrieve non-existing game - 404"() {
        given:
        def gameId = random.nextObject(String.class)

        when:
        def result = mockMvc.perform(get("/v1/games/$gameId")).andReturn()

        then:
        1 * service.get(gameId) >> {
            throw new GameNotFoundException(null)
        }

        def response = result.response
        response.getStatus() == HttpStatus.NOT_FOUND.value()
        JSONAssert.assertEquals(notFoundResponse, response.getContentAsString(), false)
    }

    def "deletes game - 204"() {
        given:
        def gameId = random.nextObject(String.class)

        when:
        def result = mockMvc.perform(delete("/v1/games/$gameId")).andReturn()

        then:
        1 * service.delete(gameId)

        def response = result.response
        response.getStatus() == HttpStatus.NO_CONTENT.value()
    }

    def "handles internal errors - 500"() {
        given:
        def gameId = random.nextObject(String.class)

        when:
        def result = mockMvc.perform(delete("/v1/games/$gameId")).andReturn()

        then:
        1 * service.delete(gameId) >> { throw new RuntimeException("Internal details should not be exposed") }

        def response = result.response
        response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()
        JSONAssert.assertEquals(unexpectedResponse, response.getContentAsString(), false)
    }
}
