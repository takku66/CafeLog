package com.cafelog.aspect;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cafelog.entity.CafeLogUser;
import com.cafelog.repository.UserSessionRepository;
import com.cafelog.service.CafeLogService;
import com.cafelog.service.OAuthService;

import lombok.RequiredArgsConstructor;

@Component
@Aspect
@RequiredArgsConstructor
public class CafeLogAspect {
    final private String EXEC_ALL_CONTROLLER = "execution(* *..*.*Controller.*(..))";
    private static final Logger logger = LoggerFactory.getLogger(CafeLogAspect.class);

    private final UserSessionRepository userSession;
    private final OAuthService oauthService;
    private final CafeLogService cafeLogService;

    @Before(EXEC_ALL_CONTROLLER)
    public void loggingBeforeMethod(JoinPoint jp){
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String httpMethod = httpServletRequest.getMethod();

        StringBuilder info = new StringBuilder();
        info.append("URI=" + ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()); // URI
        info.append(", METHOD=" + httpMethod); // METHOD

        StringBuilder debug = new StringBuilder();
        debug.append(info.toString());
        debug.append(", ClassName=" + jp.getSignature().getDeclaringType().getSimpleName()); // クラス名
        debug.append(", MethodName=" + jp.getSignature().getName()); // メソッド名
        debug.append(", Arg Value="); // 引数の値
        Object[] argsValue = jp.getArgs(); 
        for(int i = 0; i < argsValue.length; i++){
            if(i == 0){
                debug.append(argsValue[i]);
            }else{
                debug.append(", " + argsValue[i]);
            }
        }

        logger.info(info.toString());
        logger.debug(debug.toString());
    }

    @Around(EXEC_ALL_CONTROLLER)
    public Object forwardLoginIfNotSession(ProceedingJoinPoint jp) {
        

        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        if(uri.indexOf("login") == -1 && uri.indexOf("signup") == -1){
        // 再度認証の必要がある場合は、ログイン画面へ戻す
        if(oauthService.isNotAuthenticated()){
            return "redirect:/login";
        }

            
            // 外部認証済みのユーザー情報を取得する
            Map<String, String> userInfoMap = new HashMap<>();
            userInfoMap = oauthService.getUserInfoMap();

            // セッションが切れていた場合は、認証済みの情報を格納する
            if(userSession.isNotExists()){
                // メールアドレスでユーザー情報を取得する
                String email = userInfoMap.get("email");
                CafeLogUser user = cafeLogService.searchUserByEmail(email);
                // ユーザー情報をセッションに保持する
                cafeLogService.saveSession(user);
            }

        }


        Object rtn = null;
        //指定したクラスの指定したメソッドを実行
        try {
            rtn = jp.proceed();
        } catch (Throwable e) {
            logger.error("", e.getMessage());
            e.printStackTrace();
        }
        return rtn;

    }


    @AfterThrowing(value = EXEC_ALL_CONTROLLER, throwing = "e")
    public void doRecoveryActions(Throwable e) {
        logger.error("", e.getMessage());
        e.printStackTrace();
    }
}
