package com.bol.kalaha.api.service;

import com.bol.kalaha.api.model.dto.GameDto;
import com.bol.kalaha.api.model.dto.GameMoveDto;

public interface GameMoveService {
    GameDto create(String gameId, GameMoveDto move);
}
