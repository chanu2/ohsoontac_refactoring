package ohsoontac.serverapi.global.security;

import lombok.extern.slf4j.Slf4j;
import ohsoontac.serverapi.global.error.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        String exception = (String)request.getAttribute("exception");
//        ErrorCode errorCode = null;
//
//        if (exception == null) {
//            setResponse(response, errorCode.NO_ERROR_TYPE);
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            System.out.println(response.getStatus()+"");
//            logger.error(authException.getMessage());
//        }
//
//        else if (exception.equals("refresh token not available")) {
//            setResponse(response, errorCode.INVALID_REFRESH_TOKEN);
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            System.out.println(response.getStatus()+"");
//            logger.error("refresh token not available");
//        }
//
//        else if (exception.equals("access token end")) {
//            setResponse(response, errorCode.INVALID_ACCESS_TOKEN);
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            System.out.println(response.getStatus()+"");
//            logger.error("access token end");
//        }
//    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String)request.getAttribute("exception");
        ErrorCode errorCode = null;

        if (exception == null) {
            setResponse(response, errorCode.NO_ERROR_TYPE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println(response.getStatus()+"");
            logger.error(authException.getMessage());
        }

        else if (exception.equals("refresh token not available")) {
            setResponse(response, errorCode.INVALID_REFRESH_TOKEN);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println(response.getStatus()+"");
            logger.error("refresh token not available");
        }

        else if (exception.equals("access token end")) {
            setResponse(response, errorCode.INVALID_ACCESS_TOKEN);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println(response.getStatus()+"");
            logger.error("access token end");
        }
    }

    /**
     * 한글 출력을 위해 getWriter() 사용
     */
//    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().println("{ \"message\" : \"" + errorCode.getHttpStatus()
//                + "\", \"status\" : \"" + errorCode.getDetail()+ "\"}");
//    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"message\" : \"" + errorCode.getStatus()
                + "\", \"status\" : \"" + errorCode.getReason()+ "\"}");
    }
}