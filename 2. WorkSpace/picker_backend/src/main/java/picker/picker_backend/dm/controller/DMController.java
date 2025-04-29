package picker.picker_backend.dm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import picker.picker_backend.dm.model.dto.DMDto;
import picker.picker_backend.dm.service.DMService;

@Controller
@RequiredArgsConstructor
public class DMController {
    private final DMService dmService;

    @MessageMapping("/send")
    public void send(DMDto dmDto) {
        dmService.sendDM(dmDto);
    }
}
