package com.xyz.zf.controller;

import com.xyz.zf.dto.OrderMasterDto;
import com.xyz.zf.enums.ResultEnum;
import com.xyz.zf.exception.SellException;
import com.xyz.zf.service.OrderMasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 卖家端订单
 * Create by liuyang
 * 2018/6/17
 */
@Controller
@RequestMapping("/seller/order")
@Slf4j
public class SellerOrderMasterController {
    @Autowired
    private OrderMasterService orderMasterService;

    /**
     * 订单列表
     * @param page 第几页,从第一页开始
     * @param size 一页有多少条数据
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                             Map<String, Object> map) {
        PageRequest pageRequest = new PageRequest(page - 1, size);
        Page<OrderMasterDto> orderMasterDtoPage = orderMasterService.findList(pageRequest);
        map.put("orderMasterDtoPage", orderMasterDtoPage);
        map.put("currentPage", page);
        map.put("size", size);
        return new ModelAndView("order/list", map);
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @GetMapping("/cancel")
    public ModelAndView cancel(@RequestParam("orderId") String orderId, Map<String, Object> map) {
        try {
            OrderMasterDto orderMasterDto = orderMasterService.findOne(orderId);
            orderMasterService.cancel(orderMasterDto);
        } catch (SellException e) {
            log.error("【卖家端取消订单】 发生异常{}", e);
            map.put("url", "/sell/seller/order/list");
            map.put("msg", e.getMessage());
            return new ModelAndView("common/error", map);
        }
        map.put("url", "/sell/seller/order/list");
        map.put("msg", ResultEnum.ORDER_CANCEL_SUCCSS.getMsg());
        return new ModelAndView("common/success", map);
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/detail")
    public ModelAndView detail(@RequestParam("orderId") String orderId, Map<String, Object> map) {
        OrderMasterDto orderMasterDto = new OrderMasterDto();
        try {
            orderMasterDto = orderMasterService.findOne(orderId);
        } catch (SellException e) {
            log.error("【卖家端查询订单详情】 发生异常{}", e);
            map.put("url", "/sell/seller/order/list");
            map.put("msg", e.getMessage());
            return new ModelAndView("common/error", map);
        }
        map.put("orderMasterDto", orderMasterDto);
        return new ModelAndView("order/detail", map);
    }

    /**
     * 完结订单
     * @param orderId
     * @param map
     * @return
     */
    @GetMapping("/finish")
    public ModelAndView finish(@RequestParam("orderId") String orderId, Map<String, Object> map) {
        try {
            OrderMasterDto orderMasterDto = orderMasterService.findOne(orderId);
            orderMasterService.finish(orderMasterDto);
        } catch (SellException e) {
            log.error("【卖家端完结订单】 发生异常{}", e);
            map.put("url", "/sell/seller/order/list");
            map.put("msg", e.getMessage());
            return new ModelAndView("common/error", map);
        }
        map.put("url", "/sell/seller/order/list");
        map.put("msg", ResultEnum.ORDER_FINISH_SUCCESS.getMsg());
        return new ModelAndView("common/success", map);
    }
}
