package com.example.backend.model.dto;

import com.example.backend.model.dao.Account;
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
    public Boolean owned;
    public Boolean equipped;

    public ColorResponse(Color color, Account account) {
        id = color.getId();
        colorName = color.getColorName();
        colorCode = color.getColorCode();
        price = color.getPrice();
        equipped = account.getEquippedColor().equals(colorCode);
    }
}
