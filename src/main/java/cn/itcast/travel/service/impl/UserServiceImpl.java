package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RoutePageBean;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public boolean regist(User user) {
        //在数据库查询是否有这个用户
        UserDao userDao=new UserDaoImpl();
        User find_user=userDao.findUserByUsername(user);
        if(find_user==null){
            //用户名没有被用过，可以注册
            //1. 设置激活码
            user.setCode(UuidUtil.getUuid());
            user.setStatus("N");
            userDao.addUser(user);
            String content="<a href='http://localhost:80/travel/ActiveUserServlet?code="+user.getCode()+"'>点击此处进行激活</a>";
            MailUtils.sendMail(user.getEmail(),content,"激活邮件");
            return true;

        }else{
            //用户名已经存在
            return false;
        }
    }

    @Override
    public boolean active(String activeCode) {
        UserDao userDao=new UserDaoImpl();
        User user=userDao.findByActiveCode(activeCode);
        if(user!=null){
            //更新状态
            userDao.updateStatus(user);
            return true;
        }
        //else用户不存在，激活失败
        return false;
    }

    @Override
    public User login(User user) {
        UserDao userDao=new UserDaoImpl();
        User find_user=userDao.findUserByUsernameAndPAssword(user);
        return find_user;
    }

    @Override
    public RoutePageBean<Route> findCurPageRoutes(Integer cid,Integer curPage,Integer pageSize) {
        UserDao userDao=new UserDaoImpl();
        List<Route> ls=userDao.findCurPageRoutes(cid,curPage,pageSize);
        Integer totalCount=userDao.findRouteNum(cid);
        //封装成RoutePageBean对象
        RoutePageBean<Route> pb=new RoutePageBean<>();
        //设置属性
        pb.setCurrentPage(curPage);
        pb.setPageSize(pageSize);
        pb.setTotalCount(totalCount);
        pb.setTotalPage((int) Math.ceil((double)totalCount/pageSize));
        pb.setDataList(ls);
        //返回对象
        return pb;
    }


}
