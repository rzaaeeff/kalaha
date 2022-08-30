package com.bol.kalaha.api.model.dto;

import com.bol.kalaha.api.model.enums.GameStatus;
import com.bol.kalaha.api.model.enums.PlayerID;
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
public class GameDto {
    private String id;

    private List<Integer> houses;

    private List<Integer> stores;

    private PlayerID activePlayerId;

    private GameStatus status;

    private LocalDateTime createdAt;
}
