package org.sopt.starbucks.domain.mymenu.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.starbucks.domain.menu.domain.Menu;
import org.sopt.starbucks.global.common.converter.PersonalOptionConverter;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "my_menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyMenu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "custom_name")
    private String customName;

    @Column(name = "count")
    private Integer count;

    @Column(name = "is_hot")
    private Boolean isHot;

    @Enumerated(EnumType.STRING)
    @Column(name = "size")
    private Size size;

    @Column(name = "personal_options", columnDefinition = "json")
    @Convert(converter = PersonalOptionConverter.class)
    private List<PersonalOption> personalOptions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Builder
    public MyMenu(String customName, Integer count, Boolean isHot, Size size, List<PersonalOption> personalOptions) {
        this.customName = customName;
        this.count = count;
        this.isHot = isHot;
        this.size = size;
        this.personalOptions = personalOptions;
    }

    public String getOptionsSummary() {
        if (personalOptions == null || personalOptions.isEmpty()) return "";
        return personalOptions.stream()
                .map(PersonalOption::name)
                .collect(Collectors.joining(" | "));
    }
}
