package com.rjhc.credit.information.service.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Area
 * @Description: TODO
 * @Author grx
 * @Date 2021/1/11
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Area {
    /**
     * 地区一级编码
     */
    private String areaNoId1;
    /**
     * 地区一级名称
     */
    private String areaDscr1;
    /**
     *地区二级编码
     */
    private String areaNoId2;
    /**
     *地区二级名称
     */
    private String areaDscr2;
    /**
     * 地区编码
     */
    private String areaNoId;
    /**
     * 地区名称
     */
    private String areaDscr;
    /**
     * 地区等级
     */
    private String level;
}
