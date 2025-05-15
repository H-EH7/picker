package picker.picker_backend.post.component.manger;

import java.util.concurrent.CompletableFuture;

public interface DBManger {
    CompletableFuture<?> insert(Object dto);
    CompletableFuture<?> update(Object dto);
    CompletableFuture<?> delete(Object dto);
}
