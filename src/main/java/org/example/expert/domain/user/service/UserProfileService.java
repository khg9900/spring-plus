package org.example.expert.domain.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final AmazonS3 amazonS3;
    private final UserRepository userRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public String uploadImage(MultipartFile image, Long userId) throws IOException {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidRequestException("User not found"));

        // 기존의 프로필 이미지가 있었다면 삭제
        if(user.getProfileImage() != null) {
            amazonS3.deleteObject(bucket, getImageKey(user.getProfileImage()));
        }

        // 파일 확장자 추출
        String extension = getImageExtension(image);
        String fileName = UUID.randomUUID() + "_" + user.getId() + "_profile." + extension;

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        // S3에 파일 업로드 요청 생성
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, image.getInputStream(), metadata);

        // S3에 파일 업로드
        amazonS3.putObject(putObjectRequest);

        // 업로드 후 URL 저장
        String publicUrl = getPublicUrl(fileName);
        user.uploadProfileImage(publicUrl);

        return publicUrl;
    }

    @Transactional
    public void deleteImage(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidRequestException("User not found"));

        // 프로필 이미지 삭제
        amazonS3.deleteObject(bucket, getImageKey(user.getProfileImage()));
        user.deleteProfileImage();
    }

    private String getPublicUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, amazonS3.getRegionName(), fileName);
    }

    public String getImageKey(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public String getImageExtension(MultipartFile image) {
        String originalName = image.getOriginalFilename();
        return originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();
    }

}
