package com.app.oauth.service;

import com.app.oauth.domain.dto.response.ApiResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    // 파일 1개 업로드
    public ApiResponseDTO uploadFile(MultipartFile uploadFile);

    // 파일 여러 개 업로드
    public ApiResponseDTO uploadFiles(List<MultipartFile> uploadFiles);

    // 절대 경로를 숨기는 display
    public byte[] getDisplayPath(String fileName);
}
