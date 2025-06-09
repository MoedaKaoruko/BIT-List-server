package com.kf4b.bitlist.controller;

import com.kf4b.bitlist.entity.FileBlob;
import com.kf4b.bitlist.entity.Task;
import com.kf4b.bitlist.service.FileBlobService;
import com.kf4b.bitlist.service.TaskService;
import com.kf4b.bitlist.service.TeamService;
import com.kf4b.bitlist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/tasks/{taskId}/attachments")
public class FileBlobController {
    @Autowired
    private FileBlobService fileBlobService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TeamService teamService;

    /**
     * 检查权限：用户是否有操作指定任务的附件的权限
     */
    private boolean checkPermissionFailure(Integer userId, Integer taskId) {
        Task task = taskService.getTaskById(taskId);
        if (task == null) {
            return true;
        }
        if (Objects.equals(task.getAssignedTo(), userId)) {
            return false;
        }else {
            return true;
        }
    }

    /**
     * 上传文件到指定任务
     *
     * @param taskId 任务ID
     * @param file   上传的文件
     * @return 上传成功的文件信息
     * @throws IOException 如果文件上传失败
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public Map<String, Object> upload(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer taskId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (checkPermissionFailure(userService.getUserByHeader(authorizationHeader).getUserId(), taskId)) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "Permission denied");
            return result;
        }
        String originalFilename = file.getOriginalFilename();

        // 构建FileBlob对象
        FileBlob fileBlob = new FileBlob();
        fileBlob.setFileName(originalFilename);  // 数据库存储的文件名
        fileBlob.setSizeInBytes((int) file.getSize());  // 文件大小
        fileBlob.setFileBlob(file.getBytes());  // 文件内容转为字节数组存入Blob字段
        fileBlob.setTaskId(taskId);  // 关联任务ID
        fileBlob.setDeleted(false);  // 默认未删除

        // 调用Service保存到数据库
        fileBlob = fileBlobService.updateFileBlobById(-1, fileBlob);

        // 返回成功信息
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "File uploaded successfully");
        result.put("id", fileBlob.getId());
        result.put("fileName", fileBlob.getFileName());
        result.put("sizeInBytes", fileBlob.getSizeInBytes());
        result.put("isDeleted", fileBlob.isDeleted());
        result.put("permissions", fileBlob.getPermissions());
        result.put("deletedAt", "2019-08-24");
        result.put("file", null);
        result.put("attachmentLink", null);
        return result;
    }

    /**
     * 下载指定任务的附件
     **/
    @GetMapping("/{attachmentId}/download")
    public ResponseEntity<byte[]> download(@RequestHeader("Authorization") String authorizationHeader,
                                           @PathVariable Integer taskId,
                                           @PathVariable Integer attachmentId) throws IOException {

        FileBlob fileBlob = fileBlobService.getFileBlobById(attachmentId);
        if (fileBlob == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (checkPermissionFailure(userService.getUserByHeader(authorizationHeader).getUserId(), taskId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
        String encodedFilename = URLEncoder.encode(fileBlob.getFileName(), "utf-8")
                .replace("+", "%20");
        headers.setContentDispositionFormData("attachment", encodedFilename);
        return new ResponseEntity<>(fileBlob.getFileBlob(), headers, HttpStatus.OK);
    }

    /**
     * 删除指定附件
     **/
    @DeleteMapping("/{attachmentId}")
    public boolean delete(@RequestHeader("Authorization") String authorizationHeader,
                          @PathVariable Integer taskId,
                          @PathVariable Integer attachmentId) {
        // 检查权限
        if (checkPermissionFailure(userService.getUserByHeader(authorizationHeader).getUserId(), taskId)) {
            return false;
        }
        fileBlobService.deleteFileBlob(attachmentId);
        return true;
    }

    @PostMapping("/{attachmentId}/restore")
    public boolean restore(@RequestHeader("Authorization") String authorizationHeader,
                           @PathVariable Integer taskId,
                           @PathVariable Integer attachmentId) {
        // 检查权限
        if (checkPermissionFailure(userService.getUserByHeader(authorizationHeader).getUserId(), taskId)) {
            return false;
        }
        fileBlobService.restoreFileBlob(attachmentId);
        return true;
    }

    @PutMapping("/{attachmentId}/permissions")
    public boolean updatePermissions(@RequestHeader("Authorization") String authorizationHeader,
                                     @PathVariable Integer taskId,
                                     @PathVariable Integer attachmentId,
                                     @RequestBody Map<String, Object> requestMap) {
        // 检查权限
        if (checkPermissionFailure(userService.getUserByHeader(authorizationHeader).getUserId(), taskId)) {
            return false;
        }
        FileBlob fileBlob = fileBlobService.getFileBlobById(attachmentId);
        if (fileBlob == null) {
            return false;
        }
        Map<Integer, FileBlob.Permission> permissions = new HashMap<>();
        permissions.putAll((Map<Integer, FileBlob.Permission>) requestMap.get("permissions"));
        fileBlob.setPermissions(permissions);
        fileBlobService.updateFileBlobById(fileBlob.getId(), fileBlob);
        return true;
    }
}
