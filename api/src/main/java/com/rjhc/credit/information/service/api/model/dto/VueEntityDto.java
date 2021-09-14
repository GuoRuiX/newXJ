package com.rjhc.credit.information.service.api.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName VueEntityDto
 * @Description: TODO
 * @Author grx
 * @Date 2020/8/20
 * @Version V1.0
 **/
@Data
public class VueEntityDto {

    private String path;
    private String name;
    private Map<String,String> meta;
    private List<Map<String,Object>> children;
}
