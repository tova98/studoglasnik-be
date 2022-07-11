package hr.tvz.ntovernic.studoglasnik.service;

import hr.tvz.ntovernic.studoglasnik.dto.CategoryDto;
import hr.tvz.ntovernic.studoglasnik.model.Category;
import hr.tvz.ntovernic.studoglasnik.repository.CategoryRepository;
import hr.tvz.ntovernic.studoglasnik.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getCategories() {
        final List<Category> categoryList = categoryRepository.findAll();

        return MapperUtils.mapAll(categoryList, CategoryDto.class);
    }

    public CategoryDto getCategoryById(final Long categoryId) {
        final Category category = getCategory(categoryId);

        return MapperUtils.map(category, CategoryDto.class);
    }

    public void createCategory(final CategoryDto categoryDto) {
        final Category category = MapperUtils.map(categoryDto, Category.class);
        categoryRepository.save(category);
    }

    public void updateCategory(final Long categoryId, final CategoryDto categoryDto) {
        final Category category = getCategory(categoryId);

        BeanUtils.copyProperties(categoryDto, category, "id");
        categoryRepository.save(category);
    }

    public void deleteCategory(final Long categoryId) {
        final Category category = getCategory(categoryId);
        categoryRepository.delete(category);
    }

    private Category getCategory(final Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id %d not found!", categoryId)));
    }
}
