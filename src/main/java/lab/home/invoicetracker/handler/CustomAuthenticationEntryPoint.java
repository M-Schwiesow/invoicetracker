package lab.home.invoicetracker.handler;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lab.home.invoicetracker.model.HttpResponse;
import tools.jackson.databind.ObjectMapper;

@Component 
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
                HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .reason("You must log in to access this resource")
                .status(UNAUTHORIZED)
                .statusCode(UNAUTHORIZED.value())
                .build();

                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(UNAUTHORIZED.value());
                OutputStream out = response.getOutputStream();
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(out, httpResponse);
                out.flush();
    }
    
}
