package chenfeihao.com.fat_measurements_mobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.math.BigDecimal;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.app.App;
import chenfeihao.com.fat_measurements_mobile.custom.layout.TitleLayout;
import chenfeihao.com.fat_measurements_mobile.http.common.ResponseView;
import chenfeihao.com.fat_measurements_mobile.http.retrofit.AnimalDataHttpService;
import chenfeihao.com.fat_measurements_mobile.http.retrofit.AnimalResultHttpService;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.AnimalDataDto;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.AnimalResultDto;
import chenfeihao.com.fat_measurements_mobile.util.LogUtil;
import chenfeihao.com.fat_measurements_mobile.util.OssUtil;
import chenfeihao.com.fat_measurements_mobile.util.StringUtil;
import chenfeihao.com.fat_measurements_mobile.util.UriPathSwitchUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 填写测量数据进行相关测量的Activity
 */
public class MeasureActivity extends AppCompatActivity {
    /**
     * UI
     */
    private TitleLayout customTitleLayout;

    private EditText measureAnimalIdEditText;

    private Spinner measureAnimalVarietySpinner;

    private Spinner measureAnimalSexSpinner;

    private EditText measureAnimalWeightEditText;

    private TextView measureSaveDraftTextView;

    private TextView measureSubmitTextView;

    private ImageView measureBUltrasoundImageView;

    private TextView measureBUltrasoundFileNameTextView;

    private TextView bUltrasoundReplaceTextView;

    /**
     * data
     */
    private File bUltrasoundFile;

    private static final int SELECT_LOCAL_FILE = 3;

    private static final Integer[] animalVarietyArray = new Integer[]{101, 102, 103};

    private static final Integer[] animalSexArray = new Integer[]{0, 1};

    /**
     * retrofit
     */
    private AnimalDataHttpService animalDataHttpService;

