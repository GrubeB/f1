package pl.app.thread.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThreadToFetchMessage {
    @JsonProperty("url")
    private String url;
    @JsonProperty("main_thread_id")
    private Long mainThreadId;
}
