package com.rjhc.credit.information.service.server.middleware.log;

import com.alibaba.fastjson.JSON;
import com.rjhc.credit.information.service.server.dao.dataobject.OperationLog;
import com.rjhc.credit.information.service.server.dao.mapper.OperationLogMapper;
import com.rjhc.credit.information.service.server.dao.mapper.PoorHouseholdsInformationMapper;
import com.rjhc.credit.information.service.server.middleware.GetXiAnData;
import com.rjhc.matrix.framework.core.util.RandomUtil;
import com.rjhc.matrix.framework.core.util.RequestUtil;
import com.rjhc.matrix.framework.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName LogAspect
 * @Description: 日志切面编程
 * @Author grx
 * @Date 2020/8/21
 * @Version V1.0
 **/
@Aspect
@Order(5)  //值越小,越先加载
@Component
@Slf4j
public class LogAspect {
    @Resource
    private OperationLogMapper operationLogMapper;
    @Autowired
    private GetXiAnData getXiAnData;
    @Autowired
    private RequestUtil requestUtil;
    @Value("${analysis.token}")
    private String token;
    @Resource
    private PoorHouseholdsInformationMapper poorHouseholdsInformationMapper;
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    @Pointcut("@annotation(com.rjhc.credit.information.service.server.middleware.log.OptionalLog)")
    public void webLog(){}

    @Before("webLog()")//切换后置通知
    public Object around(JoinPoint pjp) throws Throwable{
//        Object proceed = pjp.proceed();
        OperationLog logs = new OperationLog();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String userNme = null;
        /*try {*/
            //从session 获取用户信息
            String username = requestUtil.getAccountHeader();
            String userIdHeader = requestUtil.getUserIdHeader();
            if(!StringUtil.isEmpty(userIdHeader)){
                log.info("获取到的当前用户为："+username);
                Map<String, Object> userandOrg = getXiAnData.getUserandOrg(userIdHeader);
                logs.setBankId(userandOrg.get("pbcCode").toString());
            }
            //根据用户编码获取当前用户信息
            logs.setUserName(username);

      /*  }catch (Exception e){
            log.error("获取用户数据错误");
        }*/
        //获取拦截的实体类
        Object target = pjp.getTarget();
        //获取拦截的方法名
        String methodName = pjp.getSignature().getName();
        //获取拦截的方法参数
        Object[] args = pjp.getArgs();
        //获取请求路径
        String actionURL = request.getRequestURI();
        //获取拦截的参数类型
        Signature signature = pjp.getSignature();
        MethodSignature msig = null;
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) signature;
        Class<?>[] parameterTypes = msig.getMethod().getParameterTypes();
        Object object = null;
        // 获得被拦截的方法
        Method method = null;
        try {
            method = target.getClass().getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }
        Map<String, Object> userId=null;
        String toKen = request.getHeader("toKen");

        if (null != method) {
            try {
                //获取方法 枚举
                OptionalLog op = method.getAnnotation(OptionalLog.class);
                //获取注解的model 模块名称
                logs.setModelName(op.module());
                //获取注解的操作名称
                logs.setOperationName(op.operations());
                if(logs.getOperationName().equals("信用报告查询")){
                    //判断当前操作是查询信用报告，存储当前用户所查询的身份证id
                        logs.setIdCard(args[0].toString());
                }
                if(logs.getOperationName().equals("登录")){
                    if(null != args && args.length > 0){
                        //取第一个参数，用户信息在第一个参数里。如果登录的业务代码有修改了，请酌情处理这里
                        Object object1 = args[0];
                        if(null != object1){
                            Map<String,Object> paramMap = JSON.parseObject(JSON.toJSONString(object1));
                            if(null != paramMap && paramMap.size() > 0){
                              String  userName = (String) paramMap.get("userName");
                                //根据当前用户获取机构
                                Map<String, Object> userandUserName = getXiAnData.getUserandUserName(userName);
                                logs.setBankId(userandUserName.get("pbcCode").toString());
                                logs.setUserName(userName);
                            }
                        }
                    }

                }

            }catch (Exception e){
                log.error("未标注日志");
            }

            //接收客户端的参数
            Map<String, String[]> parameterMap = request.getParameterMap();
        }
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        StringBuffer stringBuffer = new StringBuffer();
        for (Object arg : args) {
            stringBuffer.append(arg);
        }
        stringObjectHashMap.put("request",stringBuffer);
        //存贮当前日期
        logs.setOperationDate(new Date());
        logs.setSourceCode(stringObjectHashMap.toString());
        logs.setId(RandomUtil.UUID36());
        if(!StringUtil.isEmpty(logs.getModelName())){
            operationLogMapper.insert(logs);
        }
        return null;
    }
}
