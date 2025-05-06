package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.service.UserProfileService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/{userId}")
    public String uploadImage(
        @PathVariable long userId,
        @RequestParam("file") MultipartFile file
    ) {
        try {
            String imageUrl = userProfileService.uploadImage(file, userId);
            return "File uploaded successfully! imageUrl: " + imageUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "File upload failed!";
        }
    }

    @DeleteMapping("/{userId}")
    public String deleteImage(@PathVariable long userId) {
        userProfileService.deleteImage(userId);
        return "Ok";
    }
}
