package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;

import java.util.List;

public interface UserDao {
    public User findUserByUsername(User user);
    public boolean addUser(User user);

    User findByActiveCode(String activeCode);

    void updateStatus(User user);

    User findUserByUsernameAndPAssword(User user);

    List<Route> findCurPageRoutes(Integer cid, Integer curPage, Integer pageSize);

    Integer findRouteNum(Integer cid);
}
