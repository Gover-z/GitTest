package com.gover.Dao;

import com.gover.Pojo.goodsPojo;
import com.gover.Pojo.userPojo;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gover
 * @verson 1.0
 */
@RestController
@RequestMapping("/balance")
public class BalanceDao {



    /**
     * 基于用户id查询余额
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public String queryUserBalance(@PathVariable("id") String id){
        if (id == null || "".equals(id)){
            return "传入ID有误";
        }
        // 先基于缓存查询该用户的余额
       /*
           UserPojo balance = redisTempalte.get(id);
            if(balance.getAllBalance() == null) {
                // 缓存中查询不到该用户余额，从数据库中查询 select user_id, user_AllBalance from ec_userInfo where user_id = {id};
                 userPojo UserBalance = userMapper.list(id)
                if(UserBalance != null){
                    // 更新到缓存中
                    redisTemplate.set(UserBalance);
                }
            }
        */
        // 查询后，并存入Redis中
        return "返回用户钱包余额:userPojo.getAllBalance()";
    }

    /**
     * 用户支付接口
     * @param user 用户信息
     * @return
     */
    @PostMapping("/userPay")
    public String UserPay(userPojo user,goodsPojo goods){
        if (user == null){
            return "用户信息有误";
        }
        // 这里还需要先判断商品库存
        Order oderInfo = redisTemplate.get(orderId);
        if(orderInfo.getGoodsCount < 0 ){
            return "商品库存不足";
        }
        // 再进行消费，肯定会涉及到用户钱包余额和商品价格的比较，消费的流程:
        // 1. 先从缓存中查询用户的钱包余额，如果钱包余额不够，就先创建一个定时订单，订单状态设置为”待支付状态“
        UserPojo userInfo = redisTempalate.get(user.getid());
        double userBalance = userInfo.getALlBalance();
        double goodsBalance = goodsInfo.getGoodsPrice();
         if ( userBalance < goodsBalance){
             // 创建订单Insert into Order values (xxx,xxx,xxx,"待支付")
             return "余额不足，请及时充值";
         }
        // 用户钱包余额充足
        boolean isUpdate = update ec_userInfo set user_allBalance = userBalance - goodsBalance;
         if (isUpdate){
             // 修改订单状态，变为 "已支付" update ec_order set order_type = '已支付' where user_id = user.getId();
             // 重新更新用户信息到缓存和前端
             // 添加一条消费明细: insert into ec_balanceInfo set (xxx,xxx,xxx,orderInfo,2,dateTime)
             queryUserBalance(user.getUserId());
         }
         return "支付成功";
    }

    /**
     * 用户退款
     * @param user
     * @return
     */
    @PostMapping("/refund")
    public String userRefund(userPojo user,){
        if (user == null){
            return "用户不存在";
        }
    }

}
