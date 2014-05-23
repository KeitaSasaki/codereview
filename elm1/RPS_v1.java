import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;


/**
   じゃんけんを行うプログラム
   入力は1~3の数字で行い、コンピューターはランダムに手を出す
*/
public class RPS_v1{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();
        final String TE[] = {"グー", "チョキ", "パー"};
        System.out.println("じゃんけんをしましょう！");
        System.out.println("1: グー、2: チョキ、3: パー です。");
        System.out.println("じゃーんけーん・・");
        System.out.printf("出す手を入力 => ");
        String input = "";
        try{
            input = sc.nextLine();
            //入力が1~3以外だったらやめる
            if(!input.equals("1") && !input.equals("2") && !input.equals("3")){
                System.out.println("1～3で入力してください！");
                return;
            }
        }
        catch(NoSuchElementException e){
            System.out.println("1～3で入力してください！");
            return;
        }
        System.out.println("ぽん！");
        int computer = rnd.nextInt(3); //コンピュータの手は0~2のランダム
        int you = Integer.parseInt(input) - 1;
        System.out.printf("あなた：%s、コンピュータ：%s\n", TE[you], TE[computer]);
        if(computer == you) System.out.println("あいこでした！");
        else if((computer - you + 3) % 3 == 1) System.out.println("あなたの勝ちです！"); //差分が1か-2なら勝ち
        else System.out.println("あなたの負けです！");
    }
}
