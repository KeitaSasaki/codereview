import java.util.Scanner;
import java.io.PrintWriter;
import java.util.Vector;

public class Eratosthenes{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        PrintWriter pw = new PrintWriter(System.out);

        System.out.println("素数リストを出力するプログラムです。\nリストの最大値を整数で入力してください。");
        int n = sc.nextInt();

        Vector<Integer> primes = new Vector<Integer>();
        boolean notPrime[] = new boolean[n+1];
        notPrime[0] = notPrime[1] = true;
        int sqrtN = (int)Math.sqrt(n);
        int i;

        //sqrt(N)まで調べれば十分
        for(i = 2; i <= sqrtN; i++){
            if(!notPrime[i]){
                primes.add(i);
                for(int j = i; j <= n; j += i){
                    notPrime[j] = true;
                }
            }
        }

        for(; i <= n; i++){
            if(!notPrime[i]){
                primes.add(i);
            }
        }

        //出力
        for(int j = 0; j < primes.size(); j++){
            pw.printf("%d ", primes.get(j));
        }
        pw.flush();
    }
}
