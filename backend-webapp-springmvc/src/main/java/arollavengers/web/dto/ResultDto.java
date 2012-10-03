package arollavengers.web.dto;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.util.ClassUtils;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ResultDto {
    @JsonProperty("error")
    public ErrorDto error;

    @JsonProperty("content")
    public Object content;

    public static  ResultDto ok(Object content) {
        ResultDto r = new ResultDto();
        r.content = content;
        return r;
    }

    public static ResultDto err(Exception ex) {
        ResultDto r = new ResultDto();
        r.error = ErrorDto.create(ex);
        return r;
    }
}

