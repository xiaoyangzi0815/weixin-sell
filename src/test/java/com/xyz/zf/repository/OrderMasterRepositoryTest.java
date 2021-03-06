package com.xyz.zf.repository;

import com.xyz.zf.pojo.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * Create by liuyang
 * 2018/6/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    private final String OPENID = "110110";

    @Test
    public void saveTest() {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("1234567");
        orderMaster.setBuyerName("xiaoyangzi");
        orderMaster.setBuyerPhone("187123658988");
        orderMaster.setBuyerAddress("上海市黄浦区");
        orderMaster.setBuyerOpenid(OPENID);
        orderMaster.setOrderAmount(new BigDecimal(2.5));
        OrderMaster result = orderMasterRepository.save(orderMaster);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByBuyerOpenid() {
        PageRequest request = new PageRequest(0, 3);
        Page<OrderMaster> result = orderMasterRepository.findByBuyerOpenid(OPENID, request);
        Assert.assertNotEquals(result.getContent().size(), 0);
        System.out.println(result.getTotalElements());
        System.out.println(result.getContent().get(0).toString());
    }
}