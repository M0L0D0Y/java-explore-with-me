package ru.practicum.ewm.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;

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
        List<CategoryDto> categoryDtos = categoryService.getCategories(from, size)
                .stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
        log.info("{}", categoryDtos);
        return categoryDtos;

    }

    @GetMapping(value = "/categories/{catId}")
    public CategoryDto getCategory(@PathVariable @Positive Long catId) {
        CategoryDto categoryDto = categoryMapper.toCategoryDto(categoryService.getCategoryById(catId));
        log.info("{}", categoryDto);
        return categoryDto;

    }


}
