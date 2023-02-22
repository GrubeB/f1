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

    public void setThreadIdFromURL() {
        this.threadId = extractThreadIdFromURL();
    }

    public Long extractThreadIdFromURL() {
        try {
            String substring = URL.substring(0, URL.lastIndexOf(".html"));
            String idString = substring.substring(URL.lastIndexOf(",") + 1);
            if (idString.length() != 8) {
                String substringWithoutNumberOfPage = URL.substring(0, URL.lastIndexOf(","));
                idString = substringWithoutNumberOfPage.substring(substringWithoutNumberOfPage.lastIndexOf(",") + 1);
            }
            return Long.parseLong(idString);
        } catch (Exception exception) {
            return -1L;
        }
    }
}
