package pl.app.thread.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThreadWithListToFetchMessage {
    @JsonProperty("url")
    private String url;
    @JsonProperty("industry_name")
    private String industryName;
}
