package com.app.oauth.service;

import com.app.oauth.domain.dto.response.ApiResponseDTO;
import com.app.oauth.exception.FileException;
import com.app.oauth.util.AwsS3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class FileServiceImpl implements FileService {

    private final AwsS3Util awsS3Util;

    @Override
    public ApiResponseDTO uploadFile(MultipartFile uploadFile) {
        Map<String, Object> responseData = new HashMap<>();
        String uploadedUrl = null;
        try {
            uploadedUrl = awsS3Util.uploadFile(uploadFile);
            responseData.put("uploadedUrl", uploadedUrl);
        } catch (IOException e) {
            throw new FileException("파일 업로드 실패", HttpStatus.BAD_REQUEST);
        }
        return ApiResponseDTO.of(true, "파일 업로드 성공", responseData);
    }

    @Override
    public ApiResponseDTO uploadFiles(List<MultipartFile> uploadFiles) {
        Map<String, Object> responseData = new HashMap<>();
        List<String> uploadedUrls = null;
        try {
            uploadedUrls = awsS3Util.uploadFiles(uploadFiles);
            responseData.put("uploadedUrls", uploadedUrls);
        } catch (IOException e) {
            throw new FileException("파일 업로드 실패", HttpStatus.BAD_REQUEST);
        }
        return ApiResponseDTO.of(true, "파일 업로드 성공", responseData);
    }

    @Override
    public byte[] getDisplayPath(String fileName){
        String key = fileName.replaceFirst("/", "");
        byte[] bytes = null;
        try{
            bytes =  awsS3Util.display(key);
        } catch (Exception e){
            throw new FileException("파일 조회 실패", HttpStatus.BAD_REQUEST);
        }

        return bytes;
    }

}
