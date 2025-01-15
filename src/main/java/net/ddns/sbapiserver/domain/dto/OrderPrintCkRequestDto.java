package net.ddns.sbapiserver.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderPrintCkRequestDto {
    @Schema(name = "order_id")
    private int orderId;
}
