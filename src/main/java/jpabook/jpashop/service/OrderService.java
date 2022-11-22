package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.order.Order;
import jpabook.jpashop.domain.order.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.order.OrderSearch;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member orderMember = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(orderMember.getAddress());

        // 주문상품 생성
        OrderItem orderitem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(orderMember, delivery, orderitem);

        // 주문 저장 - Order 내 CascadeType.ALL 옵션으로 인해 OrderItem, Delivery 같이 저장 됨.
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     * - 도메인 모델 패턴
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소처리
        order.cancel();
    }

    /**
     * 주문 검색
     */
    public List<Order> searchOrders (OrderSearch orderSearch) {
        return orderRepository.searchOrders(orderSearch);
    }
}
