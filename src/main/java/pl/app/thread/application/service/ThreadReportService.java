package pl.app.thread.application.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.stereotype.Service;
import pl.app.report.application.port.in.CreateNewReport;
import pl.app.report.application.port.in.GenerateReport;
import pl.app.report.domain.CellStyleType;
import pl.app.report.domain.Report;
import pl.app.report.domain.ReportType;
import pl.app.thread.application.port.in.GenerateThreadReport;
import pl.app.thread.application.port.out.persistance.FindAllBetweenDates;
import pl.app.thread.domain.Thread;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
class ThreadReportService implements GenerateThreadReport {
    private final CreateNewReport createNewReport;
    private final GenerateReport generateReport;
    private final FindAllBetweenDates findAllBetweenDates;

    @Override
    public byte[] generate(ReportType type, LocalDate from, LocalDate to) throws IOException {
        if (Objects.isNull(from)) from = LocalDate.MIN;
        if (Objects.isNull(to)) to = LocalDate.now();
        List<Thread> allBetweenDates = findAllBetweenDates.findAllBetweenDates(from, to);
        return generate(allBetweenDates, type);
    }

    public byte[] generate(List<Thread> threadList, ReportType type) throws IOException {
        Report report = createNewReport.createNewReport(type);
        writeHeader(report);
        writeRows(report, threadList);
        return generateReport.generateReport(report);
    }

    private void writeRows(Report report, List<Thread> threadList) {
        CellStyle cellStylePrimary = report.generateCellStyle(CellStyleType.ARIAL, CellStyleType.FONT10, CellStyleType.BACKGROUND_PRIMARY);
        CellStyle cellStyleSecondary = report.generateCellStyle(CellStyleType.ARIAL, CellStyleType.FONT10, CellStyleType.BACKGROUND_PRIMARY_LIGHT);

        Comparator<LocalDateTime> nullLastLocalDateTimeSafe = (o1, o2) -> {
            if (Objects.isNull(o2)) return 1;
            else if (Objects.isNull(o1)) return -1;
            else return o1.compareTo(o2);
        };

        Comparator<Thread> comparator = Comparator
                .nullsLast(Comparator
                        .nullsLast(Comparator.comparing(Thread::getMainThreadId))
                        .thenComparing(Thread::getCreateDateTime, nullLastLocalDateTimeSafe));
        List<Thread> sortedThreadList = threadList.stream().sorted(comparator).toList();


        IntStream.range(0, sortedThreadList.size())
                .forEach(idx -> {
                    Thread thread = sortedThreadList.get(idx);
                    List<String> rowData = getRowData(thread);
                    report.writeRow(0, idx + 1, 0, rowData, thread.getThreadId().equals(thread.getMainThreadId()) ? cellStylePrimary : cellStyleSecondary);
                });

    }

    private List<String> getRowData(Thread thread) {
        return Arrays.asList(
                String.valueOf(Objects.equals(thread.getThreadId(), thread.getMainThreadId())),
                String.valueOf(thread.getTitle()),
                String.valueOf(thread.getComment()),
                String.valueOf(thread.getCreateDateTime()),
                String.valueOf(thread.getAuthorName()),
                String.valueOf(thread.getThreadId()),
                String.valueOf(thread.getMainThreadId()),
                String.valueOf(thread.getIndustryName()),
                String.valueOf(thread.getURL()),
                String.valueOf(thread.getExchangeRate()),
                String.valueOf(thread.getExchangeRateChange()),
                String.valueOf(thread.getNumberOfDislikes()),
                String.valueOf(thread.getNumberOfLikes())
        );
    }

    private void writeHeader(Report report) {
        List<String> headers = getHeaders();
        report.setColumnsWidth(0, 0, 20, 256 * 18);
        report.setColumnsWidth(0, 1, 2, 256 * 70);
        report.setRowsHeight(0, 20 * 20);

        CellStyle headerStyle = report.generateCellStyle(CellStyleType.ARIAL, CellStyleType.BOLD, CellStyleType.FONT10, CellStyleType.BACKGROUND_PRIMARY_DARK);
        report.writeRow(0, 0, 0, headers, headerStyle);

    }

    private List<String> getHeaders() {
        return Arrays.asList(
                "Pierwszy post",
                "Tytuł",
                "Komentarz",
                "Data stworzenia",
                "Autor",
                "PostId",
                "MainPostId",
                "Spółka",
                "URL",
                "Kurs walutowy",
                "Kurs walutowy zmiana",
                "Dislikes",
                "Likes"
        );
    }
}

