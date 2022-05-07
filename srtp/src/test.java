/**
 * @Author: Feng Jixuan
 * @Date: 2022-05-2022-05-01
 * @Description: srtp
 * @version=1.0
 */
public class test {
    public static void main(String[] args) {
        int k=0;
        long startTime =System.currentTimeMillis();
        for (int i=1;i<=100000000;i++) k=1;
        long endTime=System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
    }
}
