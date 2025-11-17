package org.sopt.starbucks.domain.menu.application;

import lombok.RequiredArgsConstructor;
import org.sopt.starbucks.domain.menu.domain.MenuRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl {

    private final MenuRepository menuRepository;
}
