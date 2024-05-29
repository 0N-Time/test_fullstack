package com.example.backend.service;

import com.example.backend.Exception.NotFoundException;
import com.example.backend.model.dao.Account;
import com.example.backend.model.dao.Color;
import com.example.backend.model.dto.ColorResponse;
import com.example.backend.model.repository.ColorRepository;
import com.example.backend.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShopService {

    private final UserRepository userRepository;
    private final ColorRepository colorRepository;

    public Color buyColor(Account account, Long colorId) throws InstantiationException {
        List<Color> colors = colorRepository.findColorsByAccountId(account.getId());
        if (colors.stream().anyMatch(c -> c.getId().equals(colorId))) {
            throw new InstantiationException("You already own this color");
        }

            Optional<Color> color = colorRepository.findById(colorId);
            if (color.isPresent()) {
                if (account.getMedals().compareTo(color.get().getPrice()) >= 0) {
                    account.setMedals(account.getMedals().subtract(color.get().getPrice()));
                    account.getColors().add(color.get());
                    userRepository.save(account);
                    return color.get();
                }
                throw new InstantiationException("Not enough medals");
            }
            throw new NotFoundException("Color not found");
    }

    public Color equipColor(Account account, Long colorId) throws InstantiationException {
        List<Color> colors = colorRepository.findColorsByAccountId(account.getId());
        if (colors.stream().anyMatch(c -> c.getId().equals(colorId))) {
            Optional<Color> color = colorRepository.findById(colorId);
            if (color.isPresent()) {
                account.setEquippedColor(color.get().getColorCode());
                userRepository.save(account);
                return color.get();
            }
        }
            throw new InstantiationException("You don't own this color");
    }

    public List<ColorResponse> getAvailableColors(Account account) {
        List<Color> ownedColors = colorRepository.findColorsByAccountId(account.getId());
        List<Color> colors = colorRepository.findAll();
        List<ColorResponse> colorResponses = new ArrayList<>();

        for (Color color : colors) {
            ColorResponse colorResponse = new ColorResponse(color, account);
            colorResponse.setOwned(ownedColors.stream().anyMatch(c -> c.getId().equals(color.getId())));
            colorResponses.add(colorResponse);
        }
        return colorResponses;
    }
}
