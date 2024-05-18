/*package com.devops.p2.service.Concrete;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.devops.p2.config.AWSClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final AmazonS3 amazonS3;
    private final AWSClientConfig awsClientConfig;

    public String upload(MultipartFile file) {
        File localFile = convertMultipartFileToFile(file);

        amazonS3.putObject(new PutObjectRequest(awsClientConfig.getBucketName(), file.getOriginalFilename(), localFile));

        return file.getOriginalFilename();
    }

    private File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), convertedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convertedFile;
    }
}
*/