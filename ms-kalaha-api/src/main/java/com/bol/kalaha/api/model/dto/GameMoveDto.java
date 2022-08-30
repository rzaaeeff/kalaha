package com.bol.kalaha.api.model.dto;

import com.bol.kalaha.api.model.enums.PlayerID;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Game move model")
public class GameMoveDto {
    @ApiModelProperty("ID of the pit to make the move")
    private Integer pitId;

    @ApiModelProperty("ID of the player who makes the move")
    private PlayerID playerId;
}
