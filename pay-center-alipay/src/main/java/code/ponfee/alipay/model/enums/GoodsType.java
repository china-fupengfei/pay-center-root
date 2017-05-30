package code.ponfee.alipay.model.enums;

/**
 * 商品类型
 */
public enum GoodsType {

    /**
     * 虚拟物品
     */
    VIRTUAL("0"),

    /**
     * 实物
     */
    REAL("1");

    private String value;

    private GoodsType(String value){
        this.value = value;
    }

    public String value(){
        return value;
    }
}
