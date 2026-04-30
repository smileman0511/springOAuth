package com.app.oauth.controller;

import com.app.oauth.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileDisplayController {

    private final FileService fileService;

    // 쿼리스트링
    @GetMapping("/display")
    public byte[] display(String fileName) {
        return fileService.getDisplayPath(fileName);
    }
}
