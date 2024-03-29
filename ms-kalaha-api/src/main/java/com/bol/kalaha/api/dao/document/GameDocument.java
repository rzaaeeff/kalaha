package com.bol.kalaha.api.dao.document;

import com.bol.kalaha.api.model.enums.GameStatus;
import com.bol.kalaha.api.model.enums.PlayerID;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(collection = "games")
public class GameDocument {
    @Id
    private String id;

    private List<Integer> houses;

    private List<Integer> stores;

    private PlayerID activePlayerId;

    private GameStatus status;

    @CreatedDate
    @EqualsAndHashCode.Exclude
    private LocalDateTime createdAt;

    @LastModifiedDate
    @EqualsAndHashCode.Exclude
    private LocalDateTime updatedAt;
}
