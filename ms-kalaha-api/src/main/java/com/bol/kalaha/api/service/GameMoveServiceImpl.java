package com.bol.kalaha.api.service;

import com.bol.kalaha.api.dao.repository.GameRepository;
import com.bol.kalaha.api.mapper.GameMapper;
import com.bol.kalaha.api.model.dto.GameDto;
import com.bol.kalaha.api.model.dto.GameMoveDto;
import com.bol.kalaha.api.model.exception.GameNotFoundException;

public class GameMoveServiceImpl implements GameMoveService {
    private final GameRepository repository;

    public GameMoveServiceImpl(GameRepository repository) {
        this.repository = repository;
    }

    @Override
    public GameDto create(String gameId, GameMoveDto moveDto) {
        var gameDocument = repository.findById(gameId).orElseThrow(
                () -> new GameNotFoundException("Game not found by ID=" + gameId)
        );

        var gameCore = GameMapper.INSTANCE.documentToCoreModel(gameDocument);
        gameCore.move(GameMapper.INSTANCE.playerIdToCorePlayerId(moveDto.getPlayerId()), moveDto.getPitId());

        var newGameDocument = GameMapper.INSTANCE.mergeCoreModelIntoDocument(gameCore, gameDocument);
        var savedGameDocument = repository.save(newGameDocument);

        return GameMapper.INSTANCE.documentToDto(savedGameDocument);
    }
}
