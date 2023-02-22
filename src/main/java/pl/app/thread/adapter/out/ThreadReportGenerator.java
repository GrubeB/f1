package pl.app.thread.adapter.out;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.stereotype.Service;
import pl.app.report.application.port.in.CreateNewReport;
import pl.app.report.application.port.in.GenerateReport;
import pl.app.report.domain.CellStyleType;
import pl.app.report.domain.Report;
import pl.app.report.domain.ReportType;
import pl.app.thread.application.port.out.GenerateThreadReport;
import pl.app.thread.domain.Thread;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ThreadReportGenerator implements GenerateThreadReport {
    private final CreateNewReport createNewReport;
    private final GenerateReport generateReport;

    public byte[] generate(List<Thread> threadList, ReportType type) throws IOException {
        Report report = createNewReport.createNewReport(type);
        writeHeader(report);
        writeRows(report, threadList);
        return generateReport.generateReport(report);
    }

    private void writeRows(Report report, List<Thread> threadList) {
        CellStyle cellStylePrimary = report.generateCellStyle(CellStyleType.ARIAL, CellStyleType.FONT10, CellStyleType.BACKGROUND_PRIMARY);
        CellStyle cellStyleSecondary = report.generateCellStyle(CellStyleType.ARIAL, CellStyleType.FONT10, CellStyleType.BACKGROUND_PRIMARY_LIGHT);
        //TODO ugly as night
        List<List<Thread>> lists = threadList.stream()
                .collect(Collectors.groupingBy(Thread::getMainThreadId))
                .values().stream().toList();
        int rowIndex = 1;
        for (int listIndex = 0; listIndex < lists.size(); listIndex++) {
            List<Thread> list = lists.get(listIndex);
            for (int localRowIndex = 0; localRowIndex < list.size(); localRowIndex++) {
                Thread thread = list.get(localRowIndex);
                List<String> rowData = getRowData(thread, localRowIndex);
                report.writeRow(0, rowIndex + localRowIndex, 0, rowData, listIndex % 2 == 0 ? cellStylePrimary : cellStyleSecondary);
            }
            rowIndex += list.size();
        }

    }

    private List<String> getRowData(Thread thread, int idx) {
        return Arrays.asList(
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
    }

    private void writeHeader(Report report) {
        List<String> headers = getHeaders();
        report.setColumnsWidth(0, 0, 20, 256 * 18);
        report.setColumnsWidth(0, 2, 2, 256 * 70);
        report.setRowsHeight(0, 20 * 20);

        CellStyle headerStyle = report.generateCellStyle(CellStyleType.ARIAL, CellStyleType.BOLD, CellStyleType.FONT10, CellStyleType.BACKGROUND_PRIMARY_DARK);
        report.writeRow(0, 0, 0, headers, headerStyle);

    }

    private List<String> getHeaders() {
        return Arrays.asList(
                "Number posta",
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
