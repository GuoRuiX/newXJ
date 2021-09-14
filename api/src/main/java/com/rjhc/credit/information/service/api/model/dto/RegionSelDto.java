package com.rjhc.credit.information.service.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName RegionSelDto
 * @Description: TODO
 * @Author grx
 * @Date 2021/1/11
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionSelDto {
    private String value;
    private String label;
    private String parentId;
    private List<RegionSelDto> children;
}
