package io.ep2p.row.client.model.protocol;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResponseDto {
    private String requestId;
    private JsonNode body;
    private int status;
    private Map<String, String> headers;
    @Builder.Default
    private String type = "response";
    public Map<String, String> getHeaders() {
        return headers == null ? new HashMap<>() : headers;
    }
}
