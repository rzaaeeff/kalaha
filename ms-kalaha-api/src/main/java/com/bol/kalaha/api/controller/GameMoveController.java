package com.bol.kalaha.api.controller;

import com.bol.kalaha.api.model.dto.GameDto;
import com.bol.kalaha.api.model.dto.GameMoveDto;
import com.bol.kalaha.api.service.GameMoveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/games/{gameId}/moves")
@Api("Game move controller")
public class GameMoveController {
    private final GameMoveService service;

    public GameMoveController(GameMoveService service) {
        this.service = service;
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Make a move in game")
    public GameDto create(@PathVariable String gameId, @RequestBody GameMoveDto moveDto) {
        return service.create(gameId, moveDto);
    }
}
