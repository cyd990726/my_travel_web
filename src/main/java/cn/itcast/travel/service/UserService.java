package cn.itcast.travel.service;

import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RoutePageBean;
import cn.itcast.travel.domain.User;

public interface UserService {
    boolean regist(User user);
    boolean active(String activeCode);

    User login(User user);

    RoutePageBean<Route> findCurPageRoutes(Integer cid,Integer curPage,Integer pageSize);
}
