package chenfeihao.com.fat_measurements_mobile.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import chenfeihao.com.fat_measurements_mobile.R;
import chenfeihao.com.fat_measurements_mobile.constant.AnimalConstant;
import chenfeihao.com.fat_measurements_mobile.custom.layout.TitleLayout;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.AnimalResultDto;
import chenfeihao.com.fat_measurements_mobile.util.StringUtil;
import de.hdodenhof.circleimageview.CircleImageView;

public class MeasureResultActivity extends AppCompatActivity {

    /**
     * UI
     */
    private TitleLayout customTitleLayout;

    private TextView measureResultTitleTextView;

    private TextView fatBalanceRankTextView;

    private CircleImageView measureResultImg;

    private TextView suggestionTextView;

    private TextView backFatOriginalTextView;

    private TextView backFatReviseTextView;

    private TextView musculiOculiOriginalTextView;

    private TextView musculiOculiReviseTextView;

    private TextView leanRateTextView;

    private TextView fatRateTextView;

    /**
     * data
     */
    private static final String[] titleArray = {"猪猪的肉质下降了，饲养员要注意了！", "猪猪的肉质正常，再接再厉！", "猪猪的肉质非常棒，快来分享养育心得吧！"};

    private static final String[] fatBalanceRankArray = {"Rank1", "Rank2", "Rank3"};

    private static final String[] suggestionArray = {"杜仲能使猪肉更加鲜美。据日本的研究报道，中药杜仲，能促进肌肉纤维的发育及提高肌肉中的胶原蛋白质的含量。因此，将杜仲磨成细颗粒状或粉状，在育肥猪的日粮中添加0.2％～0.3％，能使猪肉变得更加鲜美，蛋白质含量显著提高，商品等级高，而对肉猪的生长发育无不良影响。",
            "维生素C能使猪肉颜色更鲜艳。据法国农科院的研究表明，在育肥猪的日粮中添加较高水平的维生素C，即100～500毫克/公斤体重，可使猪肉的颜色变得更深，改善猪肉的品质，尤其能改善容易产生酸性肉和苍白肉品种的猪的肉品质。",
            "碳水化合物能使猪肉中脂肪洁白坚硬。据国内资料介绍，饲料中碳水化合物可使猪肉中的脂肪洁白坚硬而品位高。所以，在肉猪育成后期，适当增加饲料中的碳水化合物含量，在农村一般养猪则适当增喂精料，是提高猪肉品质行之有效的方法。",
            "维生素E能使猪肉货架期延长。据法国农科院的研究表明，在育肥猪的日粮中添加10、20和30毫克的维生素E，存在猪肉组织中的维生素 E含量较高，而维生素E可防止猪肉贮藏期间酮体脂肪的亚油酸氧化肉变质，从而能延长猪肉的货架命期。",
            "适时屠宰会使猪肉含水量和脂肪含量适中。根据我国黑龙江畜牧研究所试验研究结果表明，大体重的猪屠宰后，肥肉多而瘦肉少，不符合当前市场的需要，商品等级低。体重过小的猪屠宰后，肌肉含水量大，脂肪含量少而肉质差，不受消费者欢迎，商品等级也低。肉猪达90公斤体重时屠宰，不仅瘦肉率高，肉水分含量、脂肪含量适中而肉质好，而且饲料利用率和经济效益也最高.",
            "主人的猪猪已经很健康啦，快去向其他小伙伴分享养育心得吧。"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_result);

        initUI();
    }

    private void initUI() {
        initCustomTitleLayout();

        measureResultTitleTextView = findViewById(R.id.measure_result_title);
        fatBalanceRankTextView = findViewById(R.id.measure_result_fat_balance_rank);
        measureResultImg = findViewById(R.id.measure_result_img);
        suggestionTextView = findViewById(R.id.measure_result_suggestion);
        backFatOriginalTextView = findViewById(R.id.measure_result_back_fat_original);
        backFatReviseTextView = findViewById(R.id.measure_result_back_fat_revise);
        musculiOculiOriginalTextView = findViewById(R.id.measure_result_musculi_oculi_original);
        musculiOculiReviseTextView = findViewById(R.id.measure_result_musculi_oculi_revise);
        leanRateTextView = findViewById(R.id.measure_result_lean_rate);
        fatRateTextView = findViewById(R.id.measure_result_fat_rate);

        renderData();
    }

    private void renderData() {
        Intent intent = getIntent();
        String resultJson = intent.getStringExtra("measure_finish_result");

        if (StringUtil.isEmpty(resultJson)) {
            return;
        }

        AnimalResultDto measureResult = JSON.parseObject(resultJson, AnimalResultDto.class);

        /**
         * 可以直接填充的值
         */
        backFatOriginalTextView.setText(Html.fromHtml(getResources().getString(R.string.measure_result_format, "背肌厚度:", measureResult.getBackFat())));
        backFatReviseTextView.setText(Html.fromHtml(getResources().getString(R.string.measure_result_format, "背肌厚度校正值:", measureResult.getBackFatRevise())));

        musculiOculiOriginalTextView.setText(Html.fromHtml(getResources().getString(R.string.measure_result_format, "眼肌面积:", measureResult.getMusculiOculi())));
        musculiOculiReviseTextView.setText(Html.fromHtml(getResources().getString(R.string.measure_result_format, "眼肌面积校正值:", measureResult.getMusculiOculiRevise())));

        backFatOriginalTextView.setText(Html.fromHtml(getResources().getString(R.string.measure_result_format, "瘦肉率:", measureResult.getLeanRate())));
        backFatReviseTextView.setText(Html.fromHtml(getResources().getString(R.string.measure_result_format, "肌间脂肪比", measureResult.getFatRate())));

        /**
         * 需要处理的值
         */
        int rank = measureResult.getFatBalanceRank();
        measureResultTitleTextView.setText(titleArray[rank - 1]);
        fatBalanceRankTextView.setText(fatBalanceRankArray[rank - 1]);

        renderSuggestion(rank);

        Integer sex = intent.getIntExtra("sex", AnimalConstant.AnimalSexEnum.MALE.getCode());
        renderImg(rank, sex);
    }

    private void renderImg(int rank, Integer sex) {
        switch (rank) {
            case 1:
                Glide.with(this).load(R.mipmap.rank1).into(measureResultImg);
                break;
            case 2:
                if (AnimalConstant.AnimalSexEnum.MALE.getCode().equals(sex)) {
                    Glide.with(this).load(R.mipmap.rank2_male).into(measureResultImg);
                } else if (AnimalConstant.AnimalSexEnum.FEMALE.getCode().equals(sex)) {
                    Glide.with(this).load(R.mipmap.rank2_female).into(measureResultImg);
                }
                break;
            case 3:
                Glide.with(this).load(R.mipmap.rank3).into(measureResultImg);
                break;
        }
    }

    private void renderSuggestion(int rank) {
        if (rank == 3) {
            suggestionTextView.setText(suggestionArray[5]);
        } else {
            int random = (int)(0 + Math.random() * 5);
            suggestionTextView.setText(suggestionArray[random]);
        }
    }

    private void initCustomTitleLayout() {
        customTitleLayout = findViewById(R.id.measure_custom_title);
        customTitleLayout.setTitle("测量结果");
    }
}

