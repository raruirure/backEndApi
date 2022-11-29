package backend.blog.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class BookForm {
    private Long id;

    @NotEmpty(message = "상품명은 필수 값 입니다.")
    private String name;

    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;
}
