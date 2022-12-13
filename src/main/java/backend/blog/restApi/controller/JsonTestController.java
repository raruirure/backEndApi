package backend.blog.restApi.controller;

import backend.blog.restApi.domain.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@Api(tags = "JSON 테스트용")
@RestController
@RequestMapping("/api/json")
@RequiredArgsConstructor
public class JsonTestController {
    @ApiOperation(value = "테스트", notes = "<b style='color: red;'>Json</b> 조회.")
    @PostMapping("/loopTest")
    public ResponseMessage testJson(@RequestBody HashMap<String, Object> req) throws JSONException {
        ResponseMessage res = new ResponseMessage();

        /**
         * 데이터 형식
         * 1. 받은 req에서 추출(Postman 호출 더미 하단 주석 기재)
         * 2. 더미 JsonString 사용
         */
        // 1. 받은 req에서 추출
        JSONObject json =  new JSONObject(req);
        JSONArray arr = (JSONArray) json.get("json");

        // 2. 더미 JsonString 사용
        // String s = "[{\"level\":1,\"label\":\"name1\",\"title\":null,\"data\":[{\"level\":2,\"label\":\"name2\",\"title\":null,\"data\":[{\"level\":3,\"label\":\"name3\",\"title\":null,\"data\":[{\"level\":4,\"label\":\"name4\",\"title\":null,\"data\":[]}]}]}]}]";
        // JSONArray arr = new JSONArray(s);

        arr = checkArrayValue(arr);

        log.info("Object Info: {}", arr);

        res.setData(arr.toString());
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }

    public JSONArray checkArrayValue(JSONArray data) {
        try {
            for(int i=0;i<data.length();i++) {
                JSONObject obj = new JSONObject();
                obj = checkObjectValue((JSONObject) data.get(i));
                log.info("JSONArray Unit: {}", obj);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    public JSONObject checkObjectValue(JSONObject data) {
        try {
            if (data.get("data")!= null) {
                JSONArray arr = (JSONArray) data.get("data");
                arr = checkArrayValue(arr);
            }
            /**
             * 특정 필드가 비었거나 그 값을 찾아오는 로직을 아래에 기술
             * 1. 아래는 특정 필드가 비었을 경우에 더미로 생성
             * 2. json key value를 통한 특정 데이터에서 추출하도록 처리 등 수정 처리
             */
            if (data.get("title").equals(null)) {
                data.put("title", "change Title_" + data.get("level"));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

//    Postman 호출 더미 하단 주석 기재
//    {
//        "json" : [
//            {
//                "level": 1,
//                    "label": "name 1",
//                    "title": "",
//                    "data": [
//                {
//                    "level": 2,
//                        "label": "name 2",
//                        "title": "",
//                        "data": [
//                    {
//                        "level": 3,
//                            "label": "name 3",
//                            "title": "",
//                            "data": [
//                        {
//                            "level": 4,
//                                "label": "name 4",
//                                "title": "",
//                                "data": []
//                        }
//                                ]
//                    }
//                        ]
//                }
//                ]
//            }
//        ]
//    }
}
