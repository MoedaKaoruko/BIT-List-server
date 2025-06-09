package com.kf4b.bitlist.service.impl;

import com.kf4b.bitlist.entity.*;
import com.kf4b.bitlist.repository.MindedItemRepository;
import com.kf4b.bitlist.repository.UserRepository;
import com.kf4b.bitlist.service.MindedItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.*;

@Service
public class MindedItemServiceImpl implements MindedItemService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MindedItemRepository mindedItemRepository;

    @Override
    public MindedItem getMindedItemById(Integer mindedItemId){
        Optional<MindedItem> a = mindedItemRepository.findById(mindedItemId);
        if(a.isPresent()){
            MindedItem x = a.get();
            return x;
        }
        return null;
    }

    @Override
    public List<MindedItem> getMindedItemByUserId(Integer userId){
        List<MindedItem> x = mindedItemRepository.findByUserId(userId);
        return x.isEmpty() ? null : x;
    }

    @Override
    public Integer totalDurationInSeconds(Integer userId){
        Integer sum = 0;
        List<MindedItem> x = mindedItemRepository.findByUserId(userId);
        if(x.isEmpty()){
            return sum;
        }
        Iterator<MindedItem> iter = x.iterator();
        while(iter.hasNext()){
            MindedItem element = iter.next();
            sum = sum + element.getDurationInSeconds();
        }
        return sum;
    }

    @Transactional
    public void updateMindedItemById(Integer id, MindedItem item){
        Optional<MindedItem> ms = mindedItemRepository.findById(id);
        MindedItem m = ms.isPresent() ? ms.get() : new MindedItem();
        m.setTimestamp(item.getTimestamp());
        m.setDurationInSeconds(item.getDurationInSeconds());
        m.setUserId(item.getUserId());
        mindedItemRepository.save(m);
    }
}
