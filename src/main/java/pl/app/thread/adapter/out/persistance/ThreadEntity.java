package pl.app.thread.adapter.out.persistance;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ThreadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "thread_id")
    private Long threadId;
    @Column(name = "main_thread_id")
    private Long mainThreadId;
    @Column(name = "url",length = 10*1024)
    private String URL;
    @Column(name = "title",length = 10*1024)
    private String title;
    @Column(name = "author_name",length = 255)
    private String authorName;
    @Column(name = "create_date_time")
    private LocalDateTime createDateTime;
    @Column(name = "exchange_rate")
    private Double exchangeRate;
    @Column(name = "exchange_rate_change")
    private Double exchangeRateChange;
    @Column(name = "comment",length = 100*1024)
    private String comment;
    @Column(name = "number_of_dislikes")
    private Integer numberOfDislikes;
    @Column(name = "number_of_likes")
    private Integer numberOfLikes;
    @Column(name = "has_been_fetched")
    private Boolean hasBeenFetched = false;
}
