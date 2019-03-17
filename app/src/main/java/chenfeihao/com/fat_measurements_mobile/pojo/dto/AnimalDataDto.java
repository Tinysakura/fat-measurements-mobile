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
}
