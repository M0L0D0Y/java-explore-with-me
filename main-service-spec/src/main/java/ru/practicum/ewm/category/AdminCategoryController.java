package ru.practicum.ewm.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.common.Create;
import ru.practicum.ewm.common.Update;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping
@Slf4j
@Validated
public class AdminCategoryController {
    private final CategoryMapper categoryMapper;
    private final AdminService adminService;

    @Autowired
    public AdminCategoryController(CategoryMapper categoryMapper, AdminService adminService) {
        this.categoryMapper = categoryMapper;
        this.adminService = adminService;
    }

    @PatchMapping(value = "/admin/categories")
    public CategoryDto updateCategory(@RequestBody @Validated(Update.class) CategoryDto categoryDto) {
        Category category = categoryMapper.toCategoryFromCategoryDto(categoryDto);
        return categoryMapper.toCategoryDto(adminService.updateCategory(category));
    }

    @PostMapping(value = "/admin/categories")
    public CategoryDto addCategory(@RequestBody @Validated(Create.class) NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.toCategory(newCategoryDto);
        return categoryMapper.toCategoryDto(adminService.addCategory(category));
    }

    @DeleteMapping(value = "/admin/categories/{catId}")
    public String deleteCategory(@PathVariable @NotNull @Positive Long catId) {
        adminService.deleteCategoryById(catId);
        return "Категория удалена";
    }
}
