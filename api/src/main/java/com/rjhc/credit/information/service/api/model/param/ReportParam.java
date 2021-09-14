package com.rjhc.credit.information.service.api.model.param;

import com.rjhc.credit.information.service.api.model.dto.RegionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ReportParam
 * @Description: 报表传输参数
 * @Author grx
 * @Date 2020/12/1
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportParam {
    //查询年份
    private String years;
    //查询类型
    private String type;
    //排序规则
    private String queryLike;
    //贷款用途
    private String loanPurpose;
    //贷款种类
    private String loanVarietie;
    //地区查询
    private String regionId;
    private String tableName;
    private String bankAddress;
    private String orderLike;
    private List<String> regionDtos;
    private String level;

    private List<String> uploadList;

}
