package ru.practicum.ewm.category;

public interface AdminService {
    void deleteCategoryById(long id);

    Category addCategory(Category category);

    Category updateCategory(Category category);
}
