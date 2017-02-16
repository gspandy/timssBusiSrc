package test.com.timss.common;

import java.math.BigDecimal;

public class CommonJava {

    /**
     * @description:
     * @author: 王中华
     * @createDate: 2015-9-10
     * @param args:
     */
    public static void main(String[] args) {
        BigDecimal imrsItemNum = new BigDecimal( 0 ); // 物资编码
        BigDecimal imrsItemNum2 = new BigDecimal( 20 ); // 物资编码
        imrsItemNum = imrsItemNum.add( imrsItemNum2 );
        System.out.println( imrsItemNum );
    }

}
