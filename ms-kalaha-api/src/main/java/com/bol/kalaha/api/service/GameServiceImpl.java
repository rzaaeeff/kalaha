package com.bol.kalaha.api.service;

import com.bol.kalaha.api.dao.repository.GameRepository;
import com.bol.kalaha.api.mapper.GameMapper;
import com.bol.kalaha.api.model.dto.GameDto;
import com.bol.kalaha.api.model.exception.GameNotFoundException;
import com.bol.kalaha.core.Game;
import com.bol.kalaha.core.model.Board;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository repository;

    public GameServiceImpl(GameRepository repository) {
        this.repository = repository;
    }

    @Override
    public GameDto create() {
        var game = Game.create(Board.create(6, 6));
        var document = GameMapper.INSTANCE.coreModelToDocument(game);
        var savedDocument = repository.save(document);

        return GameMapper.INSTANCE.documentToDto(savedDocument);
    }

    @Override
    public GameDto get(String id) {
        var document = repository.findById(id).orElseThrow(
                () -> new GameNotFoundException("Game not found by ID=" + id)
        );

        return GameMapper.INSTANCE.documentToDto(document);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new GameNotFoundException("Game not found by ID=" + id);
        }
        repository.deleteById(id);
    }
}
