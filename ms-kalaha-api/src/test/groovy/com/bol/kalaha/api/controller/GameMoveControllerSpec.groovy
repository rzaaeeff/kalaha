package com.bol.kalaha.api.controller

import com.bol.kalaha.api.model.dto.GameDto
import com.bol.kalaha.api.model.dto.GameMoveDto
import com.bol.kalaha.api.model.exception.GameNotFoundException
import com.bol.kalaha.api.service.GameMoveService
import com.bol.kalaha.core.exception.IllegalMoveException
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.benas.randombeans.EnhancedRandomBuilder
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class GameMoveControllerSpec extends Specification {

    private GameMoveService service
    private GameMoveController controller
    private MockMvc mockMvc

    void setup() {
        service = Mock(GameMoveService)
        controller = new GameMoveController(service)

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ErrorHandler())
                .build()
    }

    def random = EnhancedRandomBuilder.aNewEnhancedRandom()
    def objectMapper = new ObjectMapper().findAndRegisterModules()

    @Shared
    def gameId = "123"

    def exceptionResponseFormat = '''
        {
            "code": "%s",
            "message": %s
        }
    '''

    def notFoundResponse = String.format(exceptionResponseFormat, "kalaha.exception.game-not-found", null)
    def illegalMoveResponse = String.format(exceptionResponseFormat, "kalaha.exception.illegal-move", null)
    def unexpectedResponse = String.format(exceptionResponseFormat, "kalaha.exception.unexpected", null)

    def "creates a new move - 201"(url) {
        given:
        def gameDto = random.nextObject(GameDto.class)
        def moveDto = random.nextObject(GameMoveDto.class)
        def requestJson = objectMapper.writeValueAsString(moveDto)

        def expected = objectMapper.writeValueAsString(gameDto)

        when:
        def result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        ).andReturn()

        then:
        1 * service.create(gameId, moveDto) >> gameDto

        def response = result.response
        response.getStatus() == HttpStatus.CREATED.value()
        JSONAssert.assertEquals(expected, response.getContentAsString(), false)

        where:
        url << ["/v1/games/$gameId/moves", "/v1/games/$gameId/moves/"]
    }

    def "cannot an illegal move - 400"(url) {
        given:
        def moveDto = random.nextObject(GameMoveDto.class)
        def requestJson = objectMapper.writeValueAsString(moveDto)

        when:
        def result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        ).andReturn()

        then:
        1 * service.create(gameId, moveDto) >> {
            throw new IllegalMoveException(null)
        }

        def response = result.response
        response.getStatus() == HttpStatus.BAD_REQUEST.value()
        JSONAssert.assertEquals(illegalMoveResponse, response.getContentAsString(), false)

        where:
        url << ["/v1/games/$gameId/moves", "/v1/games/$gameId/moves/"]
    }

    def "cannot create a new move for non-existing game - 404"(url) {
        given:
        def moveDto = random.nextObject(GameMoveDto.class)
        def requestJson = objectMapper.writeValueAsString(moveDto)

        when:
        def result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        ).andReturn()

        then:
        1 * service.create(gameId, moveDto) >> {
            throw new GameNotFoundException(null)
        }

        def response = result.response
        response.getStatus() == HttpStatus.NOT_FOUND.value()
        JSONAssert.assertEquals(notFoundResponse, response.getContentAsString(), false)

        where:
        url << ["/v1/games/$gameId/moves", "/v1/games/$gameId/moves/"]
    }

    def "handles internal errors - 500"(url) {
        given:
        def moveDto = random.nextObject(GameMoveDto.class)
        def requestJson = objectMapper.writeValueAsString(moveDto)

        when:
        def result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        ).andReturn()

        then:
        1 * service.create(gameId, moveDto) >> {
            throw new RuntimeException("Internal details should not be exposed")
        }

        def response = result.response
        response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()
        JSONAssert.assertEquals(unexpectedResponse, response.getContentAsString(), false)

        where:
        url << ["/v1/games/$gameId/moves", "/v1/games/$gameId/moves/"]
    }
}
