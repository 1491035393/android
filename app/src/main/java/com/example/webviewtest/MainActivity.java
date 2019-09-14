package com.example.webviewtest;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();//隐藏标题栏

        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏状态栏
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag,flag);

        setContentView(R.layout.activity_main);
//        final WebView webView = (WebView) findViewById(R.id.web_view);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("file:///android_asset/B.html");
//        webView.clearCache(true);
//        webView.clearFormData();
//        webView.clearHistory();
//       getDataByJsoup();
        handler.post(task);//立即调用
//        try{
////            File input = new File("com/system.html");
////            System.out.println("file load success");
////            System.out.println("file catch here");
////            Document doc = Jsoup.parse(input, "UTF-8","");
////            System.out.println("catch here");
//            Document doc = Jsoup.connect("http://47.106.245.23:80/homepage.html").get();
//            Element text = doc.select("p").first();
//            String inf = text.text();
//            Log.e("the inf is","jsoup=====>>"+inf+" .");
//            System.out.println(inf);
//        }catch (IOException e){
//            Log.e("there is error","please check it");
//
//        }
    }

    private void getDataByJsoup(){
        //获取html数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
//                    File input = new File("C:\\Users\\ling\\Desktop\\system.html");
//                    Document doc = Jsoup.parse(input, "UTF-8");
                    Document doc = Jsoup.connect("http://192.168.3.3:88/system_B.html").get();
                    Element text = doc.select("p").first();
                    String inf = text.text();
                    Log.e("p's","jsoup=====>>"+inf+" .");
                    if(inf.equals("shutdown")){
                        Log.e("jay","that's ok");
//                        Runtime.getRuntime().exec(new String[]{"su","-c","shutdown"});
                        sendRequestWithHttpURLConnection();

                        shutdown();
                    }
                }catch (IOException e){
                    Log.e("there is error","please check it");
                }
            }
        }).start();

    }
    public static int shutdown() {
        //关机函数,https://blog.csdn.net/FL1623863129/article/details/72850399
        int r = 0;
        try {
            java.lang.Process process = Runtime.getRuntime().exec(new String[]{"su","-c", "reboot -p"});
            r = process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
            r = -1;
        } catch (InterruptedException e) {
            e.printStackTrace();
            r = -1;
        }
        Log.i("info", "time: "+r);
        return r;
    }

    private Handler handler = new Handler();
        private Runnable task = new Runnable() {
            //循环执行代码,https://blog.csdn.net/QQ1401617948/article/details/52947080
            @Override
            public void run() {
                handler.postDelayed(this,5000);//设置循环时间
                //需要执行的代码
                final WebView webView = (WebView) findViewById(R.id.web_view);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl("file:///android_asset/B.html");
                webView.clearCache(true);
                webView.clearFormData();
                webView.clearHistory();
                getDataByJsoup();
            }
        };

    private void sendRequestWithHttpURLConnection(){
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try{
                    URL url = new URL("http://192.168.3.3:88/shutdown_sign_B.asp");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    // 对获取的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        response.append(line);
                    }
//                    showResponse();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(reader != null){
                        try{
                            reader.close();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

//    public void onCreate(Bundle savadInstanaceState){
//        super.onCreate(savadInstanaceState);
//        setContentView(R.layout.activity_main);
//        handler.post(task);//立即调用
//    }
}




//public class BootBroadcastReceiver extends BroadcastReceiver{
//    public static final String action_boot = "android.intent.action.BOOT_COMPLETED";
//
//    @Override
//    public void onReceive(Context context, Intent intent){
//        if(intent.getAction().equals(action_boot)){
//            Toast.makeText(context,"收到开机广播",Toast.LENGTH_SHORT).show();
//            Intent ootStartIntent=new Intent(context,MainActivity.class);
//            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivities(ootStartIntent);
//        }
//    }
//}
