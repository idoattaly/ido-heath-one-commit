package com.dchealth.filter;
import com.dchealth.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by heren on 2014/10/13.
 */
public class CorsFilter implements Filter {
    private Map<String,Integer> ipMap = new ConcurrentHashMap<String,Integer>();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        initIpMap();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.setCharacterEncoding("UTF-8");
        Boolean isLegal = judegeRequest((HttpServletRequest)servletRequest);
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Allow-Control-Allow-Methods", "POST,GET,OPTIONS");
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With");
        res.addHeader("Access-Control-Max-Age", "600000");
        res.setCharacterEncoding("UTF-8");
        if(isLegal){
            filterChain.doFilter(servletRequest, res);
        }else{
            throw new ServletException("获取注册短信验证码次数已达上限，请您明天再来");
        }
    }

    public void initIpMap(){
        Runnable runnable = new Runnable() {
            public void run() {
                ipMap.clear();
            }
        };
        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.scheduleAtFixedRate(runnable, 10, 24*60*60, TimeUnit.SECONDS);
    }
    public Boolean judegeRequest(HttpServletRequest request){
        Boolean isLegal = false;
        String ip = getIpAddr(request);
        if(StringUtils.isEmpty(ip)){
            isLegal = true;
        }else{
            if(ipMap.get(ip)!=null){
                Integer number = ipMap.get(ip)+1;
                if(number<21){
                    ipMap.put(ip,number);
                    isLegal = true;
                }
            }else{
                ipMap.put(ip,1);
                isLegal = true;
            }
        }
        return isLegal;
    }
    public String getIpAddr(HttpServletRequest request) {
                String ip = "";
                String uri = request.getRequestURI();
                request.getSession();
                if(!StringUtils.isEmpty(uri) && uri.contains("api/yun-user/get-very-code-by-mobile")){
                    ip = request.getHeader("x-forwarded-for");
                    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                        ip = request.getHeader("Proxy-Client-IP");
                    }
                    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                        ip = request.getHeader("WL-Proxy-Client-IP");
                    }
                    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                        ip = request.getRemoteAddr();
                    }
                }
                return ip;
    }
    @Override
    public void destroy() {

    }
}

