package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.security.cert.Extension;
import java.util.List;

public class UserDaoImpl implements UserDao {
    //这里是设置templet为成员变量而不是静态变量是为了能让
    // 这个类没创建一个实例都能有一个特有的template，要是静态
    // 的话当大量用户访问的时候一个数据库连接池的connection肯定不够用
    private JdbcTemplate template=new JdbcTemplate(JDBCUtils.getDataSource());

    //用户查询
    @Override
    public User findUserByUsername(User user) {
        String sql="select * from tab_user where username=?";
        try {
            User find_user=template.queryForObject(sql,
                    new BeanPropertyRowMapper<User>(User.class),
                    user.getUsername());
            return find_user;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addUser(User user) {
        String sql="insert into tab_user(username,password,name,birthday,sex,telephone,email,code,status) values(?,?,?,?,?,?,?,?,?)";
        int res=template.update(sql,user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getCode(),
                user.getStatus());
        return res>0;
    }

    @Override
    public User findByActiveCode(String activeCode) {
        String sql="select * from tab_user where code=?";
        User user=null;
        try {
            user= template.queryForObject(sql,new BeanPropertyRowMapper<>(User.class),activeCode);
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateStatus(User user) {
        String sql="update tab_user set status='Y' where uid=?";
        template.update(sql,user.getUid());
    }

    @Override
    public User findUserByUsernameAndPAssword(User user) {
        //这里进行查询
        String sql="select * from tab_user where username=? and password=?";
        try {
            User find_user=template.queryForObject(sql,new BeanPropertyRowMapper<>(User.class),user.getUsername(),user.getPassword());
            return find_user;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<Route> findCurPageRoutes(Integer cid, Integer curPage, Integer pageSize) {
        String sql="select * from tab_route where cid=? limit ?,?";
        List<Route> ls=template.query(sql,new BeanPropertyRowMapper<>(Route.class),cid,(curPage-1)*pageSize,pageSize);
        return ls;
    }

    @Override
    public Integer findRouteNum(Integer cid) {
        String sql="select count(*) from tab_route where cid=?";
        Integer num = template.queryForObject(sql,Integer.class,cid); ;
        return num;
    }


    //添加用户

}
