package com.kf4b.bitlist.repository;

import com.kf4b.bitlist.entity.MindedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.*;

public interface MindedItemRepository extends JpaRepository<MindedItem, Integer> {
        List<MindedItem> findByUserId(Integer user_id);
}