    private AnimalResultHttpService animalResultHttpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        initUI();
        preFill();
        initRetrofitClient();
    }

    /**
     * 预填草稿数据
     */
    private void preFill() {
        Intent intent = getIntent();

        Gson gson = new Gson();
        AnimalDataDto draftData = gson.fromJson(intent.getStringExtra("animal_data_draft"), AnimalDataDto.class);

        if (draftData == null) {
            return;
        }

        measureAnimalIdEditText.setText(draftData.getAnimalId());
        measureAnimalWeightEditText.setText(draftData.getAnimalWeight().toString());
        measureAnimalVarietySpinner.setSelection(Integer.parseInt(String.valueOf(draftData.getAnimalVariety()).substring(2, 3)));
        measureAnimalSexSpinner.setSelection(draftData.getAnimalSex());

        Glide.with(this).load(OssUtil.generateOssUrl(draftData.getNosKey())).into(measureBUltrasoundImageView);
        measureBUltrasoundFileNameTextView.setText(draftData.getNosKey());
    }

    private void initRetrofitClient() {
        animalDataHttpService = getApp().getRetrofit().create(AnimalDataHttpService.class);

        animalResultHttpService = getApp().getRetrofit().create(AnimalResultHttpService.class);
    }

    private void initUI() {
        measureAnimalIdEditText = findViewById(R.id.measure_animal_id);
        measureAnimalWeightEditText = findViewById(R.id.measure_animal_weight);
        measureAnimalVarietySpinner = findViewById(R.id.measure_animal_variety);
        measureAnimalSexSpinner = findViewById(R.id.measure_animal_sex);
        measureBUltrasoundImageView = findViewById(R.id.measure_b_ultrasound);
        measureBUltrasoundFileNameTextView = findViewById(R.id.measure_b_ultrasound_file_name);
        bUltrasoundReplaceTextView = findViewById(R.id.measure_b_ultrasound_replace);

        initCustomTitleLayout();
        initMeasureBUltrasoundRelated();

        initMeasureSaveDraftTextView();
        initMeasureSubmitTextView();

        initBUltrasoundReplaceTextView();
    }

    /**
     * 重新选择B超文件
     */
    private void initBUltrasoundReplaceTextView() {
        bUltrasoundReplaceTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");//无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, SELECT_LOCAL_FILE);
        });
    }

    private void initMeasureSaveDraftTextView() {
        measureSaveDraftTextView = findViewById(R.id.measure_save_draft);

        measureSaveDraftTextView.setOnClickListener(v -> {
            try {
                RequestBody requestBody = requestBodyBuild();

                animalDataHttpService.saveAnimalDataForm(requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(animalResultDtoResponseView -> {
                    Toast toast = Toast.makeText(MeasureActivity.this, "草稿保存成功", Toast.LENGTH_SHORT);
                    toast.show();

                    try {
                        Thread.sleep(1000);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                LogUtil.V("动物数据表单上传失败");
                e.printStackTrace();
            }
        });
    }

    private void initMeasureSubmitTextView() {
        measureSubmitTextView = findViewById(R.id.measure_submit);

        measureSubmitTextView.setOnClickListener(v -> {
            try {
                RequestBody requestBody = requestBodyBuild();

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(false);
                progressDialog.show();

                animalDataHttpService.saveAnimalDataForm(requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(animalResultDtoResponseView -> {
                    Long animalDataId = animalResultDtoResponseView.getResult().getId();
                    Integer animalSex = animalResultDtoResponseView.getResult().getAnimalSex();

                    try {
                        animalResultHttpService.measure(animalDataId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResponseView<AnimalResultDto>>() {
                            @Override
                            public void call(ResponseView<AnimalResultDto> animalResultDtoResponseView) {
                                /**
                                 * 将序列化后的测量结果放入intent后跳转到MeasureResultActivity
                                 */
                                Intent intent = new Intent(MeasureActivity.this, MeasureResultActivity.class);

                                String jsonStr = JSON.toJSONString(animalResultDtoResponseView.getResult());
                                LogUtil.V("序列化结果:" + jsonStr);
                                intent.putExtra("measure_finish_result", jsonStr);
                                intent.putExtra("sex", animalSex);

                                progressDialog.cancel();
                                finish();
                                startActivity(intent);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.V("动物数据测量失败");
                    }
                });
            } catch (Exception e) {
                LogUtil.V("动物数据表单上传失败");
                e.printStackTrace();
            }
        });
    }

    private RequestBody requestBodyBuild() {
        Intent intent = getIntent();
        Long animalDataId;
        animalDataId = intent.hasExtra("animal_data_id") ? intent.getLongExtra("animal_data_id", 0) : null;

        String animalId = measureAnimalIdEditText.getText().toString();
        BigDecimal animalWeight = StringUtil.isEmpty(measureAnimalWeightEditText.getText().toString()) ? new BigDecimal(0) : new BigDecimal(measureAnimalWeightEditText.getText().toString());
        Integer animalVariety = animalVarietyArray[measureAnimalVarietySpinner.getSelectedItemPosition()];
        Integer animalSex = animalSexArray[measureAnimalSexSpinner.getSelectedItemPosition()];

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("animalId", animalId)
                .addFormDataPart("animalWeight", animalWeight.toString())
                .addFormDataPart("animalSex", animalSex + "")
                .addFormDataPart("animalVariety", animalVariety + "")
                .addFormDataPart("animalBUltrasound", bUltrasoundFile.getName(), RequestBody.create(MediaType.parse("image/*"), bUltrasoundFile));

        if (animalDataId != null) {
            builder.addFormDataPart("id", animalDataId + "");
        }

        RequestBody requestBody = builder.build();

        return requestBody;
    }

    private void initCustomTitleLayout() {
        customTitleLayout = findViewById(R.id.measure_custom_title);
        customTitleLayout.setTitle("发布");
    }

    private void initMeasureBUltrasoundRelated() {
        Intent intent = getIntent();

        String realPath = intent.getStringExtra("file_path");

        if (!StringUtil.isEmpty(realPath)) {
            bUltrasoundFile = new File(realPath);
            measureBUltrasoundFileNameTextView.setText(bUltrasoundFile.getName());
            Glide.with(this).load(bUltrasoundFile).into(measureBUltrasoundImageView);
        }
    }

    private App getApp() {
        return (App) getApplicationContext();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_LOCAL_FILE:
                    Uri uri = data.getData();
                    String realPath = UriPathSwitchUtil.getPathByUri4kitkat(this, uri);

                    bUltrasoundFile = new File(realPath);
                    measureBUltrasoundFileNameTextView.setText(bUltrasoundFile.getName());
                    Glide.with(this).load(bUltrasoundFile).into(measureBUltrasoundImageView);
                    break;
                default:
                    break;
            }
        }
    }
}
