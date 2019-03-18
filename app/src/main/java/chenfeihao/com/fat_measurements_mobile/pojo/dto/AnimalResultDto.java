package chenfeihao.com.fat_measurements_mobile.pojo.dto;

import java.math.BigDecimal;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/17
 */
public class AnimalResultDto {
    private Long id;

    private Long dbCreateTime;

    private Long dbUpdateTime;

    /**
     * 对应动物数据id
     */
    private Long animalDataId;

    /**
     * 背膘厚度
     */
    private BigDecimal backFat;

    /**
     * 背膘厚度校正值
     */
    private BigDecimal backFatRevise;

    /**
     * 眼肌面积
     */
    private BigDecimal musculiOculi;

    /**
     * 眼肌面积校正值
     */
    private BigDecimal musculiOculiRevise;

    /**
     * 瘦肉率
     */
    private BigDecimal leanRate;

    /**
     * 肌间脂肪比
     */
    private BigDecimal fatRate;

    /**
     * 脂肪均匀等级
     */
    private Integer fatBalanceRank;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDbCreateTime() {
        return dbCreateTime;
    }

    public void setDbCreateTime(Long dbCreateTime) {
        this.dbCreateTime = dbCreateTime;
    }

    public Long getDbUpdateTime() {
        return dbUpdateTime;
    }

    public void setDbUpdateTime(Long dbUpdateTime) {
        this.dbUpdateTime = dbUpdateTime;
    }

    public Long getAnimalDataId() {
        return animalDataId;
    }

    public void setAnimalDataId(Long animalDataId) {
        this.animalDataId = animalDataId;
    }

    public BigDecimal getBackFat() {
        return backFat;
    }

    public void setBackFat(BigDecimal backFat) {
        this.backFat = backFat;
    }

    public BigDecimal getBackFatRevise() {
        return backFatRevise;
    }

    public void setBackFatRevise(BigDecimal backFatRevise) {
        this.backFatRevise = backFatRevise;
    }

    public BigDecimal getMusculiOculi() {
        return musculiOculi;
    }

    public void setMusculiOculi(BigDecimal musculiOculi) {
        this.musculiOculi = musculiOculi;
    }

    public BigDecimal getMusculiOculiRevise() {
        return musculiOculiRevise;
    }

    public void setMusculiOculiRevise(BigDecimal musculiOculiRevise) {
        this.musculiOculiRevise = musculiOculiRevise;
    }

    public BigDecimal getLeanRate() {
        return leanRate;
    }

    public void setLeanRate(BigDecimal leanRate) {
        this.leanRate = leanRate;
    }

    public BigDecimal getFatRate() {
        return fatRate;
    }

    public void setFatRate(BigDecimal fatRate) {
        this.fatRate = fatRate;
    }

    public Integer getFatBalanceRank() {
        return fatBalanceRank;
    }

    public void setFatBalanceRank(Integer fatBalanceRank) {
        this.fatBalanceRank = fatBalanceRank;
    }
}