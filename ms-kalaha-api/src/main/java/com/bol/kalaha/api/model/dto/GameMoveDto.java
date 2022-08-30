package com.bol.kalaha.api.model.dto;

import com.bol.kalaha.api.model.enums.PlayerID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameMoveDto {
    private Integer pitId;
    private PlayerID playerId;
}
