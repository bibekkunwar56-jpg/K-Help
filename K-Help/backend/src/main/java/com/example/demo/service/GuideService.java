package com.example.demo.service;

import com.example.demo.dto.GuideResponse;
import com.example.demo.dto.SaveGuideRequest;
import com.example.demo.entity.Guide;
import com.example.demo.entity.User;
import com.example.demo.repository.GuideRepository;
import com.example.demo.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GuideService {

    private final GuideRepository guideRepository;
    private final UserRepository userRepository;

    public GuideService(GuideRepository guideRepository, UserRepository userRepository) {
        this.guideRepository = guideRepository;
        this.userRepository = userRepository;
    }

    /** Public list. Topic and keyword are both optional; only published guides are returned. */
    @Transactional(readOnly = true)
    public List<GuideResponse> list(String topic, String q) {
        String cleanTopic = blankToNull(topic);
        String keyword = blankToNull(q);

        List<Guide> guides;
        if (cleanTopic != null && keyword != null) {
            guides = guideRepository.searchPublishedByTopic(cleanTopic.trim().toLowerCase(), keyword);
        } else if (cleanTopic != null) {
            guides = guideRepository.findByPublishedTrueAndTopicOrderByTitleAsc(cleanTopic.trim().toLowerCase());
        } else if (keyword != null) {
            guides = guideRepository.searchPublished(keyword);
        } else {
            guides = guideRepository.findByPublishedTrueOrderByTitleAsc();
        }

        return guides.stream().map(GuideResponse::from).toList();
    }

    /** Topic chips for the guides page. */
    @Transactional(readOnly = true)
    public List<String> topics() {
        return guideRepository.findPublishedTopics();
    }

    @Transactional(readOnly = true)
    public GuideResponse getBySlug(String slug) {
        Guide guide = guideRepository
                .findBySlug(slug.trim().toLowerCase())
                .filter(Guide::isPublished)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guide not found"));
        return GuideResponse.from(guide);
    }

    /** Admin only — includes drafts. */
    @Transactional(readOnly = true)
    public List<GuideResponse> listAll(UUID userId) {
        requireAdmin(userId);
        return guideRepository.findAll().stream()
                .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
                .map(GuideResponse::from)
                .toList();
    }

    @Transactional
    public GuideResponse create(UUID userId, SaveGuideRequest request) {
        User author = requireAdmin(userId);
        String slug = request.getSlug().trim().toLowerCase();

        if (guideRepository.existsBySlug(slug)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A guide with this slug already exists");
        }

        Guide guide = new Guide();
        guide.setAuthor(author);
        apply(guide, request, slug);

        return GuideResponse.from(guideRepository.save(guide));
    }

    @Transactional
    public GuideResponse update(String slug, UUID userId, SaveGuideRequest request) {
        requireAdmin(userId);
        Guide guide = requireGuide(slug);

        String newSlug = request.getSlug().trim().toLowerCase();
        if (!guide.getSlug().equals(newSlug)) {
            if (guideRepository.existsBySlug(newSlug)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "A guide with this slug already exists");
            }
            guide.setSlug(newSlug);
        }

        apply(guide, request, newSlug);
        return GuideResponse.from(guideRepository.save(guide));
    }

    @Transactional
    public void delete(String slug, UUID userId) {
        requireAdmin(userId);
        guideRepository.delete(requireGuide(slug));
    }

    // ---------------------------------------------------------------- helpers

    private void apply(Guide guide, SaveGuideRequest request, String slug) {
        guide.setSlug(slug);
        guide.setTitle(request.getTitle().trim());
        guide.setSummary(request.getSummary().trim());
        guide.setBody(request.getBody().trim());
        guide.setTopic(request.getTopic().trim().toLowerCase());
        guide.setSourceUrl(blankToNull(request.getSourceUrl()));
        guide.setPublished(request.isPublished());
    }

    private Guide requireGuide(String slug) {
        return guideRepository
                .findBySlug(slug.trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guide not found"));
    }

    private User requireAdmin(UUID userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!"ADMIN".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can manage guides");
        }
        return user;
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
