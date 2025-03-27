package com.example.cardealershipwebsite.controller;

import com.example.cardealershipwebsite.dto.BrandDto;
import com.example.cardealershipwebsite.model.Brand;
import com.example.cardealershipwebsite.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Brand Controller. */
@Validated
@RestController
@RequestMapping("/brands")
@Tag(name = "Brands", description = "Управление брендами автомобилей")
public class BrandController {
    private final BrandService brandService;

    /** Constructor. */
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @Operation(summary = "Получить список всех брендов", description = "Возвращает список всех брендов автомобилей в системе.")
    @GetMapping
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    /** Get mapping. */
    @Operation(summary = "Получить бренд по ID", description = "Возвращает информацию о бренде по указанному идентификатору.")
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(
            @Parameter(description = "ID бренда", required = true)
            @PathVariable @Min(1) long id
    ) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    /** Post mapping. */
    @Operation(summary = "Создать новый бренд", description = "Добавляет новый бренд автомобилей в систему.")
    @PostMapping
    public ResponseEntity<Brand> createBrand(
            @Parameter(description = "Данные нового бренда", required = true)
            @Valid @RequestBody Brand brand
    ) {
        return ResponseEntity.ok(brandService.createBrand(brand));
    }

    /** Delete. */
    @Operation(summary = "Удалить бренд", description = "Удаляет бренд из системы по его ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(
            @Parameter(description = "ID бренда", required = true)
            @PathVariable @Min(1) long id
    ) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}
