package com.example.cardealershipwebsite.service;

import com.example.cardealershipwebsite.model.LogRequestInfo;
import java.io.File;
import java.util.concurrent.ConcurrentMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**da.*/
@Service
public class LogTaskService {

    private final LogService logService;

    /**da.*/
    public LogTaskService(@Lazy LogService logService) {
        this.logService = logService;
    }

    /**da.*/
    @Async
    public void generateLogFileAsync(String requestId, String date, ConcurrentMap<String, LogRequestInfo> store) {
        try {
            Thread.sleep(20000);
            File file = logService.getLogFile(date);
            store.put(requestId, new LogRequestInfo(requestId, date, LogRequestInfo.Status.DONE, file, null));
        } catch (Exception e) {
            store.put(requestId, new LogRequestInfo(requestId, date, LogRequestInfo.Status.ERROR, null, e.getMessage()));
        }
    }
}