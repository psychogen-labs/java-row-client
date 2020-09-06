package lab.idioglossia.row.client.model;

import lab.idioglossia.row.client.Subscription;
import lab.idioglossia.row.client.model.protocol.RowResponseStatus;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class RowResponse<E> {
    private String requestId;
    private E body;
    private int status;
    @Builder.Default
    private Map<String, String> headers = new HashMap<String, String>();
    private Subscription subscription;

    public void setStatus(RowResponseStatus status) {
        this.status = status.getId();
    }
}