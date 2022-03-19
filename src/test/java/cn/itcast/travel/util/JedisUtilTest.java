package cn.itcast.travel.util;

import junit.framework.TestCase;
import redis.clients.jedis.Jedis;

public class JedisUtilTest extends TestCase {

    public void testGetJedis() {
        Jedis jedis = JedisUtil.getJedis();
        jedis.auth("cyd19990726");
        System.out.println(jedis.get("name"));
    }
}