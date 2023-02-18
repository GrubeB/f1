package pl.app.thread.domain;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Thread {
    private Long id;
    private Long threadId;
    private String industryName;
    private Long mainThreadId;
    private String URL;
    private String title;
    private String authorName;
    private LocalDateTime createDateTime;
    private Double exchangeRate;
    private Double exchangeRateChange;
    private String comment;
    private Integer numberOfDislikes;
    private Integer numberOfLikes;
    private Boolean hasBeenFetched = false;
}
