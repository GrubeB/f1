package pl.app.thread.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.app.excel.Report;
import pl.app.excel.ReportGenerator;
import pl.app.excel.ReportType;
import pl.app.thread.application.port.out.GenerateThreadReport;
import pl.app.thread.domain.Thread;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ThreadReportGenerator implements GenerateThreadReport {
    private final ReportGenerator reportGenerator;

    public byte[] generate(List<Thread> threadList, ReportType type) throws IOException {
        Report report = reportGenerator.createNewReport(type);
        List<String> headers = Arrays.asList(
                "nr",
                "isMainThread",
                "title",
                "comment",
                "createDateTime",
                "authorName",
                "threadId",
                "mainThreadId",
                "industryName",
                "URL",
                "exchangeRate",
                "exchangeRateChange",
                "numberOfDislikes",
                "numberOfLikes"
        );
        report.setColumnsWidth(0,0,20,256*18);
        report.setColumnsWidth(0,2,2,256*70);
        report.writeRow(0, 0, 0, headers);
        IntStream.range(0, threadList.size()).forEach(idx -> {
            Thread thread = threadList.get(idx);
            List<String> row = Arrays.asList(
                    String.valueOf(idx),
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
            report.writeRow(0, idx+1, 0, row);
        });
        return reportGenerator.generateReport(report);
    }
}
