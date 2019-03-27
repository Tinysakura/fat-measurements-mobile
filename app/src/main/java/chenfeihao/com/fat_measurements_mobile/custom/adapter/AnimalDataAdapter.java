package chenfeihao.com.fat_measurements_mobile.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Measure;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.activity.MeasureActivity;
import chenfeihao.com.fat_measurements_mobile.activity.MeasureResultActivity;
import chenfeihao.com.fat_measurements_mobile.constant.AnimalConstant;
import chenfeihao.com.fat_measurements_mobile.constant.AnimalConstant.AnimalDraftEnum;
import chenfeihao.com.fat_measurements_mobile.custom.OssConstant;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.AnimalDataDto;
import chenfeihao.com.fat_measurements_mobile.util.OssUtil;

import static chenfeihao.com.fat_measurements_mobile.constant.AnimalConstant.AnimalDraftEnum.*;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/25
 */
public class AnimalDataAdapter extends RecyclerView.Adapter<AnimalDataAdapter.ViewHolder> {

    private Context mContext;

    private List<AnimalDataDto> animalDataDtoList;

    private static String[] animalVariety = {"大白猪", "长白猪", "杜洛克"};

    private static SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    public AnimalDataAdapter(List<AnimalDataDto> animalDataDtoList) {
        this.animalDataDtoList = animalDataDtoList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView cardImgView;
        TextView cardAnimalId;
        TextView cardAnimalSex;
        TextView cardAnimalVariety;
        TextView cardAnimalTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (CardView) itemView;
            cardImgView = cardView.findViewById(R.id.card_img_view);
            cardAnimalId = cardView.findViewById(R.id.card_animal_id);
            cardAnimalSex = cardView.findViewById(R.id.card_animal_sex);
            cardAnimalVariety = cardView.findViewById(R.id.card_animal_variety);
            cardAnimalTime = cardView.findViewById(R.id.card_animal_time);
        }
    }

    @Override
    public AnimalDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalDataAdapter.ViewHolder viewHolder, int i) {
        AnimalDataDto animalDataDto = animalDataDtoList.get(i);

        viewHolder.cardAnimalId.setText(animalDataDto.getAnimalId());

        if (AnimalConstant.AnimalSexEnum.MALE.getCode().equals(animalDataDto.getAnimalSex())) {
            viewHolder.cardAnimalSex.setTextColor(mContext.getResources().getColor(R.color.colorBlue1));
            viewHolder.cardAnimalSex.setText("♂");
        } else if (AnimalConstant.AnimalSexEnum.FEMALE.getCode().equals(animalDataDto.getAnimalSex())) {
            viewHolder.cardAnimalSex.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            viewHolder.cardAnimalSex.setText("♀");
        }

        viewHolder.cardAnimalVariety.setText(animalVariety[Integer.parseInt(animalDataDto.getAnimalVariety().toString().substring(2, 3)) - 1]);

        viewHolder.cardAnimalTime.setText(dataFormat.format(new Date(animalDataDto.getDbUpdateTime())));

        Glide.with(mContext).load(OssUtil.generateOssUrl(animalDataDto.getNosKey())).into(viewHolder.cardImgView);

        /**
         * 设置点击事件
         * 根据animalData的状态，若为已测量则跳转到MeasureResultActivity，否则跳入MeasureActivity
         */
        viewHolder.cardImgView.setOnClickListener(v -> {
            switch (animalDataDto.getAnimalDraft()) {
                case 0:
                    Intent intent1 = new Intent(mContext, MeasureActivity.class);
                    intent1.putExtra("animal_data_draft", JSON.toJSONString(animalDataDto));
                    intent1.putExtra("animal_data_id", animalDataDto.getId());
                    mContext.startActivity(intent1);

                    break;
                case 1:
                    Intent intent2 = new Intent(mContext, MeasureResultActivity.class);
                    intent2.putExtra("animal_data_id", animalDataDto.getId());
                    mContext.startActivity(intent2);

                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return animalDataDtoList.size();
    }
}
