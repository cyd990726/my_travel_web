package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet(name = "UserServlet",value = "/user/*")
public class UserServlet extends BaseServlet{
    private UserService userService=new UserServiceImpl();
    /**
     * 从session中获取当前用户
     * @param req
     * @param resp
     * @throws IOException
     */
    public void findCurUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session=req.getSession();
        User user=(User) session.getAttribute("user");
        this.writeValue(resp,user);
    }

    /**
     * 注册
     * @param req
     * @param resp
     */
    public void regist(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //先进行验证码的校验
        String checkCode=req.getParameter("check");
        HttpSession session= req.getSession();
        String checkCode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //保证验证码只能使用一次，用完后就移除
        session.removeAttribute("CHECKCODE_SERVER");
        ResultInfo info=new ResultInfo();
        if(!checkCode.equalsIgnoreCase(checkCode_server)){
            //验证码错误
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info序列化为json
            ObjectMapper mapper=new ObjectMapper();
            String info_json = mapper.writeValueAsString(info);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write(info_json);
            return;
        }
        //获取参数封装成对象
        Map<String,String[]> ps=req.getParameterMap();
        User user=new User();
        try {
            BeanUtils.populate(user,ps);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用service中的注册服务
        boolean flag = userService.regist(user);
        if(flag){
            //注册成功
            info.setFlag(true);
        }else {
            info.setFlag(false);
            info.setErrorMsg("注册失败");
        }
        //将info序列化为json
        ObjectMapper mapper=new ObjectMapper();
        String info_json = mapper.writeValueAsString(info);
        this.writeValue(resp,info_json);

    }

    /**
     * 登录
     * @param request
     * @param response
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //先验证验证码是否正确
        String checkCode=request.getParameter("check");
        HttpSession session=request.getSession();
        String checkCode_server=(String) session.getAttribute("CHECKCODE_SERVER");
        ResultInfo resultInfo=new ResultInfo();
        if(!checkCode.equalsIgnoreCase(checkCode_server)){
            //验证码不对
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误");
            //将resultInfo写入响应体中
            this.writeValue(response,resultInfo);
            return;
        }
        //封装参数
        Map<String,String[]> ps=request.getParameterMap();
        User user=new User();
        try {
            BeanUtils.populate(user,ps);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用service登录
        User login_user = userService.login(user);
        if(login_user==null){
            //登录失败，用户名或者密码不正确
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户名或者密码不正确");
            this.writeValue(response,resultInfo);
            return;
        }
        if(!"Y".equalsIgnoreCase(login_user.getStatus())){
            //此账号未被激活，请联系管理员
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("此账号未被激活，请联系管理员");

            this.writeValue(response,resultInfo);
            return;
        }
        //登陆成功,将user存入session中
        session.setAttribute("user",login_user);
        resultInfo.setFlag(true);
        this.writeValue(response,resultInfo);
    }

    /**
     * 退出登录
     * @param req
     * @param resp
     */
    public void exit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //销毁session
        req.getSession().invalidate();
        //重定向回到主页
        resp.sendRedirect(req.getContextPath()+"/index.html");
    }

    public void active(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        //进行邮箱激活
        String activeCode=req.getParameter("code");
        if(activeCode!=null){
            boolean flag=userService.active(activeCode);
            if(flag){
                resp.getWriter().write("<a href='http://localhost:80/travel/user/active'>激活成功，请点击此处跳转登陆页面</a>");
            }else {
                resp.getWriter().write("激活失败，请联系管理员");
            }
        }
    }


}
