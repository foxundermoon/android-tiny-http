package com.getui.sdk.util.http_tiny.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.getui.sdk.util.http_tiny.DefaultHttpRequest;
import com.getui.sdk.util.http_tiny.HttpException;
import com.getui.sdk.util.http_tiny.HttpRequest;
import com.getui.sdk.util.http_tiny.HttpResponse;
import com.getui.sdk.util.http_tiny.IllegalUsageException;

import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    HttpRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        httpRequest =  new DefaultHttpRequest();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        textView = (TextView) findViewById(R.id.txt_msg);
        findViewById(R.id.basic_get_btn).setOnClickListener(v -> basicGet());
        findViewById(R.id.post_form_btn).setOnClickListener(v -> postForm());
        findViewById(R.id.post_json_btn).setOnClickListener(v -> postJson());
        findViewById(R.id.put_btn).setOnClickListener(v -> put());
        findViewById(R.id.patch_btn).setOnClickListener(v -> patch());
        findViewById(R.id.delete_btn).setOnClickListener(v -> delete());
        findViewById(R.id.multipart_upload_btn).setOnClickListener(v -> multipatUpload());
    }

    private void multipatUpload() {

    }

    private void delete() {

    }

    private void patch() {

    }

    private void put() {

    }

    private void postJson() {

    }

    private void postForm() {

    }

    private void basicGet() {
        new Thread(() -> {
            try {
                HttpResponse response = httpRequest.get().url("http://baidu.com").execute();
                p(new String(response.getBody()));
            } catch (IllegalUsageException e) {
                p(e.getMessage());
            } catch (HttpException e) {
                p(e.getMessage());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }).start();


    }

    void p(String msg) {
        runOnUiThread(() -> textView.setText(msg));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
