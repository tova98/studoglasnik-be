package hr.tvz.ntovernic.studoglasnik.controller;

import hr.tvz.ntovernic.studoglasnik.dto.CategoryDto;
import hr.tvz.ntovernic.studoglasnik.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public List<CategoryDto> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryDto getCategoryById(@PathVariable final Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void createCategory(@RequestBody final CategoryDto categoryDto) {
        categoryService.createCategory(categoryDto);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCategory(@PathVariable final Long categoryId, @RequestBody final CategoryDto categoryDto) {
        categoryService.updateCategory(categoryId, categoryDto);
    }


    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(@PathVariable final Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
