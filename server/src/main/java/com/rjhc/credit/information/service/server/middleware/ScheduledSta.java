package com.rjhc.credit.information.service.server.middleware;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.rjhc.credit.information.service.api.model.dto.PoorHouseholdsInformationDto;
import com.rjhc.credit.information.service.api.model.dto.StatisticalAnalysisDto;
import com.rjhc.credit.information.service.server.common.convertor.PoorHouseholdsInformationConvertor;
import com.rjhc.credit.information.service.server.dao.dataobject.WaringPoor;
import com.rjhc.credit.information.service.server.dao.mapper.WaringPoorMapper;
import com.rjhc.credit.information.service.server.service.StatisticalAnalysisService;
import com.rjhc.matrix.framework.core.util.DateUtils;
import com.rjhc.matrix.framework.core.util.RandomUtil;
import io.swagger.models.auth.In;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * @ClassName ScheduledSta
 * @Description: 定时任务界面
 * @Author grx
 * @Date 2020/9/18
 * @Version V1.0
 **/
@Slf4j
@Component
public class ScheduledSta{
    @Autowired
    private scheduledDateAsync scheduledDate;


    @Scheduled(cron="${scheduledTime.msg}")
//    @Scheduled(cron="0/5 * * * * ?")
    public void getStaFile() throws Exception {
        log.info("定时任务开始执行");
        scheduledDate.scheduledDate();
        log.info("定时任务执行完成");
    }





}
