package calc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

import calc.Token.Type;

public class Calculator {

	/**
	 * 数式の文字列を逆ポーランド記法(Reverse Polish Notation)に従ったトークン列に変換
	 * @param expr 変換元の文字列
	 * @return exprを元に逆ポーランド記法で並べたトークンのリスト
	 * @throws CalcFormatException 
	 * */
	
	/** 各演算子よりも高い優先順位を持つ演算子の集合（自身含む）*/
	private static final List<Type> precedence_over_factops = Arrays.asList(Type.FACTOP); 
	private static final List<Type> precedence_over_termops = Arrays.asList(Type.FACTOP, Type.TERMOP); 

	public static List<Token> convertToRPN(String expr) throws CalcFormatException {
		Deque<Token> operators = new ArrayDeque<Token>();
		List<Token> buf = new ArrayList<Token>();
		
		//数式を分解
		StringTokenizer st = new StringTokenizer(expr, "\\+-\\*/()", true);

		//文字列をトークンに変換しながら逆ポーランド記法のアルゴリズムに従って変換
		//アルゴリズム：http://www.gg.e-mansion.com/~kkatoh/program/novel2/novel208.html
		while(st.hasMoreTokens()){
			Token tkn = new Token(st.nextToken());
			switch (tkn.getType()) {
			case NUMBER:
				//数字はバッファに追加
				buf.add(tkn);
				break;
			case OPPAREN:
				//スタックに突っ込む
				operators.push(tkn);
				break;
			case CLPAREN:
				//'('までスタックからポップし、バッファへ
				while(true){
					Token e = operators.pop();
					if(e.getType() == Type.OPPAREN) break;
					buf.add(e);
				}
				break;
			case FACTOP:
				insertOperator(operators, tkn, buf, precedence_over_factops);
				break;
			case TERMOP:
				insertOperator(operators, tkn, buf, precedence_over_termops);
				break;
			default:
				//理論上はここには来ないはず。。
				throw new CalcFormatException("未知のトークンです: " + tkn.getType().toString());
			}
		}
		
		while(!operators.isEmpty()){
			Token e = operators.pop();
			buf.add(e);
		}
		
		return buf;
	}
	
	/**
	 * convertToRPN関数内で演算子が見つかった際に行う処理
	 * その際にスタックに入っている自分よりも優先順位の高い演算子をバッファに移す
	 * @param operators 演算子スタック
	 * @param tkn 現在見ているトークン
	 * @param buf バッファ
	 * @param priority_ops tknよりも優先順位の高い演算子の集合*/
	private static void insertOperator(Deque<Token> operators, Token tkn, List<Token> buf, List<Type> priority_ops){
		if(operators.isEmpty()){
			operators.push(tkn);
			return;
		}
		
		Token e = operators.peek();
		while(priority_ops.contains(e.getType())){
			//現在見てる演算子よりも優先順位が高かったらバッファに移す
			buf.add(e);
			operators.pop();
			if(operators.isEmpty()) break;
			e = operators.peek();
		}
		
		operators.push(tkn);
	}
	
	/**
	 * @param 数式の文字列
	 * @return 数式の表す分数
	 * @throws CalcFormatException 
	 * */
	public static Fraction calc(String expr) throws CalcFormatException{
		return calc(convertToRPN(expr));
	}
	
	/**トークン列をRPNを用いて計算
	 * @param 式を表すトークン列
	 * @return 数式の表す分数
	 * @throws CalcFormatException 
	 * */
	public static Fraction calc(List<Token> exprs) throws CalcFormatException{
		Stack<Fraction> stack = new Stack<Fraction>(); 
		for (Token token : exprs) {
			switch (token.getType()) {
			case NUMBER:
				stack.push(new Fraction(Long.parseLong(token.toString())));
				break;
			case FACTOP:
			case TERMOP:
				String op = token.toString();
				if(stack.size() < 2) throw new CalcFormatException("オペランドが足りません: " + op);
				Fraction right = stack.pop();
				Fraction left = stack.pop();
				switch (op) {
				case "+":
					stack.push(left.add(right));
					break;
				case "-":
					stack.push(left.subtract(right));
					break;
				case "*":
					stack.push(left.multiply(right));
					break;
				case "/":
					stack.push(left.divide(right));
					break;
				default:
					throw new CalcFormatException("未知の演算子です: " + op);
				}
				break;
			default:
				throw new CalcFormatException("未知のトークンです: " + token.getType());
			}
		}
		if(stack.size() != 1) throw new CalcFormatException("結果のサイズがおかしいです: " + stack.size());
		return stack.pop();
	}
	
	public static void doIt(String[] args){
		String expr = null;
		boolean isDouble = false;
		
		//引数が1つで無いとき
		if(args.length == 0){	// 引数が0の時
			System.out.println("数式を入力してください。");
			return;
		} else if(args.length > 1){
			if(!args[0].equals("-d")){
				System.out.println("オプションが無効です。");
				return;
			}
			isDouble = true;
			expr = args[1];
		} else{
			expr = args[0];
		}
		
		//空白の除去
		expr = expr.replace(" ", "");
		
		try {
			Fraction res = calc(expr);
			System.out.println(res.toString(isDouble));
		} catch (CalcFormatException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} 
	}
	
	//エントリポイントはmainかdoIt
	public static void main(String[] args) {
//		doIt(args);
		doIt(new String[] {"1b+3"});
	}

}
