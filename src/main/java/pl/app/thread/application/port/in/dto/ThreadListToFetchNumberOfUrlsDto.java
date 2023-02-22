package pl.app.thread.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThreadListToFetchNumberOfUrlsDto {
    @JsonProperty("url")
    private String url;
    @JsonProperty("start_url")
    private Integer startUrl;
    @JsonProperty("umber_of_urls")
    private Integer numberOfUrls;
    @JsonProperty("industry_name")
    private String industryName;
}
