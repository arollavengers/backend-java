package arollavengers.web.dto;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.util.ClassUtils;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ErrorDto {

    @JsonProperty
    public String type;

    @JsonProperty
    public String message;

    @JsonProperty
    public String trace;

    public static ErrorDto create(Exception ex) {
        ErrorDto dto = new ErrorDto();
        dto.type = ClassUtils.getShortName(ex.getClass());
        dto.message = ex.getMessage();
        dto.trace = ExceptionUtils.getStackTrace(ex);
        return dto;
    }
}
