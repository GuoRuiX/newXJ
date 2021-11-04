package com.rjhc.credit.information.service.server.middleware;

import com.rjhc.credit.information.service.api.model.dto.Area;
import com.rjhc.matrix.framework.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Future;

/**
 * @ClassName GetXiAnData
 * @Description: 获取西安的数据
 * @Author grx
 * @Date 2020/11/30
 * @Version V1.0
 **/
@Component
@Slf4j
@EnableAsync
public class GetXiAnData {
    @Value("${spring.datasource.druid.driver-class-name}")
    private String type;
    @Value("${xa.url}")
    private String url;
    @Value("${xa.username}")
    private String userName;
    @Value("${xa.password}")
    private String password;
    @Value("${xaOrg.url}")
    private String urlOrg;
    @Value("${xaOrg.username}")
    private String userNameOrg;
    @Value("${xaOrg.password}")
    private String passwordOrg;
    /**
     * 功能描述：
     * 〈根据当前用户获取当前登陆人机构信息〉
     * @Author: grx
     * @Date: 下午4:01 2020/12/14
     * @param id
     * @return: java.util.Map<java.lang.String,java.lang.String>
     */
    public Map<String,Object> getUserandOrg(String id) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Class.forName(type);
        Connection connection = DriverManager.getConnection(url, userName, password);
        String userSql="SELECT * FROM t_sc_login_user where LOGIN_ID="+"'"+id+"'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(userSql);
        if(rs.next()){
            String loginId = rs.getString("LOGIN_ID");
            String loginName = rs.getString("LOGIN_NAME");
            String userType = rs.getString("USER_TYPE");
            String pbcCode = rs.getString("PBC_CODE");
            map.put("loginId",loginId);
            map.put("loginName",loginName);
            map.put("userType",userType);
            map.put("pbcCode",pbcCode);
        }
        connection.close();
        return map;
    }
    public Map<String,Object> getUserandUserName(String userName1) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Class.forName(type);
        Connection connection = DriverManager.getConnection(url, userName, password);
        String userSql="SELECT * FROM t_sc_login_user where LOGIN_NAME="+"'"+userName1+"'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(userSql);
        if(rs.next()){
            String loginId = rs.getString("LOGIN_ID");
            String loginName = rs.getString("LOGIN_NAME");
            String passWord = rs.getString("PASS_WORD");
            String userType = rs.getString("USER_TYPE");
            String pbcCode = rs.getString("PBC_CODE");
            map.put("loginId",loginId);
            map.put("loginName",loginName);
            map.put("passWord",passWord);
            map.put("userType",userType);
            map.put("pbcCode",pbcCode);
        }
        connection.close();
        return map;
    }
    /**
     * 功能描述：
     * 〈根据当前登陆人获取当前机构〉
     * @Author: grx
     * @Date: 下午4:12 2020/12/14
     * @param orgId
     * @return: java.util.Map<java.lang.String,java.lang.String>
     */
    public Map<String,String> getOrg(String orgId) throws Exception {
        Map<String, String> map = new HashMap<>();
        Class.forName(type);
        Connection connection = DriverManager.getConnection(urlOrg, userNameOrg, passwordOrg);
        String userSql="SELECT * FROM t_org_biz_lvl where ORG_ID="+"'"+orgId+"'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(userSql);
        if(rs.next()){
            String orgId1 = rs.getString("ORG_ID");
            String orgDscr = rs.getString("ORG_DSCR");
            String orgLvl = rs.getString("ORG_LVL");
            map.put("orgId",orgId1);
            map.put("orgDscr",orgDscr);
            map.put("orgLvl",orgLvl);
        }
        connection.close();
        return map;
    }
    /**
     * 功能描述：
     * 〈获取当前机构下的子集机构〉
     * @Author: grx
     * @Date: 下午4:25 2020/12/14
     * @param orgId
     * @param lel
     * @return: java.util.List<java.lang.String>
     */
    public List<String> getOrgLevel(String orgId,String lel) throws Exception {
        List<String> strings = new ArrayList<>();
        Class.forName(type);
        Connection connection = DriverManager.getConnection(urlOrg, userNameOrg, passwordOrg);
        String orgLevel="ORG_ID_"+lel;
        String lelSql="SELECT * FROM t_org_biz_lvl where "+orgLevel+"="+"'"+orgId+"'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(lelSql);
        while (rs.next()){
            strings.add(rs.getString("ORG_ID"));
        }
        connection.close();
        return strings;
    }

    /**
     * 功能描述：
     * 〈获取地区数据〉
     * @Author: grx
     * @Date: 下午5:11 2021/1/11
     * @param
     * @return: java.util.List<com.rjhc.credit.information.service.api.model.dto.Area>
     */
    public List<Area> getArea() throws Exception {
        List<Area> areaList = new ArrayList<>();
        Class.forName(type);
        Connection connection = DriverManager.getConnection(urlOrg, userNameOrg, passwordOrg);
        String areaAll="select * from t_area_code";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(areaAll);
        while (rs.next()){
            Area area = new Area();
            area.setAreaNoId1(rs.getString("AREA_NO_ID_1"));
            area.setAreaDscr1(rs.getString("AREA_DSCR_1"));
            area.setAreaNoId2(rs.getString("AREA_NO_ID_2"));
            area.setAreaDscr2(rs.getString("AREA_DSCR_2"));
            area.setAreaNoId(rs.getString("AREA_NO_ID"));
            area.setAreaDscr(rs.getString("AREA_DSCR"));
            area.setLevel(rs.getString("LEVEL"));
            areaList.add(area);
        }
        connection.close();
        return areaList;
    }

    public List<String> getAreaByLevel(String areaNoId,String level) throws Exception {
        List<String> areaList = new ArrayList<>();
        Class.forName(type);
        Connection connection = DriverManager.getConnection(urlOrg, userNameOrg, passwordOrg);
        String attribute = null;
        if(!"3".equals(level)){
            attribute="AREA_NO_ID_"+level;
        }else {
            attribute="AREA_NO_ID";
        }
        String areaAll="select AREA_DSCR from t_area_code where "+attribute+"='"+areaNoId+"'  ";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(areaAll);
        while (rs.next()){
            areaList.add(rs.getString("AREA_DSCR"));
        }
        connection.close();
        return areaList;
    }
    /**
     * 根据金融机构id获取当前机构数据
     */
    public Map<String,String> getDeptOrg(String orgId) throws Exception {
        Map<String, String> map = new HashMap<>();
        Class.forName(type);
        Connection connection = DriverManager.getConnection(urlOrg, userNameOrg, passwordOrg);
        String userSql="SELECT * FROM t_org_biz_lvl where ORG_ID="+"'"+orgId+"'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(userSql);
        if(rs.next()){
            String orgId1 = rs.getString("ORG_ID");
            String orgDscr = rs.getString("ORG_DSCR");
            String orgLvl = rs.getString("LEVEL");
            map.put("orgId",orgId1);
            map.put("orgDscr",orgDscr);
            map.put("orgLvl",orgLvl);
        }
        connection.close();
        return map;
    }
    /**
     * 根据1级金融机构获取当前机构下的所有机构
     */
    @Async
    public Future<HashSet<String>> getDeptOrgBylevelOne(String orgName) throws Exception {
        HashSet<String> list = new HashSet<>();
        Class.forName(type);
        Connection connection = DriverManager.getConnection(urlOrg, userNameOrg, passwordOrg);
        String level = getLevel(orgName);
        if(StringUtil.isEmpty(level)){
            level = "1";
        }
        String orgLevel = "ORG_DSCR_"+level;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * FROM t_org_biz_lvl where ").append(orgLevel).append(" = ").append("'").append(orgName).append("'");
        String userSql="SELECT * FROM t_org_biz_lvl where "+orgLevel+" ="+"'"+orgName+"'";
        log.info(userSql);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(stringBuffer.toString());
        while (rs.next()){
            String orgId1 = rs.getString("ORG_ID");
            list.add(orgId1);
        }
        log.info("当前获取机构："+list);
        connection.close();
        return new AsyncResult<>(list);
    }
    public HashSet<String> getDeptOrgBylevelsel(String orgName) throws Exception {
        HashSet<String> list = new HashSet<>();
        Class.forName(type);
        Connection connection = DriverManager.getConnection(urlOrg, userNameOrg, passwordOrg);
        String level = getLevel(orgName);
        if(StringUtil.isEmpty(level)){
            level = "1";
        }
        String orgLevel = "ORG_DSCR_"+level;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * FROM t_org_biz_lvl where ").append(orgLevel).append(" = ").append("'").append(orgName).append("'");
        String userSql="SELECT * FROM t_org_biz_lvl where "+orgLevel+" ="+"'"+orgName+"'";
        log.info(userSql);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(stringBuffer.toString());
        while (rs.next()){
            String orgId1 = rs.getString("ORG_ID");
            list.add(orgId1);
        }
        log.info("当前获取机构："+list);
        connection.close();
        return list;
    }


    public String getLevel(String orgName) throws Exception {
        Class.forName(type);
        Connection connection = DriverManager.getConnection(urlOrg, userNameOrg, passwordOrg);
        //先根据机构名称获取机构信息
        String getLevel="SELECT * FROM t_org_biz_lvl where ORG_DSCR="+"'"+orgName+"'";
        log.info(getLevel);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(getLevel);
        String level = null;
        if(rs.next()){
            level = rs.getString("LEVEL");
        }
        log.info("当前获取机构的等级为："+level);
        connection.close();
        return level;
    }
    /**
     * 功能描述：
     * 〈农信社查询〉
     * @Author: grx
     * @Date: 下午5:22 2021/9/3
     * @param orgName
     * @return: java.lang.String
     */
    @Async
    public Future<HashSet<String>> getLevelNXSAsync(String orgName) throws Exception {
        HashSet<String> list = new HashSet<>();
        Class.forName(type);
        Connection connection = DriverManager.getConnection(urlOrg, userNameOrg, passwordOrg);
        //先根据机构名称获取机构信息
        String getLevel="SELECT * FROM t_org_biz_lvl where ORG_DSCR="+"'"+orgName+"'";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * FROM t_org_biz_lvl where ORG_DSCR").append(" like '%").append("农村信用合作联社").append("%'")
                .append(" or ").append("ORG_DSCR ").append("'%").append("农村商业银行").append("%'");
        log.info(stringBuffer.toString());
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(stringBuffer.toString());
        String level = null;
        while (rs.next()){
            String orgId1 = rs.getString("ORG_ID");
            list.add(orgId1);
        }
        log.info("当前获取机构："+list);
        connection.close();
        return new AsyncResult<>(list);
    }
    public HashSet<String> getLevelNXS(String orgName) throws Exception {
        HashSet<String> list = new HashSet<>();
        Class.forName(type);
        Connection connection = DriverManager.getConnection(urlOrg, userNameOrg, passwordOrg);
        //先根据机构名称获取机构信息
        String getLevel="SELECT * FROM t_org_biz_lvl where ORG_DSCR="+"'"+orgName+"'";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * FROM t_org_biz_lvl where ORG_DSCR").append(" like '%").append("农村信用合作联社").append("%'")
        .append(" or ").append("ORG_DSCR ").append("like '%").append("农村商业银行").append("%'");
        log.info(stringBuffer.toString());
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(stringBuffer.toString());
        String level = null;
        while (rs.next()){
            String orgId1 = rs.getString("ORG_ID");
            list.add(orgId1);
        }
        log.info("当前获取机构："+list);
        connection.close();
        return list;
    }


    public String getUserRelo(String userName) throws Exception {
        Class.forName(type);
        Connection connection = DriverManager.getConnection(urlOrg, userNameOrg, passwordOrg);
        //根据当前用户获取当前用户登录角色信息
        String sql ="SELECT * FROM t_sc_role where ROLE_ID = (SELECT ROLE_ID FROM t_sc_role_member where MEMBER_ID = '"+userName+"')";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        String roleName=null;
        if(rs.next()){
             roleName = rs.getString("ROLE_NAME");

        }
        connection.close();
        return roleName;
    }


}
