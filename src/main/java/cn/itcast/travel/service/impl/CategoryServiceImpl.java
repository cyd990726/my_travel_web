package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.*;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao=new CategoryDaoImpl();
    @Override
    public List<Category> findAllCategories() {
        //首先应该先去查找redis
        Jedis jedis = JedisUtil.getJedis();
        Set<Tuple> ls= jedis.zrangeWithScores("category",0,-1);
        List<Category> res=null;
        System.out.println("res"+ls);
        if(ls.isEmpty()){
            //说明redis中没有，从数据库中查
            System.out.println("从数据库查询...");
            res = categoryDao.findAll();
            //存入数据库
            for (Category c:
                 res) {
                jedis.zadd("category",c.getCid(),c.getCname());
            }
        }else {
            //说明有，将其返回
            System.out.println("从redis查询");
            res=new ArrayList<Category>();
            Iterator<Tuple> it=ls.iterator();
            while (it.hasNext()){
                Category item=new Category();
                Tuple tuple=it.next();
                item.setCname(tuple.getElement());
                item.setCid((int) tuple.getScore());
                res.add(item);
            }
        }
        return res;
    }
}
