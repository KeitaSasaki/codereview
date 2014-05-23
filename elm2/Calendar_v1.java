public class Calendar_v1{
    public static void main(String[] args){
        System.out.println("2014年4月のカレンダーを出力します");
        System.out.println("2014年 4月");
        System.out.println("日 月 火 水 木 金 土");
        System.out.print("     ");
        for(int i = 1; i <= 30; i++){
            System.out.printf(i % 7 == 6 ? "\n" : " ");
            System.out.printf("%2d", i);
        }
        System.out.printf("\n");
    }
}
