package com.rjhc.credit.information.service.api;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName DataReportingInterface
 * @Description: 数据上报
 * @Author grx
 * @Date 2020/8/13
 * @Version V1.0
 **/
@RequestMapping("/dateReporting")
@Api(value = "数据上报管理", tags = {"数据上报管理"})
public interface DataReportingInterface {
}
