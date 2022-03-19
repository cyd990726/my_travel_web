package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RoutePageBean;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "RouteServlet",value = "/route/*")
public class RouteServlet extends BaseServlet{
    private UserService userService=new UserServiceImpl();
    //查询表route_list的信息
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取参数
        Integer cid=Integer.parseInt(request.getParameter("cid")) ;
        Integer curPage=request.getParameter("curPage")==null?1:Integer.parseInt(request.getParameter("curPage"));
        Integer pageSize=request.getParameter("pageSize")==null?10:Integer.parseInt(request.getParameter("pageSize"));
        //当前页面默认值为1，页面尺寸默认值为10
        curPage=curPage==null?1:curPage;
        pageSize=pageSize==null?10:pageSize;
        System.out.println("curpage:"+curPage);
        System.out.println("pageSize:"+pageSize);
        //调用服务查询
        RoutePageBean<Route> pb=userService.findCurPageRoutes(cid,curPage,pageSize);
        String str=this.writeValueAsString(pb);
        System.out.println(str);
        this.writeValue(response,pb);

    }
}
