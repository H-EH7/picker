package picker.picker_backend.post.postenum;

public enum PostStatus {
    PROCESSING,
    SUCCESS,
    FAILED,
    DLQ_FAILED,
    DLQ_PROCESSING
}
