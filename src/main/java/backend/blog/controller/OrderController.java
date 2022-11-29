package backend.blog.controller;

import backend.blog.domain.order.OrderSearch;
import backend.blog.service.ItemService;
import backend.blog.service.MemberService;
import backend.blog.domain.Member;
import backend.blog.domain.item.Item;
import backend.blog.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createOrder(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId, @RequestParam("itemId") Long itemId, @RequestParam("count") int count) {
        orderService.order(memberId, itemId, count);

        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String findOrders(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        model.addAttribute("orders", orderService.searchOrders(orderSearch));
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String orderCancel(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
