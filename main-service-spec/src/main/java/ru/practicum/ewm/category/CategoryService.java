package ru.practicum.ewm.category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategories(int from, int size);

    Category getCategoryById(long id);

}
