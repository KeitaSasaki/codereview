package calc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {

	/** 標準出力をテストするためのフィールドとメソッド*/
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}

	@Test
	public void 引数の数が多いとオプションが無効ですと表示する(){
		Calculator.doIt(new String[] {"1", "2", "3"});
		//引数が2以上の時は「-d, 式」の形式以外だと「オプションが無効です。」と表示
		assertEquals("オプションが無効です。\n", outContent.toString());
	}

	@Test
	public void 引数がないと数式を入力してくださいと表示する(){
		Calculator.doIt(new String[] {});
		assertEquals("数式を入力してください。\n", outContent.toString());
	}

	@Test
	public void 数値でないとエラーメッセージを出す(){
		//数値でないと（不正な値：For input string: "a"）のように表示
		Calculator.doIt(new String[] {"1b+3"});
		assertEquals("input is \"1b+3\".", "不正な値：For input string: \"b\"\n", outContent.toString());
	}

	@Test
	public void dオプションで少数表示(){
		Calculator.doIt(new String[] {"-d", "3/2"});
		assertEquals("1.5\n", outContent.toString());
	}

	@Test
	public void 空白を許す(){
		Calculator.doIt(new String[] {"1 + 2 * 3"});
		assertEquals("7\n", outContent.toString());
	}

	@Test
	public void 計算が正しい() throws CalcFormatException{
		//サンプル
		assertThat("1+2*3", Calculator.calc("1+2*3").toString(), is("7"));
		assertThat("(1+2)*3", Calculator.calc("(1+2)*3").toString(), is("9"));
		assertThat("2/3+2*3", Calculator.calc("2/3+2*3").toString(), is("20/3"));
		//toStringにtrueを渡すとdoubleで表示
		assertThat("2/3+2*3（小数）", Calculator.calc("2/3+2*3").toString(true), is("6.666666666666667"));

		assertThat("1-3", Calculator.calc("1-3").toString(), is("-2"));
		assertThat("100+200/3", Calculator.calc("100+200/3").toString(), is("500/3"));
		assertThat("5+4*3+2/6", Calculator.calc("5+4*3+2/6").toString(), is("52/3"));
		assertThat("1+2*3-4", Calculator.calc("1+2*3-4").toString(), is("3"));
		assertThat("(1+4)*(3+7)/5", Calculator.calc("(1+4)*(3+7)/5").toString(), is("10"));
		assertThat("(1+4)*(3+7)/7", Calculator.calc("(1+4)*(3+7)/7").toString(), is("50/7"));
		assertThat("1+2+3", Calculator.calc("1+2+3").toString(), is("6"));
		assertThat(Calculator.calc("20/3").toString(), is("20/3"));
	}

}
