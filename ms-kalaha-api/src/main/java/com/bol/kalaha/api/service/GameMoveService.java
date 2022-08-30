package com.bol.kalaha.api.service;

import com.bol.kalaha.api.model.dto.GameDto;
import com.bol.kalaha.api.model.dto.GameMoveDto;
import org.springframework.stereotype.Service;

@Service
public interface GameMoveService {
    GameDto create(String gameId, GameMoveDto move);
}
