package com.xyz.zf.service.impl;

import com.xyz.zf.exception.SellException;
import com.xyz.zf.service.RedisLock;
import com.xyz.zf.service.SecKillService;
import com.xyz.zf.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by liuyang
 * 2018/6/30
 */
@Service
@Slf4j
public class SecKillServiceImpl implements SecKillService {
    @Autowired
    private RedisLock redisLock;

    private static final int TIME_OUT = 10 * 1000;  //超时时间，10s
    /**
     * 国庆活动，皮蛋粥特价，限量100000份
     */
    private static Map<String, Integer> products;
    private static Map<String, Integer> stock;
    private static Map<String, String> orders;
    static {
        /**
         * 模拟多个表，商品信息表，库存表，秒杀成功订单表
         */
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("123456", 100000);
        stock.put("123456", 100000);
    }
    private String queryMap(String productId) {
        return "国庆活动，皮蛋粥特价，限量份"
                + products.get(productId)
                + " 还剩：" + stock.get(productId) + " 份"
                + " 该商品成功下单用户数目：" + orders.size() + " 人";
    }

    @Override
    public String querySecKillProductInfo(String productId) {
        return this.queryMap(productId);
    }

    @Override
    public void orderProductMockDiffUser(String productId) {
        //加锁
        long time = System.currentTimeMillis() + TIME_OUT;
        if (!redisLock.lock(productId, String.valueOf(time))) {
            throw new SellException(101, "哎呦喂，人也太多了，换个姿势再试试~~~");
        }
        //1.查询该商品库存，为0则结束活动
        int stockNum = stock.get(productId);
        if (stockNum == 0) {
            throw new SellException(100, "活动结束");
        }
        //2.下单，模拟不同用户openid不同
        orders.put(KeyUtil.genUniqueKey(), productId);
        //3.减库存
        stockNum = stockNum - 1;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stock.put(productId, stockNum);
        //解锁
        redisLock.unlock(productId, String.valueOf(time));
    }

}
