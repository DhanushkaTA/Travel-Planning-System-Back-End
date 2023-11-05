package lk.ijse.apigateway.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResponseUtil {
    private int code;
    private String message;
    private Object data;
}
