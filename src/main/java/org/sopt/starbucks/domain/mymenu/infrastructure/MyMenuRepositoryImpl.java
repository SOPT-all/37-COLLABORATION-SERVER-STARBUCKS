package org.sopt.starbucks.domain.mymenu.infrastructure;

import lombok.RequiredArgsConstructor;
import org.sopt.starbucks.domain.mymenu.domain.MyMenu;
import org.sopt.starbucks.domain.mymenu.domain.MyMenuRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyMenuRepositoryImpl implements MyMenuRepository {

    private final MyMenuJpaRepository myMenuJpaRepository;

    @Override
    public List<MyMenu> findAll() {
        return myMenuJpaRepository.findAll();
    }
}
