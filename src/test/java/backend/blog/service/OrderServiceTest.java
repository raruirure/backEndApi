package backend.blog.service;

import backend.blog.domain.item.Item;
import backend.blog.domain.order.OrderStatus;
import backend.blog.exception.NotEnoughStockException;
import backend.blog.repository.OrderRepository;
import backend.blog.domain.Address;
import backend.blog.domain.Member;
import backend.blog.domain.item.Book;
import backend.blog.domain.order.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired private EntityManager em;
    @Autowired private OrderService orderService;
    @Autowired private OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember("김회원", "서울시", "방화동로", "96566");

        Item book = createBook("처음부터 시작하는 JPA 기초", 10000, 100);

        // when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문 시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 동일해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", book.getPrice() * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.",  100 - orderCount, book.getStockQuantity());

    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember("김회원", "서울시", "방화동로", "96566");
        Item book = createBook("처음부터 시작하는 JPA 기초", 10000, 100);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        
        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문 시 상태는 CANCEL", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문 취소 시 재고는 복구 되야한다", 100, book.getStockQuantity());

    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember("김회원", "서울시", "방화동로", "96566");
        Item book = createBook("처음부터 시작하는 JPA 기초", 10000, 100);

        int orderCount = 101;
        
        // when
        orderService.order(member.getId(), book.getId(), orderCount);

        // then
        fail("예외가 발생해야 한다.");

    }

    private Book createBook(String bookName, int bookPrice, int stock) {
        Book book = new Book();
        book.setName(bookName);
        book.setPrice(bookPrice);
        book.setStockQuantity(stock);
        em.persist(book);
        return book;
    }

    private Member createMember(String memberName, String city, String street, String zipcode) {
        Member member = new Member();
        member.setName(memberName);
        member.setAddress(new Address(city, street, zipcode));
        em.persist(member);
        return member;
    }
}