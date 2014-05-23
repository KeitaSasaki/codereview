package intermediate;

public class Intermediate1 {
    /**
     引数の整数を二進文字列に変換
     */
	public static String toBinaryString(int src){
		String ans = "";
		if(src < 0){
			throw new IllegalArgumentException();
		} else if(src == 0) ans = "0";
		while(0 < src){
			int digit = src % 2;
			src /= 2;
			ans += digit;
		}
        //得られた文字列をひっくり返して返す
		return new StringBuilder(ans).reverse().toString();
	}
	public static void main(String[] args) {
		System.out.println(toBinaryString(3));
	}
}
