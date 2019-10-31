package com.hxj.bsdiffdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button mBtnUpdate;

    private TextView mTvVersion;

    static {
        System.loadLibrary("bspatch");
    }

    public native void generateNewApkByPatch(String oldApkFile, String newApkFile, String patchFile);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnUpdate = findViewById(R.id.btn_update);
        mTvVersion = findViewById(R.id.tv_version);

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            Log.i("James", "onCreate versionName: " + versionName);
            mTvVersion.setText("当前的版本：" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mBtnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // 自定义提示框.
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                                100);
                    }
                } else {
                    update();
                }
            }
        });
    }

    private void update() {

        new UpdateApkAsyckTask().execute();
    }

    class UpdateApkAsyckTask extends AsyncTask<Void, Void, File> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected File doInBackground(Void... voids) {
            // 获取当前 apk 安装的路径.
            String oldApkFilePath = getApplicationInfo().sourceDir;

            // 新 apk 所在目录.
            File newApkFile = new File(Environment.getExternalStorageDirectory(), "test_new.apk");
            boolean newApkFileExist = false;
            if (!newApkFile.exists()) {
                try {
                    newApkFileExist = newApkFile.createNewFile();
                } catch (IOException e) {
                    newApkFileExist = false;
                    e.printStackTrace();
                }
            } else {
                newApkFileExist = true;
            }

            if (!newApkFileExist) {
                Toast.makeText(MainActivity.this, "创建文件失败", Toast.LENGTH_SHORT).show();
                return null;
            }

            // 服务器下载后的补丁文件
            File patchFile = new File(Environment.getExternalStorageDirectory(), "test.patch");
            if (!patchFile.exists()) {
                Toast.makeText(MainActivity.this, "暂未发现新版本", Toast.LENGTH_SHORT).show();
            }

            // 获取新的 apk 安装路径.
            String newApkFilePath = newApkFile.getAbsolutePath();

            // 获取补丁文件路径
            String patchFilePath = patchFile.getAbsolutePath();

            generateNewApkByPatch(oldApkFilePath, newApkFilePath, patchFilePath);

            return newApkFile;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);

            Log.i("James", "onPostExecute: " + file.getTotalSpace());
        }
    }
}
