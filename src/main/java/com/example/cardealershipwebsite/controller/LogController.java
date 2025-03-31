package com.example.cardealershipwebsite.controller;

import com.example.cardealershipwebsite.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Log controller. */
@Tag(name = "Логи", description = "Управление лог-файлами")
@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    /** Constructor. */
    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    /** Endpoint для получения лог-файла по дате. */
    @Operation(summary = "Получить лог-файл по дате",
            description = "Возвращает лог-файл для указанной даты. Если файл не найден, возвращается ошибка 404.")
    @GetMapping("/{date}")
    public ResponseEntity<byte[]> getLogFile(
            @Parameter(description = "Дата в формате YYYY-MM-DD") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            File logFile = logService.getLogFile(date);
            Path logFilePath = logFile.toPath();

            byte[] logFileBytes = Files.readAllBytes(logFilePath);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + logFile.getName());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .headers(headers)
                    .body(logFileBytes);
        } catch (IOException e) {
            String errorMessage = "{\"error\":\"Log file not found for the specified date: " + date + "\"}";
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorMessage.getBytes());
        }
    }
}