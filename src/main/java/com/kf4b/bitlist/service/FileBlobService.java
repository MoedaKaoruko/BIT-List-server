package com.kf4b.bitlist.service;

import com.kf4b.bitlist.entity.*;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface FileBlobService {

    // 根据主键获取文件数据
    FileBlob getFileBlobById(Integer FileId);

    // 根据任务ID获取相关文件序列
    List<FileBlob> getFileBlobByTaskId(Integer TaskId);

    // 更新操作
    FileBlob updateFileBlobById(Integer FileId, FileBlob file);

    // 删除操作
    void deleteFileBlob(Integer FileId);

    // 恢复操作
    void restoreFileBlob(Integer FileId);

}
