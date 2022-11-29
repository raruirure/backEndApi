package backend.blog.service;

import backend.blog.domain.order.OrderSearch;
import backend.blog.repository.ItemRepository;
import backend.blog.repository.MemberRepository;
import backend.blog.repository.OrderRepository;
import backend.blog.domain.Delivery;
import backend.blog.domain.DeliveryStatus;
import backend.blog.domain.Member;
import backend.blog.domain.order.Order;
import backend.blog.domain.order.OrderItem;
import backend.blog.domain.item.Item;
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
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member orderMember = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(orderMember.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

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
//        return orderRepository.findAllByString(orderSearch);
        return orderRepository.searchOrdersByCriteria(orderSearch);
    }
}
