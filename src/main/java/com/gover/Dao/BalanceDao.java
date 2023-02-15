package com.gover.Dao;

import com.gover.Pojo.balancePojo;
import com.gover.Pojo.goodsPojo;
import com.gover.Pojo.userPojo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/queryUserBalance/{id}")
    public String queryUserBalance(@PathVariable("id") String id){
        if (id == null || "".equals(id)){
            return "传入ID有误";
        }
        // 先基于缓存查询该用户的余额
           UserPojo balance = redisTempalte.get(id);
            if(balance.getAllBalance() == null) {
                // 缓存中查询不到该用户余额，从数据库中查询 select user_id, user_AllBalance from ec_userInfo where user_id = {id};
                 userPojo UserBalance = userMapper.list(id)
                if(UserBalance != null){
                    // 更新到缓存中
                    redisTemplate.set(UserBalance);
                }
            }
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
             // 创建订单Insert into ec_order values (xxx,xxx,xxx,"待支付")
             return "余额不足，请及时充值";
         }
        // 用户钱包余额充足
        boolean isUpdate = update ec_userInfo set user_allBalance = userBalance - goodsBalance;
         if (isUpdate){
             // 修改订单状态，变为 "已支付" update ec_order set order_type = '已支付' where user_id = user.getId();
             // 重新更新用户信息到缓存和前端
             // 添加一条消费明细: insert into ec_balanceInfo values (xxx,xxx,xxx,orderInfo,2,dateTime)
             queryUserBalance(user.getUserId());
         }
         return "支付成功";
    }

    /**
     * 用户退款
     * @param user
     * @return
     */
    @PostMapping("/userRefund")
    public String userRefund(userPojo user,double Price){
        if (user == null){
            return "信息有误";
        }
        double goodsPrice = goods.getPrice();
        // 查询出钱包余额
        userPojo userInfo = select user_id,user_name,user_allBalance from ec_userInfo where userId = user.getUserId();
        // 更新钱包余额
        boolean isRefund = update ec_userInfo  set user_allBalance = userInfo.getUserAllBalance() + Price;
        // 退款成功后
        if (isRefund){
            // 添加退款明细
            insert into ec_balanceInfo values (xxx,xxx,xxx,orderInfo,3,dateTime);
            // 再重新查询一次用户钱包信息和余额明细
            // 更新到缓存中
             redisTemplate.opsforValue.set("user",userInfo);
             redisTempalte.opsforHash.set("user:balance"+userInfo.getUserId(),balanceInfoMap);
        }
        return "退款成功";
    }

    /**
     * 查询钱包余额明细
     * @param user
     * @return
     */
    @GetMapping("/queryBalanceInfo")
    public List queryBalanceInfo(userPojo user){
        List<balancePojo> balanceList =
                select balance_id,balance_bail,balance_type,balance_createTime
                from ec_balanceInfo as bi
                    left join ec_balanceType as bt as in bi.balance_typeId bt.balance_typeId
                where user_id = {user.getUserId()}
                order by balance_createTime desc
                limit (pageNumber - 1) * pageSize, pageSize;
        return balanceList;

    }

}
