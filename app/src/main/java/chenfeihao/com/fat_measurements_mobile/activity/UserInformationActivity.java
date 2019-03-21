package chenfeihao.com.fat_measurements_mobile.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.app.App;
import chenfeihao.com.fat_measurements_mobile.custom.layout.SelectPicPopWindow;
import chenfeihao.com.fat_measurements_mobile.custom.layout.TitleLayout;
import chenfeihao.com.fat_measurements_mobile.custom.listener.SelectPicListener;
import chenfeihao.com.fat_measurements_mobile.http.common.ResponseView;
import chenfeihao.com.fat_measurements_mobile.http.retrofit.UserHttpService;
import chenfeihao.com.fat_measurements_mobile.pojo.bo.MobileUser;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.UserDto;
import chenfeihao.com.fat_measurements_mobile.util.DensityUtil;
import chenfeihao.com.fat_measurements_mobile.util.LogUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class UserInformationActivity extends AppCompatActivity {
    /**
     * UI
     */
    private TitleLayout customTitle;

    private EditText alterUserNameEditText;

    private EditText alterUserSignatureEditText;

    private EditText alterUserPwdEditText;

    private TextView alterUserHeadPortraitTextView;

    private TextView saveEditUserInfoTextView;

    /**
     * retrofit
     */
    UserHttpService userHttpService;

    /**
     * data
     */
    // 拍照
    private static final int TAKE_PHOTO = 1;
    // 从相册选择
    private static final int SELECT_PHOTO = 2;
    // 临时文件名
    private static final String TEMPORARY_IMAGE_PATH = "output_image_temporary.jpg";

    private Uri imageUri;

    private String headPortraitUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        initUI();
        initRetrofit();
    }

    private void initRetrofit() {
        userHttpService = ((App)getApplicationContext()).getRetrofit().create(UserHttpService.class);
    }

    private void initUI() {
        customTitle = findViewById(R.id.user_custom_title);
        customTitle.setTitle("编辑资料");

        alterUserNameEditText = findViewById(R.id.alter_user_name);
        alterUserSignatureEditText = findViewById(R.id.alter_user_signature);
        alterUserPwdEditText = findViewById(R.id.alter_user_pwd);
        alterUserHeadPortraitTextView = findViewById(R.id.alter_user_head_portrait);
        saveEditUserInfoTextView = findViewById(R.id.save_edit_user_info);

        MobileUser mobileUser = getApp().getMobileUser();
        alterUserNameEditText.setText(mobileUser.getUserName());
        alterUserPwdEditText.setText(mobileUser.getUserPassword());
        alterUserSignatureEditText.setText(mobileUser.getSignature());

        initAlterUserHeadPortraitTextViewListener();
        initSaveEditUserInfoTextViewListener();
        // initAlterUserSignatureEditTextListener();
    }

    private void initSaveEditUserInfoTextViewListener() {
        saveEditUserInfoTextView.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            dialog.setTitle("提示");
            dialog.setMessage("确定要保存编辑的内容吗?");
            dialog.setCancelable(false);

            dialog.setPositiveButton("确定", (dialog1, which) -> {
                saveEditUserInfo();
                dialog1.dismiss();
                finish();
            });
            dialog.setNegativeButton("放弃", (dialog2, which) -> dialog2.dismiss());

            dialog.show();
        });
    }

    private void initAlterUserHeadPortraitTextViewListener() {
        alterUserHeadPortraitTextView.setOnClickListener(v -> {
            SelectPicPopWindow selectPicPopWindow = new SelectPicPopWindow(UserInformationActivity.this, null);
            selectPicPopWindow.showAtLocation(findViewById(R.id.activity_user_information), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, DensityUtil.getBottomBarHeight(this) + 15);

            selectPicPopWindow.setSelectPicListener(initSelectPicListener());
        });
    }

    private void initAlterUserSignatureEditTextListener() {
        alterUserSignatureEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!alterUserSignatureEditText.hasFocus()) {
                    SharedPreferences.Editor sharedPreferences = getSharedPreferences("user_signature", MODE_PRIVATE).edit();
                    sharedPreferences.putString("user_signature", alterUserSignatureEditText.getText().toString());
                    sharedPreferences.apply();
                }
            }
        });
    }

    private void updatePersonalSignature() {
        SharedPreferences.Editor sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE).edit();
        sharedPreferences.putString("user_signature", alterUserSignatureEditText.getText().toString());
        sharedPreferences.apply();
    }

    private void saveEditUserInfo() {
        updatePersonalSignature();

        MobileUser mobileUser = getApp().getMobileUser();
        UserDto userDto = new UserDto();
        userDto.setId(mobileUser.getId());
        userDto.setUserName(alterUserNameEditText.getText().toString());
        userDto.setUserPassword(alterUserPwdEditText.getText().toString());

        try {
            userHttpService.updateUserInfo(userDto).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(stringResponseView -> LogUtil.V("用户信息更新成功"));

            SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
            String mobileUserJsonStr = sharedPreferences.getString("mobile_user", null);

            MobileUser oldUserInfo = JSON.parseObject(mobileUserJsonStr, MobileUser.class);
            oldUserInfo.setUserName(alterUserNameEditText.getText().toString());
            oldUserInfo.setUserPassword(alterUserPwdEditText.getText().toString());
            oldUserInfo.setUserHeadPortrait(headPortraitUrl);

            SharedPreferences.Editor userDataEditor = getSharedPreferences("user_data", MODE_PRIVATE).edit();
            userDataEditor.putString("mobile_user", JSON.toJSONString(oldUserInfo));
            userDataEditor.apply();

            getApp().initUserInfo();
        } catch (Exception e) {
            LogUtil.V("用户信息更新失败");
            e.printStackTrace();
        }
    }

    private SelectPicListener initSelectPicListener() {
        SelectPicListener selectPicListener = new SelectPicListener() {
            @Override
            public void selectPicFromCameraListener() {
                selectPicFromCamera();
            }

            @Override
            public void selectPicFromStorageListener() {
                selectPicFromStorage();
            }
        };

        return selectPicListener;
    }

    private void selectPicFromCamera() {
        File outputImage = new File(getExternalCacheDir(), TEMPORARY_IMAGE_PATH);

        if (outputImage.exists()) {
            outputImage.delete();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(UserInformationActivity.this, "com.chenfeihao.fat_measurement.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        // 启动相机外部Activity
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    public void selectPicFromStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                /**
                 * 将拍摄的照片上传给服务端
                 */
                if (resultCode == RESULT_OK) {
                    uploadFile2Server(imageUri);
                }
                break;
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    uploadFile2Server(uri);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 通过uri和selection来获取真实的图片路径
     * */
    private String getImagePath(Uri uri,String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void uploadFile2Server(Uri uri) {
        // OutputStream outputStream = getContentResolver().openOutputStream(uri);
        File file = new File(getImagePath(uri, null));
        // 创建RequestBody，传入参数："multipart/form-data"，File
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part requestImgPart =
                MultipartBody.Part.createFormData("headportrait", "headportrait_temporary.jpg", fileRequestBody);

        try {
            userHttpService.uploadHeadPortrait(requestImgPart).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(stringResponseView -> {
                headPortraitUrl = stringResponseView.getResult();
                LogUtil.V("头像上传成功");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private App getApp() {
        return (App)getApplicationContext();
    }
}
