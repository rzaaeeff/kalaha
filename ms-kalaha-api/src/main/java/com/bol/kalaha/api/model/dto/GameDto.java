package com.bol.kalaha.api.model.dto;

import com.bol.kalaha.api.model.enums.GameStatus;
import com.bol.kalaha.api.model.enums.PlayerID;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Game model")
public class GameDto {
    @ApiModelProperty("ID of the game")
    private String id;

    @ApiModelProperty("Seed count in each house")
    private List<Integer> houses;

    @ApiModelProperty("Seed count in each store")
    private List<Integer> stores;

    @ApiModelProperty("ID of the active player")
    private PlayerID activePlayerId;

    @ApiModelProperty("Current status of the game")
    private GameStatus status;

    @ApiModelProperty("Creation date of the game")
    private LocalDateTime createdAt;
}
