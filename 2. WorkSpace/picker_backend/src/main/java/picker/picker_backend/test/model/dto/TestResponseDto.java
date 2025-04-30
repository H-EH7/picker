package picker.picker_backend.test.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class TestResponseDto {
    private Long id;
    private String value;

    public TestResponseDto(Long id, String value) {
        this.id = id;
        this.value = value;
    }
}
