package backend.blog.restApi.domain;

import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage {

    @ApiModelProperty(example = "API 버전")
    private String apiVersion;
    @ApiModelProperty(example = "HTTP 상태 코드")
    private int statusCode;
    @ApiModelProperty(example = "응답 데이터")
    private Object data;
    @ApiModelProperty(example = "결과 응답")
    private String resultMessage;
    @ApiModelProperty(example = "결과 응답 상세")
    private String detailMessage;

    public static ResponseMessage of(Object data, int statusCode, String resultMessage,
                                     String detailMessage, String apiVersion) {
        return ResponseMessage.builder().data(data).statusCode(statusCode).resultMessage(resultMessage)
                .detailMessage(detailMessage).apiVersion(apiVersion).build();
    }

    public static ResponseMessage of(Object data, HttpStatus status, String detailMessage,
                                     String apiVersion) {
        return of(data, status.value(), status.getReasonPhrase(), detailMessage, apiVersion);
    }

    public static ResponseMessage of(Object data, HttpStatus status, String detailMessage) {
        return of(data, status, detailMessage, null);
    }

    public static ResponseMessage of(Object data, HttpStatus status) {
        return of(data, status, null);
    }

    public static ResponseMessage of(HttpStatus status) {
        return of(null, status);
    }

    public static ResponseMessage ok() {
        return of(HttpStatus.OK);
    }

    public static ResponseMessage ok(Object data) {
        return of(data, HttpStatus.OK);
    }

    public <T> T getConvertFromMapToData(Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        return clazz.cast(mapper.convertValue(this.data, clazz));
    }
}
