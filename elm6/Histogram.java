public class Histogram{
    public static void main(String[] args){
        int n = args.length;
        if(n == 0){
            System.out.println("グラフにプロットする値を引数に指定してください。\n 例）java Histogram 1 2 3 3 2 1");
            return;
        }
        int inp[] = new int[args.length];
        for(int i = 0; i < n; i++){
            try{
                inp[i] = Integer.parseInt(args[i]);
            }
            catch(Exception e){
                System.out.println("引数に指定できるのは数値のみです。");
                return;
            }
        }
        int mmax = -1;
        for(int e: inp) mmax = Math.max(mmax, e);
        String output[] = new String[mmax];
        for(int i = 0; i < mmax; i++){
            output[i] = "";
        }
        for(int e: inp){
            for(int i = 0; i < mmax; i++){
                if(i < mmax - e) output[i] += ' ';
                else output[i] += '*';
            }
        }
        for(String s: output) System.out.println(s);
    }
}
