package com.kf4b.bitlist.controller;

import com.kf4b.bitlist.entity.MindedItem;
import com.kf4b.bitlist.service.MindedItemService;
import com.kf4b.bitlist.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mindeditem")
public class MindedItemController {
    @Autowired
    private MindedItemService mindedItemService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // 构造函数注入
    public MindedItemController(MindedItemService mindedItemService) {
        this.mindedItemService = mindedItemService;
    }
}
