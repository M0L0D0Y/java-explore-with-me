package ru.practicum.mainservice.category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategories(int from, int size);

    Category getCategoryById(long id);

    void deleteCategoryById(long id);

    Category addCategory(Category category);

    Category updateCategory(Category category);
}
