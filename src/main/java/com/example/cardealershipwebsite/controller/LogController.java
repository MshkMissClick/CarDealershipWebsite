package com.example.cardealershipwebsite.controller;

import com.example.cardealershipwebsite.model.LogRequestInfo;
import com.example.cardealershipwebsite.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
            @Parameter(description = "Дата в формате YYYY-MM-DD") @PathVariable String date) {
        try {
            // Получаем временный лог-файл по дате
            File logFile = logService.getLogFile(date);
            Path logFilePath = logFile.toPath();

            // Читаем файл в байты
            byte[] logFileBytes = Files.readAllBytes(logFilePath);

            // Формируем ответ с файлом
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + logFile.getName());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .headers(headers)
                    .body(logFileBytes);
        } catch (IOException e) {
            // Возвращаем ошибку, если файл не найден или произошла ошибка
            String errorMessage = "{\"error\":\"Log file not found for the specified date: " + date + "\"}";
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorMessage.getBytes());
        }
    }

    /**Create.*/
    @PostMapping("/async/{date}")
    public ResponseEntity<Map<String, String>> createLogAsync(@PathVariable String date) {
        String id = logService.createAsyncLog(date);
        return ResponseEntity.ok(Map.of("requestId", id));
    }

    /**Get.*/
    @GetMapping("/status/{id}")
    public ResponseEntity<Map<String, String>> getStatus(@PathVariable String id) {
        LogRequestInfo info = logService.getStatus(id);
        if (info == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "NOT_FOUND"));
        }
        return ResponseEntity.ok(Map.of("status", info.getStatus().name()));
    }

    /**Download.*/
    @GetMapping("/file/{id}")
    public ResponseEntity<?> downloadLogFile(@PathVariable String id) {
        File file = logService.getReadyFile(id);
        if (file == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "File not found or not ready"));
        }

        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .headers(headers)
                    .body(bytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to read file"));
        }
    }
}