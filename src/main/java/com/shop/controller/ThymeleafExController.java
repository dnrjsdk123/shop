package com.shop.controller;

import com.shop.dto.ItemDto;
import com.shop.entity.Item;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {
    // localhost/thymeleaf/ex01 -> thymeleafEx01.html
    // ${data} - > Hello World 나오도록 출력

    @GetMapping("/ex01")
    public String thymeleafEx01(Model model){
        model.addAttribute("data","Hello World");
                return "thymeleafEX/thymeleafEx01";
    }
    @GetMapping("/ex02")
    public String thymeleafEx02(Model model){
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());
        model.addAttribute("itemDto",itemDto);
        return "thymeleafEx/thymeleafEx02";
    }
    @GetMapping("/ex03")
    public String thymeleafEx03(Model model){

        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int i = 1; i<=10; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemNm("상품명"+i);
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setPrice(10000+i);
            itemDto.setRegTime(LocalDateTime.now());
            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList",itemDtoList);
        return "thymeleafEx/thymeleafEx03";
    }
    @GetMapping("/ex04")
    public String thymeleafEx04(Model model){

        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int i = 1; i<=10; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemNm("상품명"+i);
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setPrice(10000+i);
            itemDto.setRegTime(LocalDateTime.now());
            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList",itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }
    @GetMapping("ex05")
    public String thymeleafEx05(Model model){
        return "thymeleafEx/thymeleafEx05";
    }
    @GetMapping("ex06")
    public String thymeleafEx06(@RequestParam String param1,
                                @RequestParam String param2,
                                Model model){
        model.addAttribute("param1",param1);
        model.addAttribute("param2",param2);
        return "thymeleafEx/thymeleafEx06";
    }
    @GetMapping("ex07")
    public String thymeleafEx07(){
        return "thymeleafEx/thymeleafEx07";
    }

}
