package chenfeihao.com.fat_measurements_mobile.custom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.constant.AnimalConstant;
import chenfeihao.com.fat_measurements_mobile.custom.OssConstant;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.AnimalDataDto;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/25
 */
public class AnimalDataAdapter extends RecyclerView.Adapter<AnimalDataAdapter.ViewHolder> {

    private Context mContext;

    private List<AnimalDataDto> animalDataDtoList;

    private static String[] animalVariety = {"大白猪", "长白猪", "杜洛克"};

    private static String urlFormat = "http://%s.%s/%s";

    public AnimalDataAdapter(List<AnimalDataDto> animalDataDtoList) {
        this.animalDataDtoList = animalDataDtoList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView cardImgView;
        TextView cardAnimalId;
        TextView cardAnimalSex;
        TextView cardAnimalVarity;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (CardView) itemView;
            cardImgView = cardView.findViewById(R.id.card_img_view);
            cardAnimalId = cardView.findViewById(R.id.card_animal_id);
            cardAnimalSex = cardView.findViewById(R.id.card_animal_sex);
            cardAnimalVarity = cardView.findViewById(R.id.card_animal_variety);
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
        } else if (AnimalConstant.AnimalSexEnum.FEMALE.getCode().equals(animalDataDto.getAnimalSex())) {
            viewHolder.cardAnimalSex.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }
        viewHolder.cardAnimalSex.setText(animalDataDto.getAnimalSex());

        viewHolder.cardAnimalVarity.setText(animalVariety[Integer.parseInt(animalDataDto.getAnimalVariety().toString().substring(2, 3))]);

        Glide.with(mContext).load(generateOssUrl(animalDataDto.getNosKey())).into(viewHolder.cardImgView);
    }

    @Override
    public int getItemCount() {
        return animalDataDtoList.size();
    }

    private String generateOssUrl(String ossKey) {
        return String.format(urlFormat, OssConstant.OSS_BUCKET_B_ULTRANSONIC, OssConstant.OSS_END_POINT, ossKey);
    }
}
