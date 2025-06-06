package com.kf4b.bitlist.controller;

import com.kf4b.bitlist.entity.Attachment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@RestController
@RequestMapping("/tasks/{taskId}/attachments")
public class AttachmentController {
    // 假设文件保存目录（可根据实际需求修改）
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping(value = "/", consumes = "multipart/form-data")
    public Attachment createAttachment(
            @PathVariable String taskId,
            @RequestParam("file") MultipartFile file  // 接收上传的文件
    ) throws IOException {
        // 生成唯一文件名（避免重复）
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID() + extension;

        // 创建保存路径（需确保目录存在）
        Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 保存文件到本地
        Path targetPath = uploadPath.resolve(uniqueFileName);
        file.transferTo(targetPath);

        //TODO 构建Attachment对象
        Attachment attachment = new Attachment();
//        attachment.setTaskId(taskId);
//        attachment.setFileName(originalFilename);
//        attachment.setFilePath(targetPath.toString());
//        attachment.setFileSize(file.getSize());

        // 这里可以添加数据库保存逻辑（如调用Service层存储attachment信息）
        // attachmentService.save(attachment);

        return attachment;  // 返回上传后的文件信息
    }
}
