package picker.picker_backend.dm.component.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import picker.picker_backend.dm.exception.NoLoginUserException;

@Component
public class HttpSessionDMUserManager implements DMUserManager{
    @Override
    public String getUserId(HttpRequest request) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpSession session = servletRequest.getServletRequest().getSession(false);

            if (session != null) {
                return session.getAttribute("userId").toString();
            }
        }

        // 로그인된 세션이 없음
        throw new NoLoginUserException();
    }
}
