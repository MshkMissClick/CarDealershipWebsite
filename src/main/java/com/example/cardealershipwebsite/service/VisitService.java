package com.example.cardealershipwebsite.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

/** Сервис учёта посещений URL. */
@Service
public class VisitService {

    private final ConcurrentHashMap<String, AtomicInteger> visitCounters = new ConcurrentHashMap<>();

    /** Увеличить счётчик посещений по URL. */
    public void registerVisit(String url) {
        visitCounters.computeIfAbsent(url, k -> new AtomicInteger(0)).incrementAndGet();
    }

    /** Получить количество посещений по URL. */
    public int getVisitCount(String url) {
        return visitCounters.getOrDefault(url, new AtomicInteger(0)).get();
    }

    /** Получить все визиты. */
    public ConcurrentHashMap<String, Integer> getAllVisitCounts() {
        ConcurrentHashMap<String, Integer> result = new ConcurrentHashMap<>();
        visitCounters.forEach((k, v) -> result.put(k, v.get()));
        return result;
    }
}