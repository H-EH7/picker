package picker.picker_backend.Auth.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class User {
    private String userId; //ID
    private String name; //이름
    private String password; //비밀번호
    private String nickname; //닉네임
    private String phone; //핸드폰
    private char gender; //성별`
    private int loginFailCount;
    private LocalDate createAt;
}
