package cn.itcast.travel.web.servlet;


import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryServlet", value = "/CategoryServlet/*")
public class CategoryServlet extends BaseServlet{
    private CategoryService categoryService=new CategoryServiceImpl();
    //查询分类数据
    public void findCategories(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Category> ls=categoryService.findAllCategories();
        this.writeValue(response,ls);
    }
}
