package chenfeihao.com.fat_measurements_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.custom.layout.TitleLayout;
import chenfeihao.com.fat_measurements_mobile.util.StringUtil;

/**
 * 填写测量数据进行相关测量的Activity
 */
public class MeasureActivity extends AppCompatActivity {
    /**
     * UI
     */
    private TitleLayout customTitleLayout;

    private EditText measureAnimalIdEditText;

    private Spinner measureAnimalTextVarietySpiner;

    private Spinner measureAnimalSexSpiner;

    private EditText measureAnimalWeightEditText;

    private TextView measureSaveDraftTextView;

    private TextView measureSubmitTextView;

    private ImageView measureBUltrasoundImageView;

    private TextView measureBUltrasoundFileNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        initUI();
    }

    private void initUI() {
        initCustomTitleLayout();
        initMeasureBUltrasoundRelated();
    }

    private void initCustomTitleLayout() {
        customTitleLayout = findViewById(R.id.measure_custom_title);
        customTitleLayout.setTitle("发布");
    }

    private void initMeasureBUltrasoundRelated() {
        measureBUltrasoundImageView = findViewById(R.id.measure_b_ultrasound);
        measureBUltrasoundFileNameTextView = findViewById(R.id.measure_b_ultrasound_file_name);

        Intent intent = getIntent();

        if (!StringUtil.isEmpty(intent.getStringExtra("file_name"))) {
            measureBUltrasoundFileNameTextView.setText(intent.getStringExtra("file_name"));
        }

        if (intent.getData() != null) {
          // TODO 用户选中的B超图片
        } else {
            Glide.with(this).load(R.mipmap.load_wait).into(measureBUltrasoundImageView);
        }
    }


}
