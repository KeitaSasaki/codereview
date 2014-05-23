// 入出力ストリームを使うので，java.io.* を import
import java.io.*;
// ソケットを使うので java.net.* を import
import java.net.*;

import java.util.List;

// 一人のクライアントとの通信を担当するスレッド
// スレッド上で走らせるため Runnable インタフェースを実装
public class Worker implements Runnable{
    // 通信のためのソケット
    private final Socket sock;
    // そのソケットから作成した入出力用のストリーム
    private PrintWriter out;
    private BufferedReader in;
    // サーバ本体のメソッドを呼び出すために記憶
    private final ChatServer chatServer;
    // 担当するクライアントの番号
    private final int id;
    private String userName = "anonymous";

    // コンストラクタ
    public Worker(int id,Socket s,ChatServer cs){
        this.id = id;
        chatServer = cs;
        sock = s;
        out = null;
        in = null;
        // System.out.println("worker " +  id);

    }

    public String getName(){
        return userName;
    }

    public String getUserName(){
        if(getSocketStream() == false) return userName;

        out.print("Connect to ...\nあなたのお名前：\n>");
        // out.write(">");
        out.flush();

        try {
            // in.mark(256);
            // in.reset();
            userName = in.readLine();
        }
        catch (IOException e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
        return userName;
    }

    public void displayWelcomeMessage(List<Worker> users){
        out.println("こんにちは" + userName + "さん。");
        if(users.size() == 0){
            out.println("あなたが最初の入室者です。他の方の入室を待ちましょう。");
        } else{
            out.print("現在の参加者は、");
            for(Worker u: users){
                out.printf("%sさん ", u.getName());
            }
            out.println("です。");
        }
        out.println();
    }

    public boolean getSocketStream(){
        boolean isSuccess = true;
        try {
            if(out == null) out = new PrintWriter(sock.getOutputStream(),true);
            if(in == null) in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        }
        catch (Throwable e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;

    }

    // 対応するスレッドが start した時に呼ばれる．
    public void run(){
        // System.out.println("Thread running:"+Thread.currentThread());
        try{
            // ソケットからストリームの作成
            if(getSocketStream() == false) return;

            String s = null;
            // ソケットからの入力があったら，
            while((s = in.readLine()) != null){
                if(s.equals("exit")){
                    break;
                } else{
                    // クライアント全体に送る．
                    chatServer.sendMessageToAll(String.format("(%s)%s", userName, s), this);
                }

            }
            sendMessageToClient("サーバとの接続が切れました。");
            // sendMessageToClient("exit");
            in.close();
            out.close();
            // 自分自身をテーブルから取り除く
            // ソケットを閉じる
            sock.close();
        } catch(IOException ioe){
            sendMessageToClient("サーバとの接続が切れました。");
            sendMessageToClient("exit");
        } finally{

            chatServer.remove(this);
        }
    }

    // 対応するソケットに文字列を送る
    public void sendMessageToClient(String s){
        out.println(s);
    }
}
