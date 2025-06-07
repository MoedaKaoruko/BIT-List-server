package com.kf4b.bitlist.service;

import com.kf4b.bitlist.entity.*;
import java.util.*;

public interface MindedItemService {

    // 常规功能，根据主键ID查事项
    MindedItem getMindedItemById(Integer mindedItemId);

    // 根据用户id获取其所有的专注事项
    List<MindedItem> getMindedItemByUserId(Integer userId);

    // 根据用户id获取其所有的专注事项并返回所有专注时间的总和（单位：秒）
    Integer totalDurationInSeconds(Integer userId);

}
