package org.sopt.starbucks.domain.menu.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.starbucks.domain.category.domain.Category;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menu")
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_kr", nullable = false)
    private String menuKr;

    @Column(name = "name_eng", nullable = false)
    private String menuEng;

    @Column(name = "info", nullable = false, length = 1000)
    private String info;

    @Column(name = "price", nullable = false)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Builder
    public Menu(String menuKr, String menuEng, String info, Integer price, Category category) {
        this.menuKr = menuKr;
        this.menuEng = menuEng;
        this.info = info;
        this.price = price;
        this.category = category;
    }
}
