package com.xyz.zf.pojo;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Create by liuyang
 * 2018/6/10
 */
@Entity
@Data
@DynamicUpdate
public class OrderDetail {
    /**
     * 订单详情编号
     */
    @Id
    private String detailId;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 商品id
     */
    private String productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品价格
     */
    private BigDecimal productPrice;
    /**
     * 商品数量
     */
    private Integer productQuantity;
    /**
     * 商品图片
     */
    private String productIcon;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
