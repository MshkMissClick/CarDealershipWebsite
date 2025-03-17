package com.example.cardealershipwebsite.config;

import com.example.cardealershipwebsite.dto.CarDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class CacheConfig {

    @Bean
    public LinkedHashMap<String, List<CarDto>> carFilterCache() {
        return new LinkedHashMap<String, List<CarDto>>(16, 0.75f, true) {
            private static final int MAX_ENTRIES = 10;

            @Override
            protected boolean removeEldestEntry(Map.Entry<String, List<CarDto>> eldest) {
                return size() > MAX_ENTRIES; // Удаляем самый старый элемент, если размер больше 10
            }
        };
    }
}
