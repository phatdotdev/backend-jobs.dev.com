package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.entity.resource.*;

import static com.dev.job.utils.ResponseHelper.*;

import com.dev.job.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ResourceController {

    ResourceService resourceService;

    @GetMapping("/locations")
    public ResponseEntity<ApiResponse<List<Location>>> getAllLocations() {
        return ok(resourceService.getAllLocations());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/locations")
    public ResponseEntity<ApiResponse<Location>> createLocation(@RequestBody String name) {
        return created(resourceService.createLocation(name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/locations/{locationId}")
    public ResponseEntity<ApiResponse<Location>> updateLocation(@PathVariable UUID locationId, @RequestBody String name) {
        return ok(resourceService.updateLocation(locationId, name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/locations/{locationId}")
    public ResponseEntity<ApiResponse<String>> deleteLocation(@PathVariable UUID locationId) {
        resourceService.deleteLocation(locationId);
        return ok("Location deleted successfully.");
    }

    @GetMapping("/tags")
    public ResponseEntity<ApiResponse<List<Tag>>> getAllTags() {
        return ok(resourceService.getAllTags());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/tags")
    public ResponseEntity<ApiResponse<Tag>> createTag(@RequestBody String name) {
        return created(resourceService.createTag(name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/tags/{tagId}")
    public ResponseEntity<ApiResponse<Tag>> updateTag(@PathVariable UUID tagId, @RequestBody String name) {
        return ok(resourceService.updateTag(tagId, name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<ApiResponse<String>> deleteTag(@PathVariable UUID tagId) {
        resourceService.deleteTag(tagId);
        return ok("Tag deleted successfully.");
    }

    @GetMapping("/links")
    public ResponseEntity<ApiResponse<List<Link>>> getAllLinks() {
        return ok(resourceService.getAllLinks());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/links")
    public ResponseEntity<ApiResponse<Link>> createLink(@RequestBody Link request) {
        return created(resourceService.createLink(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/links/{linkId}")
    public ResponseEntity<ApiResponse<Link>> updateLink(@PathVariable UUID linkId, @RequestBody Link request) {
        return ok(resourceService.updateLink(linkId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/links/{linkId}")
    public ResponseEntity<ApiResponse<String>> deleteLink(@PathVariable UUID linkId) {
        resourceService.deleteLink(linkId);
        return ok("Link deleted successfully.");
    }

    @GetMapping("/certificates")
    public ResponseEntity<ApiResponse<List<Certificate>>> getAllCertificates() {
        return ok(resourceService.getAllCertificates());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/certificates")
    public ResponseEntity<ApiResponse<Certificate>> createCertificate(@RequestBody Certificate request) {
        return created(resourceService.createCertificate(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/certificates/{id}")
    public ResponseEntity<ApiResponse<Certificate>> updateCertificate(@PathVariable UUID id, @RequestBody Certificate request) {
        return ok(resourceService.updateCertificate(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/certificates/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCertificate(@PathVariable UUID id) {
        resourceService.deleteCertificate(id);
        return ok("Certificate deleted successfully.");
    }

    // SCHOOL
    @GetMapping("/schools")
    public ResponseEntity<ApiResponse<List<School>>> getAllSchools() {
        return ok(resourceService.getAllSchools());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/schools")
    public ResponseEntity<ApiResponse<School>> createSchool(@RequestBody School request) {
        return created(resourceService.createSchool(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/schools/{id}")
    public ResponseEntity<ApiResponse<School>> updateSchool(@PathVariable UUID id, @RequestBody School request) {
        return ok(resourceService.updateSchool(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/schools/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSchool(@PathVariable UUID id) {
        resourceService.deleteSchool(id);
        return ok("School deleted successfully.");
    }


}

