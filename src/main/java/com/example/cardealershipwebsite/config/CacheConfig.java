package com.example.cardealershipwebsite.config;

import com.example.cardealershipwebsite.dto.CarDto;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Cache conf. */
@Configuration
public class CacheConfig {

    /** Hash map for cache. */
    @Bean
    public Map<String, List<CarDto>> carFilterCache() {
        return new LinkedHashMap<>(16, 0.75f, true) {
            private static final int MAX_ENTRIES = 10;

            @Override
            protected boolean removeEldestEntry(Map.Entry<String, List<CarDto>> eldest) {
                return size() > MAX_ENTRIES; // Удаляем самый старый элемент, если размер больше 10
            }
        };
    }
}
