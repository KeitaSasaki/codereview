package intermediate;

public class Intermediate2 {
	/**
	 * intの配列srcから連続する重複を除去した配列を返す
	 * */
	static int[] uniq(int[] src) {
		if(src == null) throw new NullPointerException();
		int n = src.length;
		if (n == 0) return src;

		// まず返す配列の要素数が分からないので、返す配列のサイズを調べる
		int prev = src[0]; // 1つ前の要素を格納
		int size = 1;
		for (int i = 1; i < n; i++) {
			if (prev != src[i]) {
				size++;
				prev = src[i];
			}
		}

		int[] res = new int[size];
		int pos = 0;
		prev = src[0];
		res[pos++] = src[0];
		// 実際に返す配列を作る
		for (int i = 1; i < n; i++) {
			if (prev != src[i]) {
				res[pos++] = src[i];
				prev = src[i];
			}
		}
		
		return res;
	}
}
