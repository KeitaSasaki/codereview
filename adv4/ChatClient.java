// 入出力ストリームを使うので，java.io.* を import
import java.io.*;
// ソケットを使うので java.net.* を import
import java.net.*;

// java ChatClient ホスト名 ポート番号
// と呼び出す
public class ChatClient{
    public static void main(String args[]){
        // ホスト名
        String hostName = "localhost";
        if(args.length >= 1) hostName = args[0];
        // ポート番号を文字列から整数に変換
        int port = 44444;
        if(args.length >= 2) port = Integer.parseInt(args[1]);
        try{
            // サーバと接続するためのソケットを作る
            Socket sock = new Socket(hostName,port);
            // ユーザからの入力のストリームの作成
            BufferedReader ui = new BufferedReader(new InputStreamReader(System.in));
            // サーバからの入力のストリームの作成
            BufferedReader si = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            // サーバへの出力のストリームの作成
            PrintWriter so = new PrintWriter(sock.getOutputStream(),true);
            // ユーザへの出力のストリームの作成
            PrintWriter uo = new PrintWriter(System.out,true);
            // ユーザ入力をサーバに転送するスレッドを作成し, start
            new Thread(new Sender(ui, so, sock)).start();
            // サーバからの入力をユーザに転送するスレッドを作成し, start
            new Thread(new Sender(si, uo, sock)).start();
        } catch(IOException e){
            // System.err.println(e);
            System.err.println("サーバと通信できません。サーバ管理者に問い合わせてください。");

        }
    }
}
