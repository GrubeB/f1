package pl.app.thread.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendThreadListToFetchDto {
    @JsonProperty("url")
    private List<String> urlList;
    @JsonProperty("industry_name")
    private String industryName;
}
