package vn.iotstar.controller.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import vn.iotstar.entity.CategoryEntity;
import vn.iotstar.model.CategoryModel;
import vn.iotstar.service.ICategoryService;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("add")
    public String addCategory(ModelMap model) {
        CategoryModel category = new CategoryModel();
        category.setIsEdit(false); // Đánh dấu đây là thao tác thêm mới
        model.addAttribute("category", category);
        return "admin/categories/addOrEdit"; // Trả về template AddOrEdit.html
    }


    @PostMapping("saveOrUpdate")
    public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryModel cateModel, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("admin/categories/addOrEdit");
        }
        CategoryEntity entity = new CategoryEntity();
        BeanUtils.copyProperties(cateModel, entity);
        categoryService.save(entity);

        String message = cateModel.getIsEdit() ? "Category is edited!" : "Category is saved!";
        model.addAttribute("message", message);
        return new ModelAndView("forward:/admin/categories/searchpaginated", model);
    }

    @RequestMapping("")
    public String list(ModelMap model) {
        List<CategoryEntity> list = categoryService.findAll();
        model.addAttribute("categories", list);
        return "admin/categories/list";
    }

    @GetMapping("edit/{categoryId}")
    public ModelAndView edit(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);
        if (optCategory.isPresent()) {
            CategoryModel cateModel = new CategoryModel();
            BeanUtils.copyProperties(optCategory.get(), cateModel);
            cateModel.setIsEdit(true);
            model.addAttribute("category", cateModel);
            return new ModelAndView("admin/categories/addOrEdit", model);
        }
        model.addAttribute("message", "Category does not exist!");
        return new ModelAndView("forward:/admin/categories", model);
    }

    @GetMapping("delete/{categoryId}")
    public ModelAndView delete(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        categoryService.deleteById(categoryId);
        model.addAttribute("message", "Category is deleted!");
        return new ModelAndView("forward:/admin/categories/searchpaginated", model);
    }

    @GetMapping("search")
    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
        List<CategoryEntity> list;
        if (StringUtils.hasText(name)) {
            list = categoryService.findByNameContaining(name);
            model.addAttribute("name", name);
        } else {
            list = categoryService.findAll();
        }
        model.addAttribute("categories", list);
        return "admin/categories/search";
    }

    @RequestMapping("searchpaginated")
    public String searchPaginated(ModelMap model,
                                  @RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "page", defaultValue = "1") int page,
                                  @RequestParam(name = "size", defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name"));
        Page<CategoryEntity> resultPage;

        if (StringUtils.hasText(name)) {
            resultPage = categoryService.findByNameContaining(name, pageable);
            model.addAttribute("name", name);
        } else {
            resultPage = categoryService.findAll(pageable);
        }

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, page - 2);
            int end = Math.min(page + 2, totalPages);
            if (totalPages > 5) {
                if (end == totalPages) start = end - 4;
                else if (start == 1) end = start + 4;
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("categoryPage", resultPage);
        return "admin/categories/searchpaginated";
    }
}
