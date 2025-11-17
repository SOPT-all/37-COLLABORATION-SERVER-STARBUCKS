package org.sopt.starbucks.domain.mymenu.infrastructure;

import org.sopt.starbucks.domain.mymenu.domain.MyMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyMenuJpaRepository extends JpaRepository<MyMenu, Long> {
}
