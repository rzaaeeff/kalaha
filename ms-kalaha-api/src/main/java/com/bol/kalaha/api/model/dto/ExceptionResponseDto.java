package com.bol.kalaha.api.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Exception response")
public class ExceptionResponseDto {

    @ApiModelProperty("Error code")
    private String code;

    @ApiModelProperty("Error message")
    private String message;
}
