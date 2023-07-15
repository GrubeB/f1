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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    @Override
    public byte[] generateZip(ReportType type) throws IOException {
        List<AbstractMap.SimpleEntry<LocalDate, LocalDate>> dates = getDates();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)) {
            dates.forEach((entry) -> {
                String dateFromString = DateTimeFormatter.ISO_LOCAL_DATE.format(entry.getKey());
                String dateToString = DateTimeFormatter.ISO_LOCAL_DATE.format(entry.getValue());
                try {
                    byte[] report = generate(ReportType.XLSX, entry.getKey(), entry.getValue());
                    putFileToZipOutputStream(zipOut, report, "report_" + dateFromString + "_" + dateToString + "." + type.getExtension());
                } catch (IOException e) {
                }
            });
            zipOut.closeEntry();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private List<AbstractMap.SimpleEntry<LocalDate, LocalDate>> getDates() {
        return Arrays.asList(
                new AbstractMap.SimpleEntry<>(LocalDate.of(2000, 1, 1), LocalDate.of(2015, 12, 31)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 6, 30)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2016, 7, 1), LocalDate.of(2016, 12, 31)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 6, 30)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2017, 7, 1), LocalDate.of(2017, 12, 31)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 6, 30)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2018, 7, 1), LocalDate.of(2018, 12, 31)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2019, 1, 1), LocalDate.of(2019, 6, 30)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2019, 7, 1), LocalDate.of(2019, 12, 31)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 6, 30)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2020, 7, 1), LocalDate.of(2020, 12, 31)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 6, 30)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2021, 7, 1), LocalDate.of(2021, 12, 31)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 6, 30)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2022, 7, 1), LocalDate.of(2022, 12, 31)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 6, 30)),
                new AbstractMap.SimpleEntry<>(LocalDate.of(2023, 7, 1), LocalDate.of(2023, 12, 31))
        );
    }

    private void putFileToZipOutputStream(ZipOutputStream zipOut, byte[] file, String fileName) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);

        ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(file);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = byteArrayIn.read(buffer)) > 0) {
            zipOut.write(buffer, 0, len);
        }
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
            if (Objects.isNull(o1) && Objects.isNull(o2)) return 0;
            if (Objects.isNull(o2)) return 1;
            else if (Objects.isNull(o1)) return -1;
            else return o1.compareTo(o2);
        };

        Comparator<Thread> comparator = Comparator.nullsLast(
                Comparator.nullsLast(Comparator.comparing(Thread::getMainThreadId))
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

