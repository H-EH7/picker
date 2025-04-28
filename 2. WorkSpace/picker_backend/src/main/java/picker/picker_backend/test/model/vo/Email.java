package picker.picker_backend.test.model.vo;

import lombok.Getter;

@Getter
public class Email {
    private final String value;

    public Email(String value) {
        if (!value.matches("^.+@.+\\..+$")) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식");
        }
        this.value = value;
    }
}
