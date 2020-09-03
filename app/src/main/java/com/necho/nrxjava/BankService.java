package com.necho.nrxjava;

import com.necho.nrxjava.annotation.BankTransferMoney;

import java.lang.reflect.Method;

/**
 * 转账处理业务类
 */
public class BankService {

    /**
     * 专账
     *
     * @param money
     */
    @BankTransferMoney(maxMoney = 6000)
    public String transferMoney(double money) {
        String transferResult = processAnnotationMoney(money);
        return transferResult;
    }

    private String processAnnotationMoney(double money) {
        try {
            Method method = BankService.class.getDeclaredMethod("transferMoney", double.class);//获取注解方法
            boolean annotationPresent = method.isAnnotationPresent(BankTransferMoney.class);//方法是否被注解标注
            if (annotationPresent) {
                BankTransferMoney transferMoney = method.getAnnotation(BankTransferMoney.class);//获取注解
                if (transferMoney != null) {
                    double maxMoney = transferMoney.maxMoney();//获取注解属性
                    if (money > maxMoney) {
                        return "转账金额" + money + "大于限额" + maxMoney + "，转账失败";
                    } else {
                        return "转账金额为:" + money + "，转账成功";
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return "转账处理失败";
    }

}
