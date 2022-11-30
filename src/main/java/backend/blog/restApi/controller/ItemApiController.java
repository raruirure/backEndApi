package backend.blog.restApi.controller;

import backend.blog.restApi.domain.ResponseMessage;
import backend.blog.web.domain.item.Item;
import backend.blog.web.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags = "상품 관련 조회")
@RestController
@RequestMapping("/api/pnd")
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;

    @ApiOperation(value = "조회", notes = "<b style='color: red;'>Read DataBase</b>에 조회합니다.")
    @GetMapping("/items")
    public ResponseMessage getItemList(Model model) {
        ResponseMessage res = new ResponseMessage();
        List<Item> items = itemService.findItems();
        log.info("Items List {}", items);

        // test
        res.setData(items);
        res.setStatusCode(HttpStatus.OK.value());
        return res;
    }
}
