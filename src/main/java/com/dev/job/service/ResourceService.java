package com.dev.job.service;

import com.dev.job.entity.resource.*;
import com.dev.job.exceptions.ResourceNotFoundException;
import com.dev.job.repository.Resource.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ResourceService {


    LocationRepository locationRepository;
    TagRepository tagRepository;
    LinkRepository linkRepository;
    CertificateRepository certificateRepository;
    SchoolRepository schoolRepository;

    // LOCATION
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location createLocation(String name) {
        return locationRepository.save(Location.builder().name(name).build());
    }

    public Location updateLocation(UUID locationId, String name) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found."));
        location.setName(name);
        return locationRepository.save(location);
    }

    public void deleteLocation(UUID locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new ResourceNotFoundException("Location not found.");
        }
        locationRepository.deleteById(locationId);
    }

    // TAG
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag createTag(String name) {
        return tagRepository.save(Tag.builder().name(name).build());
    }

    public Tag updateTag(UUID tagId, String name) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found."));
        tag.setName(name);
        return tagRepository.save(tag);
    }

    public void deleteTag(UUID tagId) {
        if (!tagRepository.existsById(tagId)) {
            throw new ResourceNotFoundException("Tag not found.");
        }
        tagRepository.deleteById(tagId);
    }

    // LINK
    public List<Link> getAllLinks() {
        return linkRepository.findAll();
    }

    public Link createLink(Link request) {
        return linkRepository.save(Link.builder()
                .label(request.getLabel())
                .link(request.getLink())
                .build());
    }

    public Link updateLink(UUID linkId, Link request) {
        Link link = linkRepository.findById(linkId)
                .orElseThrow(() -> new ResourceNotFoundException("Link not found."));
        link.setLabel(request.getLabel());
        link.setLink(request.getLink());
        return linkRepository.save(link);
    }

    public void deleteLink(UUID linkId) {
        if (!linkRepository.existsById(linkId)) {
            throw new ResourceNotFoundException("Link not found.");
        }
        linkRepository.deleteById(linkId);
    }

    // CERTIFICATE
    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    public Certificate createCertificate(Certificate request) {
        return certificateRepository.save(Certificate.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build());
    }

    public Certificate updateCertificate(UUID id, Certificate request) {
        Certificate cert = certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found."));
        cert.setName(request.getName());
        cert.setDescription(request.getDescription());
        return certificateRepository.save(cert);
    }

    public void deleteCertificate(UUID id) {
        if (!certificateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Certificate not found.");
        }
        certificateRepository.deleteById(id);
    }

    // SCHOOL

    public List<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    public School createSchool(School request) {
        return schoolRepository.save(School.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build());
    }

    public School updateSchool(UUID id, School request) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found."));
        school.setName(request.getName());
        school.setDescription(request.getDescription());
        return schoolRepository.save(school);
    }

    public void deleteSchool(UUID id) {
        if (!schoolRepository.existsById(id)) {
            throw new ResourceNotFoundException("School not found.");
        }
        schoolRepository.deleteById(id);
    }

}
