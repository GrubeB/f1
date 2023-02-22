package pl.app.thread.application.port.out.persistance;

import pl.app.thread.domain.Thread;

import java.time.LocalDate;
import java.util.List;

public interface FindAllBetweenDates {
    List<Thread> findAllBetweenDates(LocalDate from, LocalDate to);
}
