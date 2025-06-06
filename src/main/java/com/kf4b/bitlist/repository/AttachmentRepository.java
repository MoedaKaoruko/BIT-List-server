package com.kf4b.bitlist.repository;

import com.kf4b.bitlist.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {
}
