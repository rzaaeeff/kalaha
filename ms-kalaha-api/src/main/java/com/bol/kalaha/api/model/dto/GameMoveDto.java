package com.bol.kalaha.api.model.dto;

import com.bol.kalaha.api.model.enums.PlayerID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameMoveDto {
    private Integer pitId;
    private PlayerID playerId;
}
