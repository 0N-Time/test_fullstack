package com.example.backend.Initialization;

import com.example.backend.model.dao.Color;
import com.example.backend.model.repository.ColorRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class ColorInitializer implements ApplicationRunner {

    private final ColorRepository colorRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (colorRepository.count() == 0) {
            Color yellow = new Color();
            yellow.setColorName("Yellow");
            yellow.setColorCode("#FFFF00");
            yellow.setPrice(new BigDecimal("0"));
            colorRepository.save(yellow);

            Color purple = new Color();
            purple.setColorName("Purple");
            purple.setColorCode("#800080");
            purple.setPrice(new BigDecimal("200"));
            colorRepository.save(purple);

            Color green = new Color();
            green.setColorName("Green");
            green.setColorCode("#008000");
            green.setPrice(new BigDecimal("300"));
            colorRepository.save(green);
        }
    }
}
