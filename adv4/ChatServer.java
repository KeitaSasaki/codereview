// 入出力ストリームを使うので，java.io.* を import
import java.io.*;

// ソケットを使うので java.net.* を import
import java.net.*;

import java.util.List;
import java.util.LinkedList;
/**
 * Chatプログラムのサーバーとなるクラス
 * @see Worker
 * @see ChatClient
 * http://lecture.ecc.u-tokyo.ac.jp/~ktanaka/programming05/primers/6-3.html
 * */
public class ChatServer{
    // 各クライアントを記憶する配列．
    // private final int MAX_CLIENT = 100;
    // private Worker workers[] = new Worker[MAX_CLIENT];
    // private String clientNames[] = new String[MAX_CLIENT];
    private List<Worker> workers = new LinkedList<Worker>();
    private int latestClientId = 0;

    // コンストラクタ
    public ChatServer(int portNo){
        // ポート番号を portNoにする．同じマシンで同じポートを使うことは
        // できないので，ユーザごとに変えること(1023以下は使えない)
        int port = portNo;
        // 配列を作成
        // workers = new Worker[MAX_CLIENT];
        Socket sock;
        try{
            System.out.println("ポート番号" + port + "をオープンします。");
            // ServerSocketを作成
            ServerSocket servsock = new ServerSocket(port);
            System.out.println("オープンに成功しました。サーバを起動しました。");
            // 無限ループ，breakが来るまで
            while(true){
                // クライアントからのアクセスをうけつけた．
                sock = servsock.accept();
                latestClientId++;
                Worker w = new Worker(latestClientId, sock, this);
                //同期しないと名前の入力中に他のユーザーからメッセージが飛んできてそれが名前になってしまうことがある
                synchronized(w){
                    w.getUserName();
                    w.displayWelcomeMessage(workers);
                    sendEnterMessage(w);
                }
                // 対応するスレッドを走らせる
                synchronized(w){
                    new Thread(w).start();
                }
                workers.add(w);
            }
        } catch(IOException ioe){
            System.out.println(ioe);
        } finally {
            System.out.println("text");

        }
    }

    public ChatServer(){
        this(44444);
    }

    public static void main(String args[]) throws IOException{
        // インスタンスを1つだけ作る．
        if(args.length >= 1) new ChatServer(Integer.parseInt(args[0]));
        else new ChatServer();
    }

    // synchronized は，同期のためのキーワード．つけなくても動くことはある．
    public synchronized void sendMessageToAll(String s, Worker except){
        //except以外に送る
        for(Worker w: workers){
            if(w != except) w.sendMessageToClient(s);
        }
    }

    // クライアント id が抜けたこと記録し，他のユーザに送る．
    public void sendEnterMessage(Worker rem){
        sendMessageToAll(rem.getName() + "さんが入室しました。", rem);
    }
    // クライアント id が抜けたこと記録し，他のユーザに送る．
    public void remove(Worker rem){
        //クライアントを削除する
        workers.remove(rem);
        sendMessageToAll(rem.getName() + "さんが退室しました。", rem);
    }
}
