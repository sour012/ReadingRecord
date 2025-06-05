package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ProgressView progressView;
    private EditText etGoal;
    private int currentProgress = 0;
    private int goal = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化组件
        progressView = findViewById(R.id.progressView);
        etGoal = findViewById(R.id.etGoal);
        Button btnSetGoal = findViewById(R.id.btnSetGoal);
        Button btnAdd10 = findViewById(R.id.btnAdd10);
        Button btnAdd30 = findViewById(R.id.btnAdd30);
        Button btnReset = findViewById(R.id.btnReset);

        // 加载保存的数据
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        goal = prefs.getInt("goal", 60);
        currentProgress = prefs.getInt("progress", 0);
        progressView.setTotalPages(prefs.getInt("totalPages", 0));
        etGoal.setText(String.valueOf(goal));
        updateProgress();

        // 按钮事件
        btnSetGoal.setOnClickListener(v -> setGoal());
        btnAdd10.setOnClickListener(v -> addProgress(10));
        btnAdd30.setOnClickListener(v -> addProgress(30));
        btnReset.setOnClickListener(v -> resetProgress());
    }

    private void setGoal() {
        try {
            goal = Integer.parseInt(etGoal.getText().toString());
            if (goal <= 0) {
                Toast.makeText(this, "目标必须大于0", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putInt("goal", goal);
            editor.apply();
            updateProgress();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效数字", Toast.LENGTH_SHORT).show();
        }
    }

    private void addProgress(int minutes) {
        currentProgress += minutes;

        int pagesRead = minutes;
        progressView.setTotalPages(progressView.getTotalPages() + pagesRead);

        saveProgress();
        updateProgress();

        if (currentProgress >= goal) {
            Toast.makeText(this, "读书任务已完成！", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetProgress() {
        currentProgress = 0;
        progressView.setTotalPages(0);
        saveProgress();
        updateProgress();
        Toast.makeText(this, "进度已重置", Toast.LENGTH_SHORT).show();
    }

    private void saveProgress() {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putInt("progress", currentProgress);
        editor.putInt("goal", goal);
        editor.putInt("totalPages", progressView.getTotalPages());
        editor.apply();
    }

    private void updateProgress() {
        if (goal > 0) {
            float percent = Math.min((currentProgress * 100.0f) / goal, 100);
            progressView.setProgress(percent);
        }
    }

    // 获取总页数（供ProgressView调用）
    public int getTotalPages() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        return prefs.getInt("totalPages", 0);
    }
}