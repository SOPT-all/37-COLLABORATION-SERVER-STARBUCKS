package org.sopt.starbucks.domain.mymenu.domain;

import java.util.List;
import java.util.Optional;

public interface MyMenuRepository {

    List<MyMenu> findAll();
    Optional<MyMenu> findById(Long myMenuId);
    void save(MyMenu myMenu);
}
