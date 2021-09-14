package com.rjhc.credit.information.service.api.model.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.EAN;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @ClassName CountyParam
 * @Description: TODO
 * @Author grx
 * @Date 2020/12/23
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountyParam {
    @ApiModelProperty(value = "贫困县")
    private String county;
    @ApiModelProperty(value = "贫困县等级")
    private String countyLevel;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private Date endDate;
    @ApiModelProperty(value = "表名")
    private String tableName;

    private String level;
    private List<String> countyList;


    /**
     * 商行机构查询
     */
    private String deptOrg;

    private HashSet<String> deptList;

}
