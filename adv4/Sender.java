// 入出力ストリームを使うので，java.io.* を import
import java.io.*;
// ソケットを使うので java.net.* を import
import java.net.*;

// ユーザのキーボード入力を受け取って, サーバに送るスレッドと
// サーバと接続したソケットからメッセージを受け取って表示するスレッド
// を共用したクラス
public class Sender implements Runnable{
    // 入力ストリーム
    private BufferedReader in;
    // 出力ストリーム
    private PrintWriter out;
    private Socket sock;

    // コンストラクタ
    public Sender(BufferedReader in, PrintWriter out, Socket sock){
        // 左辺の this.inはインスタンス変数の in, 右辺の inは引数の in
        this.in = in;
        this.out = out;
        this.sock = sock;
    }

    // スレッドが start すると,これが呼ばれて,動き続ける．
    public void run(){
        try{
            String s;
            // 入力ストリームから一行入力
            while((s = in.readLine()) != null){
                // 出力ストリームに一行出力
	            out.println(s);
                if(s.equals("exit") || !sock.isConnected()) break;
                System.out.println(String.format("conn = %s, close = %s, bind = %s\n", sock.isConnected(), sock.isClosed(), sock.isBound()));
            }
        } catch(Exception e){
            System.err.println(e);
        }
    }
}
