package com.example.backend.model.dto;

import com.example.backend.model.dao.Color;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ColorResponse {
    public Long id;
    public String colorName;
    public String colorCode;
    public BigDecimal price;

    public ColorResponse(Color color) {
        id = color.getId();
        colorName = color.getColorName();
        colorCode = color.getColorCode();
        price = color.getPrice();
    }
}
