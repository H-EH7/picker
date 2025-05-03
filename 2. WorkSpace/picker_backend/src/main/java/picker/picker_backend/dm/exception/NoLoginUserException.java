package picker.picker_backend.dm.exception;

public class NoLoginUserException extends RuntimeException {
    public NoLoginUserException() {
        super("로그인된 유저가 아닙니다.");
    }

    public NoLoginUserException(String message) {
        super(message);
    }
}
