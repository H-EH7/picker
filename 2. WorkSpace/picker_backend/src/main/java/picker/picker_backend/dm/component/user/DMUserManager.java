package picker.picker_backend.dm.component.user;

import org.springframework.http.HttpRequest;

public interface DMUserManager {
    String getUserId(HttpRequest request);
}
