package yang;


import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.awt.*;

public class RedisDemo {

    static Jedis redis = new JedisPool("127.0.0.1", 6379).getResource();

    final static List<Integer> mysql = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    private static BloomFilter<Integer> bf = BloomFilter.create(
            Funnels.integerFunnel(),
            10000,
            0.001);

    static {

        for (int i = 0; i < mysql.size(); i++) {
            bf.put(i);
        }
    }

    public static void main(String[] args) {

        Integer id = 1;
        //查缓存
        String yang = redis.get(id.toString());

        if (!StringUtils.isBlank(yang)) {

            System.out.println(yang);
            return;
        }
        //穿过缓存
        boolean b = bf.mightContain(id);
        if (b) {
            if (mysql.contains(id)) {
                System.out.println("在数据库 ID ：" + id);
                redis.set(id.toString(), "xxx");
                redis.expire(id.toString(), 10);
                return;


            }

        }
        return;



    }

}