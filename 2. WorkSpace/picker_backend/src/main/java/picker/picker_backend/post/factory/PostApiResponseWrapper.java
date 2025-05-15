package picker.picker_backend.post.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class PostApiResponseWrapper<T> {
    private boolean success;
    private T data;
    private String message;

    public static <T> PostApiResponseWrapper<T> success(T data, String message){
        return  new PostApiResponseWrapper<>(true, data, message);
    }

    public static <T> PostApiResponseWrapper<T> fail(T data, String message){
        return  new PostApiResponseWrapper<>(false, data, message);
    }

    public static <T> PostApiResponseWrapper<T> processing(T data, String message){
        return  new PostApiResponseWrapper<>(true, data, message);
    }

}
