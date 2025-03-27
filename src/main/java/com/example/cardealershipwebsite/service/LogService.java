package com.example.cardealershipwebsite.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;

/** Service. */
@Service
public class LogService {

    /** Get log file. */
    public File getLogFile(String date) throws IOException {
        // Получаем текущую дату или переданную дату
        LocalDate logDate = date != null ? LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE) : LocalDate.now();
        String logFileName = "application.log";

        // Путь к лог-файлу
        Path logFilePath = Paths.get("logs", logFileName);

        if (Files.notExists(logFilePath)) {
            throw new FileNotFoundException("Log file not found: " + logFileName);
        }

        // Чтение строк из файла
        List<String> filteredLines = Files.readAllLines(logFilePath).stream()
                .filter(line -> line.startsWith(logDate.toString()))
                .toList();

        if (filteredLines.isEmpty()) {
            throw new FileNotFoundException("No logs found for date: " + logDate);
        }

        Path tempDir = Files.createTempDirectory(Paths.get(System.getProperty("java.io.tmpdir")), "logsafe");
        Path tempFile = Files.createTempFile(tempDir, "logs-" + logDate, ".log");

        // Записываем отфильтрованные строки в новый файл
        Files.write(tempFile, filteredLines);

        // Возвращаем временный файл
        return tempFile.toFile();
    }
}
