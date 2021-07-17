package com.zp.blog.aspect;

import com.zp.blog.domain.SysLog;
import com.zp.blog.domain.UserInfo;
import com.zp.blog.service.SysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Aspect
@Component
public class BlogAspectLog {

    @Autowired
    private SysLogService sysLogService;

    private Date visitTime;  //访问时间
    private Class clazz;  //访问的类
    private Method method; //访问的方法

    @Pointcut("execution(* com.zp.blog.web.*.*.*(..)) && !execution(* com.zp.blog.web.back.AdminSysLogController.*(..)) && !execution(* com.zp.blog.web.comment.*.*(..))")
    public void pt() {
    }

    @Before("pt()")
    public void doBefore(JoinPoint joinPoint) throws NoSuchMethodException {
        visitTime = new Date ();  //获取访问时间
        clazz = joinPoint.getTarget ().getClass ();  //获取访问的类
        String methodName = joinPoint.getSignature ().getName ();  //获取访问的方法的名称
        //获取方法参数
        Object[] args = joinPoint.getArgs ();
        //通过反射获取Method
        if (args == null || args.length == 0) {
            method = clazz.getMethod (methodName); //获取无参的方法
        } else {
            Class[] classes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                //Model 类型的参数注入的是BindingAwareModelMap类，这个类是Model接口实现类ExtendedModelMap类的子类
                if (args[i] instanceof BindingAwareModelMap) {
                    //将子类BindingAwareModelMap强转为父类ExtendedModelMap，这个类是Model接口的实现类  clazz.getMethod (methodName, classes); 就不会出现类型转换异常
                    classes[i] = Model.class;
                    continue;
                }
                if (args[i] instanceof RedirectAttributesModelMap) {
                    classes[i] = RedirectAttributes.class;
                    continue;
                }
                if (args[i] instanceof BeanPropertyBindingResult) {
                    classes[i] = BindingResult.class;
                    continue;
                }
                if (args[i] instanceof ArrayList) {
                    classes[i] = List.class;
                    continue;
                }

                classes[i] = args[i].getClass ();
            }
            method = clazz.getMethod (methodName, classes);

        }
    }

    //后置通知   主要获取日志中其它信息，时长、ip、url...
    @AfterReturning("pt()")
    public void doAfter() {
        //获取url -->  /orders/findAll.do
        String url = "";
        if (clazz != null && method != null) {
            //1.获取类上 @RequestMapping("/product") 注解中的value值
            RequestMapping clazzAnnotation = (RequestMapping) clazz.getAnnotation (RequestMapping.class);
            if (clazzAnnotation != null) {
                String[] classValue = clazzAnnotation.value ();
                //2.获取方法上 @RequestMapping("/findAll.do") 注解中的value值
                RequestMapping methodAnnotation = method.getAnnotation (RequestMapping.class);
                if (methodAnnotation != null) {
                    String[] methodValue = methodAnnotation.value ();
                    //3.获取URL
                    if (classValue[0] != null && methodValue[0] != null) {
                        url = classValue[0] + methodValue[0];
                    }

//                    String uri = request.getRequestURI ();

                    //获取执行时间
                    Integer executionTime = Math.toIntExact (new Date ().getTime () - visitTime.getTime ());

                    //获取ip  需要request获取 --》 获取request需要在web.xml中配置Listener  RequestContextListener类 在注入HttpServletRequest
                    //获取request对象
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes ();
                    HttpServletRequest request = attributes.getRequest ();
                    String ip = request.getRemoteAddr ();

                    //获取操作者用户名(两种方式)  1.从上下文中获取登录的对象   2.也可以从request.getSession中获取
                    SecurityContext context = SecurityContextHolder.getContext ();
//                    SecurityContext context = (SecurityContext) request.getSession ().getAttribute ("SPRING_SECURITY_CONTEXT");
                    Object principal = context.getAuthentication ().getPrincipal ();
                    String nickname = "";
                    if (principal instanceof UserInfo) {
                        UserInfo userInfo = (UserInfo) context.getAuthentication ().getPrincipal ();
                        nickname = userInfo.getNickname ();
                    } else {
                        nickname = "还没登录";
                    }


                    //封装日志类 SysLog
                    SysLog sysLog = new SysLog ();
                    sysLog.setId (UUID.randomUUID ().toString ());
                    sysLog.setVisitTime (visitTime);
                    sysLog.setExecutionTime (executionTime);
                    sysLog.setIp (ip);
                    sysLog.setUrl (url);
                    sysLog.setNickname (nickname);
                    sysLog.setMethod ("[类名]" + clazz.getName () + "[方法名]" + method.getName ());

                    //调用service
                    sysLogService.save (sysLog);
                }

            }

        }
    }

}



