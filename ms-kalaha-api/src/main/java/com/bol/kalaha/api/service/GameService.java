package com.bol.kalaha.api.service;

import com.bol.kalaha.api.model.dto.GameDto;

public interface GameService {
    GameDto create();

    GameDto get(String id);

    void delete(String id);
}
