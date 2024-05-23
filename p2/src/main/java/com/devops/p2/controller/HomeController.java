package com.devops.p2.controller;

import com.devops.p2.config.AWSClientConfig;
import com.devops.p2.dao.PersonRepository;
import com.devops.p2.entity.Person;
import com.devops.p2.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private AWSClientConfig awsClientConfig;

    @GetMapping("/")
    public String getUsers(Model model) {
        List<Person> plist = personRepository.findAll();
        model.addAttribute("plist", plist);
        return "index";
    }

    @GetMapping("/addPerson")
    public String addPersonPage(Model model) {
        model.addAttribute("person", new Person());
        return "addPerson";
    }

    @PostMapping("/addPerson")
    public String addPerson(@ModelAttribute Person person, @RequestParam("imageFile") MultipartFile imageFile, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "errorPage";
        }
        try {
            if (!imageFile.isEmpty()) {
                byte[] bytes = imageFile.getBytes();
                Path tempPath = Files.createTempFile("temp", imageFile.getOriginalFilename());
                Files.write(tempPath, bytes);

                String key = imageFile.getOriginalFilename(); // You can adjust the key as needed
                documentService.uploadFileToS3(awsClientConfig.getBucketName(), key, tempPath.toFile());

                person.setImgUrl(documentService.generateImageUrl(awsClientConfig.getBucketName(), key));

                Files.deleteIfExists(tempPath);
            }
            personRepository.save(person);
            return "redirect:";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload the file";  // Consider a more descriptive error handling
        }
    }

    @GetMapping("/updatePerson/{id}")
    public String updatePersonPage(@PathVariable Integer id, Model model) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            model.addAttribute("person", optionalPerson.get());
            return "updatePerson";
        } else {
            return "redirect:";
        }
    }

    @PostMapping("/updatePerson")
    public String updatePerson(@ModelAttribute Person person, @RequestParam("imageFile") MultipartFile imageFile, BindingResult bindingResult) {
        int id = person.getId();
        Optional<Person> optionalPerson = personRepository.findById(id);
        String keyImg = extractFileName(optionalPerson.get().getImgUrl());
        documentService.deleteFileFromS3(awsClientConfig.getBucketName(), keyImg);

        try {
            // Check if file is empty
            if (!imageFile.isEmpty()) {
                // Save the uploaded file temporarily
                byte[] bytes = imageFile.getBytes();
                Path tempPath = Files.createTempFile("temp", imageFile.getOriginalFilename());
                Files.write(tempPath, bytes);

                // Upload the file to S3
                String bucketName = "swe304images";
                String key = imageFile.getOriginalFilename(); // You can adjust the key as needed
                documentService.uploadFileToS3(bucketName, key, tempPath.toFile());

                // Set the image URL in registerDto
                person.setImgUrl(documentService.generateImageUrl(bucketName, key));
                // Delete the temporary file
                Files.deleteIfExists(tempPath);
            }

            // Update the user with the image URL
            personRepository.save(person);

            return "redirect:";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file";
        }
    }

    @GetMapping("/deletePerson/{id}")
    public String deletePersonPage(@PathVariable Integer id, Model model) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            model.addAttribute("person", optionalPerson.get());
            return "deletePerson";
        } else {
            return "redirect:";
        }
    }

    @PostMapping("/deletePerson")
    public String deletePerson(@RequestParam("id") Integer id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        try {
            if (optionalPerson.isPresent()) {
                String key = extractFileName(optionalPerson.get().getImgUrl());
                personRepository.deleteById(optionalPerson.get().getId());
                documentService.deleteFileFromS3(awsClientConfig.getBucketName(), key);
            }
            return "redirect:";
        } catch (NullPointerException e) {
            return e.getMessage();
        }
    }

    private void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            // Dosya silinemezse hata durumunu ele alabilirsiniz
        }
    }

    public static String extractFileName(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            String path = url.getPath();
            // Assuming the path starts with a "/" and the file key is after the last "/"
            return path.substring(path.lastIndexOf('/') + 1);
        } catch (MalformedURLException e) {
            System.err.println("Error processing URL: " + imageUrl);
            e.printStackTrace();
            return null; // or handle more gracefully as needed
        }
    }
}
