package com.kf4b.bitlist.repository;

import com.kf4b.bitlist.entity.FileBlob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface FileBlobRepository extends JpaRepository<FileBlob, Integer> {

    List<FileBlob> findByTaskId(Integer TaskId);

    // 删除操作
    @Query(value = "UPDATE File SET is_deleted = 1 WHERE file_id = ?1", nativeQuery = true)
    @Modifying
    void deleteFileBlob(Integer FileId);

    // 恢复操作
    @Query(value = "UPDATE File SET is_deleted = 0 WHERE file_id =?1", nativeQuery = true)
    @Modifying
    void restoreFileBlob(Integer FileId);

}
