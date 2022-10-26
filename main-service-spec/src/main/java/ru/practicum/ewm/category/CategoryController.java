package ru.practicum.ewm.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.common.Update;
import ru.practicum.ewm.common.Create;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Slf4j
@Validated
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping(value = "/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "10") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return categoryService.getCategories(from, size)
                .stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());

    }

    @GetMapping(value = "/categories/{catId}")
    public CategoryDto getCategory(@PathVariable @Positive Long catId) {
        return categoryMapper.toCategoryDto(categoryService.getCategoryById(catId));

    }

    @PatchMapping(value = "/admin/categories")
    public CategoryDto updateCategory(@RequestBody @Validated(Update.class) CategoryDto categoryDto) {
        Category category = categoryMapper.toCategoryFromCategoryDto(categoryDto);
        return categoryMapper.toCategoryDto(categoryService.updateCategory(category));
    }

    @PostMapping(value = "/admin/categories")
    public CategoryDto addCategory(@RequestBody @Validated(Create.class) NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.toCategory(newCategoryDto);
        return categoryMapper.toCategoryDto(categoryService.addCategory(category));
    }

    @DeleteMapping(value = "/admin/categories/{catId}")
    public String deleteCategory(@PathVariable @NotNull @Positive Long catId) {
        categoryService.deleteCategoryById(catId);
        return "Категория удалена";
    }

}
