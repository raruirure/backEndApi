package backend.blog.domain.item;

import backend.blog.exception.NotEnoughStockException;
import backend.blog.domain.Category;
import backend.blog.dto.UpdateItemDto;
import backend.blog.exception.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    // 1. 재고수량증가
    public void addStock(int stock){
        this.stockQuantity += stock;
    }

    // 2. 재고수량감소
    public void reduceStock(int stock){
        int restStock = this.stockQuantity - stock;

        if(restStock < 0) throw new NotEnoughStockException("not enough stockQuantity");

        this.stockQuantity = restStock;
    }

    // 3. 상품 기본정보 변경
    public void updateBasicInfo(UpdateItemDto itemDto) {
        this.name = itemDto.getName();
        this.price = itemDto.getPrice();
        this.stockQuantity = itemDto.getStockQuantity();
    }
}
