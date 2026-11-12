package lab.home.invoicetracker.model;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import lombok.SuperBuilder;


/**
 * A standardised response object.  
 * Personally on the fence about the generic data... but then, that's just a "body", isn't it?
 * HttpResponse
 */
@Data 
@Builder 
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HttpResponse {
    protected String timeStamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String reason;
    protected String message;
    protected String developerMessage;
    protected Map<?,?> data;
}
