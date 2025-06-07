package com.kf4b.bitlist.repository;

import com.kf4b.bitlist.entity.FileBlob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileBlobRepository extends JpaRepository<FileBlob, Integer> {
}
