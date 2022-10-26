package ru.practicum.ewm.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.CommonMethods;
import ru.practicum.ewm.user.Page;

import java.util.List;

@Slf4j
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryStorage categoryStorage;
    private final CommonMethods commonMethods;

    @Autowired
    public CategoryServiceImpl(CategoryStorage categoryStorage, CommonMethods commonMethods) {
        this.categoryStorage = categoryStorage;
        this.commonMethods = commonMethods;
    }

    @Override
    public List<Category> getCategories(int from, int size) {
        Pageable pageable = Page.getPageable(from, size);
        List<Category> categories = categoryStorage.findAllCategories(pageable);
        log.info("Все Категории найдены");
        return categories;
    }

    @Override
    public Category getCategoryById(long id) {
        Category category = commonMethods.checkExistCategory(id);
        log.info("Категория с id = {} найдена", id);
        return category;
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
