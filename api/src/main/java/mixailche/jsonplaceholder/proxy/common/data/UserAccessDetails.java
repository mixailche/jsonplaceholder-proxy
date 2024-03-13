package mixailche.jsonplaceholder.proxy.common.data;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class UserAccessDetails {
    private long userId;
    private Map<ContentType, AccessLevel> permissions;
}
