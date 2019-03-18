package chenfeihao.com.fat_measurements_mobile.pojo.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/17
 */
@Data
public class AnimalDataDto {
    private Long id;

    private Long dbCreateTime;

    private Long dbUpdateTime;

    private Long userId;

    private String nosKey;

    private String animalBUltrasound;

    private String animalId;

    private BigDecimal animalWeight;

    private Integer animalSex;

    private Integer animalVariety;

    private Integer animalDraft;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNosKey() {
        return nosKey;
    }

    public void setNosKey(String nosKey) {
        this.nosKey = nosKey;
    }

    public String getAnimalBUltrasound() {
        return animalBUltrasound;
    }

    public void setAnimalBUltrasound(String animalBUltrasound) {
        this.animalBUltrasound = animalBUltrasound;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public BigDecimal getAnimalWeight() {
        return animalWeight;
    }

    public void setAnimalWeight(BigDecimal animalWeight) {
        this.animalWeight = animalWeight;
    }

    public Integer getAnimalSex() {
        return animalSex;
    }

    public void setAnimalSex(Integer animalSex) {
        this.animalSex = animalSex;
    }

    public Integer getAnimalVariety() {
        return animalVariety;
    }

    public void setAnimalVariety(Integer animalVariety) {
        this.animalVariety = animalVariety;
    }

    public Integer getAnimalDraft() {
        return animalDraft;
    }

    public void setAnimalDraft(Integer animalDraft) {
        this.animalDraft = animalDraft;
    }
}
