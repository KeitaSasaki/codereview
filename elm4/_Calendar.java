import java.util.Scanner;
import java.util.Calendar;

public class _Calendar{
    static Scanner sc = new Scanner(System.in);
    static int days[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    static Calendar cal;
    /**
       year年が閏年かを判定する
       閏年ならtrue
     */
    static boolean isLeap(int year){
        return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
    }

    public static void main(String[] args){
        int year, month;
        System.out.println("カレンダーを出力します");
        System.out.println("年を入力してください（例：2014）");
        try{
            year = sc.nextInt();
        } catch(Exception e){
            System.out.println("数値の形式が正しくありません。");
            return;
        }
        if(year < 1){
            System.out.println("年は１以上の自然数で入力してください。");
            return;
        }
        System.out.println("月を入力してください（例：4）");
        try{
            month = sc.nextInt();
        } catch(Exception e){
            System.out.println("数値の形式が正しくありません。");
            return;
        }
        if(month < 1 || 12 < month){
            System.out.println("月は１～１２の間で入力してください。");
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, 1);
        int day = cal.get(Calendar.DAY_OF_WEEK) - 1;
        System.out.println("日 月 火 水 木 金 土");
        int end = days[month-1] + (month==2&&isLeap(year)? 1:0);
        int p = 1, c = 0;
        while(p <= end){
            if(day <= c){
                System.out.printf("%2d", p);
                p++;
            } else{
                System.out.print("  ");
            }
            c++;
            System.out.printf(c % 7 == 0? "\n" : " ");
        }
        System.out.printf("\n");
    }
}
