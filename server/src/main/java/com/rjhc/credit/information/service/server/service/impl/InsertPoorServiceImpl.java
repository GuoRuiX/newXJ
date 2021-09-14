package com.rjhc.credit.information.service.server.service.impl;

import com.rjhc.credit.information.service.server.dao.dataobject.PoorHouseholdsInformation;
import com.rjhc.credit.information.service.server.dao.dataobject.Region;
import com.rjhc.credit.information.service.server.dao.mapper.PoorHouseholdsInformationMapper;
import com.rjhc.credit.information.service.server.service.InsertPoorService;
import com.rjhc.credit.information.service.server.service.RegionService;
import com.rjhc.matrix.framework.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName InsertPoorServiceImpl
 * @Description: TODO
 * @Author grx
 * @Date 2021/3/5
 * @Version V1.0
 **/
@Service
@Slf4j
public class InsertPoorServiceImpl implements InsertPoorService {
    @Value("${spring.datasource.druid.url}")
    private String DB_URL;
    @Value("${spring.datasource.druid.username}")
    private String USER;
    @Value("${spring.datasource.druid.password}")
    private String PASSWORD;
    @Autowired
    private RegionService regionService;
    @Resource
    private PoorHouseholdsInformationMapper insertPoorMapper;
    @Override
    public void savePoor() throws Exception {

        List<String> administrativeVillage = new ArrayList<>();
        administrativeVillage.add("行");
        administrativeVillage.add("政");
        administrativeVillage.add("村");
        List<String> Township = new ArrayList<>();
        Township.add("乡");
        Township.add("镇");
        List<String> nameList = new ArrayList<>();
        nameList.add("张三");
        nameList.add("李四");
        nameList.add("王武");
        nameList.add("赵六");
        nameList.add("赵七");
        List<String> address = new ArrayList<>();
        address.add("家庭");
        address.add("住址");
        List<String> poorHouseholdsCode = new ArrayList<>();
        poorHouseholdsCode.add("贫困户");
        poorHouseholdsCode.add("编码");
        List<String> idCardType = new ArrayList<>();
        idCardType.add("SFZ");
        List<String> health = new ArrayList<>();
        health.add("健康");
        List<String> povertySign = new ArrayList<>();
        povertySign.add("已脱贫");
        povertySign.add("未脱贫");
        List<String> relationShip = new ArrayList<>();
        relationShip.add("FZ");
        List<String> gender = new ArrayList<>();
        gender.add("男");
        gender.add("女");
        List<String> nation = new ArrayList<>();
        nation.add("汉");
        nation.add("少数名族");
        Random random = new Random();
        AtomicInteger count = new AtomicInteger(1);
        PreparedStatement pstmt = null;
        Connection conn = null;
//        try{
        conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        boolean closed = conn.isClosed();
        conn.setAutoCommit(false);
        List<Region> regions = regionService.selectAll();

//    INSERT INTO credit_test.poor_households_Information(id, years, prefecture, counties, township, administrative_village, administrative_village_code, householdername, id_card, address, poor_households_code, population_code, poor_households_attribute, id_card_type, health, povertysign, familysize, relationship, gender, age, nation, landarea, woodlandarea, income, synchronization_date, image) VALUES ('1', '2021', '乌鲁木齐', '乌鲁木齐市', '乌鲁木齐县', '新政村', '2002', '张三', '14242222', '家庭住址', '200', '300', '贫苦户编码', 'SFZ', '健康', '未脱贫', 4, 'FZ', '男', 22, '汉', 22.00, 22.00, 22.00, '2021-03-05 09:51:10', NULL);
         pstmt = conn.prepareStatement("INSERT INTO poor_households_Information(id, years, prefecture, counties, township, administrative_village_code, householdername) " +
                 "VALUES (?,?,?,?,?,?,?);");
        int a=0;
        ArrayList<PoorHouseholdsInformation> list = new ArrayList<PoorHouseholdsInformation>();
        for (int i = 0; i < 1000000; i++) {
            PoorHouseholdsInformation poorHouseholdsInformation = new PoorHouseholdsInformation();
            poorHouseholdsInformation.setId(RandomUtil.UUID36());
            poorHouseholdsInformation.setYears(String.valueOf(random.nextInt(2000)));
            poorHouseholdsInformation.setPrefecture(regions.get(random.nextInt(regions.size())).getRegionName());
            poorHouseholdsInformation.setCounties(regions.get(random.nextInt(regions.size())).getRegionName());
            poorHouseholdsInformation.setTownship(Township.get(random.nextInt(Township.size())));
            poorHouseholdsInformation.setAdministrativeVillage(administrativeVillage.get(random.nextInt(administrativeVillage.size())));
            poorHouseholdsInformation.setAdministrativeVillageCode(String.valueOf(random.nextInt(200)));
            poorHouseholdsInformation.setHouseholderName(nameList.get(random.nextInt(nameList.size())));
            poorHouseholdsInformation.setIdCard(String.valueOf(random.nextInt(2000000000)));
            poorHouseholdsInformation.setAddress(address.get(random.nextInt(address.size())));
            poorHouseholdsInformation.setPoorHouseholdsCode(String.valueOf(random.nextInt(200)));
            poorHouseholdsInformation.setPopulationCode(String.valueOf(random.nextInt(200)));
            poorHouseholdsInformation.setPoorHouseholdsAttribute("ZF");
            poorHouseholdsInformation.setIdCardType(idCardType.get(random.nextInt(idCardType.size())));
            poorHouseholdsInformation.setHealth(health.get(random.nextInt(health.size())));
            poorHouseholdsInformation.setPovertySign(povertySign.get(random.nextInt(povertySign.size())));
            poorHouseholdsInformation.setFamilySize(random.nextInt(10));
            poorHouseholdsInformation.setRelationShip(relationShip.get(random.nextInt(relationShip.size())));
            poorHouseholdsInformation.setGender(gender.get(random.nextInt(gender.size())));
            poorHouseholdsInformation.setAge(random.nextInt(200));
            poorHouseholdsInformation.setNation(nation.get(random.nextInt(nation.size())));
            poorHouseholdsInformation.setLandarea(new BigDecimal(random.nextInt(200)));
            poorHouseholdsInformation.setWoodlandarea(new BigDecimal(random.nextInt(200)));
            poorHouseholdsInformation.setIncome(new BigDecimal(random.nextInt(200)));
            poorHouseholdsInformation.setSynchronizationDate(new Date());

            list.add(poorHouseholdsInformation);
            if(list.size() % 50000 == 0){
                a++;
                log.info(""+i);
                insertPoorMapper.savePoor(list);
                list.clear();
            }




         /*   String s = nameList.get(random.nextInt(nameList.size()));
            log.info(s);
            pstmt.setString(1, RandomUtil.UUID36());
            pstmt.setString(2, poorHouseholdsInformation.getYears());
            pstmt.setString(3, poorHouseholdsInformation.getPrefecture());
            pstmt.setString(4, regions.get(random.nextInt(regions.size())).getRegionName());
            pstmt.setString(5, Township.get(random.nextInt(Township.size())));
            pstmt.setString(6, String.valueOf(random.nextInt(2000)));
            pstmt.setString(7, s);
            pstmt.addBatch();
            if (count.get() % 10 == 0) {// 当增加了10000个批处理的时候再提交
                pstmt.executeBatch();// 执行批处理
                conn.commit();
                pstmt.clearBatch();
            }
            count.getAndIncrement();
            log.info(count.toString());*/
        }
      /*  pstmt.executeBatch();// 执行批处理
        conn.commit();
        pstmt.close();*/

    }
}
