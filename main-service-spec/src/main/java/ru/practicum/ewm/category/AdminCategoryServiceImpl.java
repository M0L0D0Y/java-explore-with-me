package ru.practicum.ewm.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.CommonMethods;

@Slf4j
@Service
@Transactional
public class AdminCategoryServiceImpl implements AdminService {
    private final CategoryStorage categoryStorage;
    private final CommonMethods commonMethods;

    @Autowired
    public AdminCategoryServiceImpl(CategoryStorage categoryStorage, CommonMethods commonMethods) {
        this.categoryStorage = categoryStorage;
        this.commonMethods = commonMethods;
    }

    @Override
    @Transactional
    public void deleteCategoryById(long id) {
        commonMethods.checkExistCategory(id);
        log.info("Категория с id {} удалена", id);
        categoryStorage.deleteById(id);
    }

    @Override
    @Transactional
    public Category addCategory(Category category) {
        Category newCategory = categoryStorage.save(category);
        log.info("Категория с именем {} сохранена", category.getName());
        return newCategory;
    }

    @Override
    @Transactional
    public Category updateCategory(Category category) {
        Category updateCategory = commonMethods.checkExistCategory(category.getId());
        updateCategory.setName(category.getName());
        Category updatedCategory = categoryStorage.save(updateCategory);
        log.info("Категория с id {} обновлена", category.getId());
        return updatedCategory;
    }
}
