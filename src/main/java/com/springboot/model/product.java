package com.springboot.model;

/**
 * Created by wxl on 2019/9/5.
 */
public class product {
    String productName; //项目名称
    String productAmount; //已众筹金额
    String pct_funded; //完成率
    String countributions; //支持人数
    String days_left; //剩余天数
    String categoryName;//类别名称

    @Override
    public String toString() {
        return "product{" +
                "productName='" + productName + '\'' +
                ", productAmount='" + productAmount + '\'' +
                ", pct_funded='" + pct_funded + '\'' +
                ", countributions='" + countributions + '\'' +
                ", days_left='" + days_left + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }

    public String getPct_funded() {
        return pct_funded;
    }

    public void setPct_funded(String pct_funded) {
        this.pct_funded = pct_funded;
    }

    public String getCountributions() {
        return countributions;
    }

    public void setCountributions(String countributions) {
        this.countributions = countributions;
    }

    public String getDays_left() {
        return days_left;
    }

    public void setDays_left(String days_left) {
        this.days_left = days_left;
    }
}
