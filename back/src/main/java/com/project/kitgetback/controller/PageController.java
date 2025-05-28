package com.project.kitgetback.controller;

import com.project.kitgetback.entity.MembershipEntity;
import com.project.kitgetback.repository.MembershipRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class PageController {
    private final MembershipRepository membershipRepository;

    public PageController(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    // 사용자 school값 반환
    @GetMapping("/user/school")
    public ResponseEntity<?> getUserSchool() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String email = auth.getName();
            MembershipEntity member = membershipRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
            return ResponseEntity.ok(new SchoolResponse(member.getSchool()));
        }
        return ResponseEntity.status(401).body(new ErrorResponse("인증되지 않은 사용자"));
    }

    // 학교별 페이지 접근 권한
    @GetMapping("/school/{schoolCode}")
    public ResponseEntity<?> checkSchoolAccess(@PathVariable String schoolCode) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String email = auth.getName();
            MembershipEntity member = membershipRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
            if (member.getSchool().equals(schoolCode)) {
                return ResponseEntity.ok(new AccessResponse(true));
            }
            return ResponseEntity.status(403).body(new AccessResponse(false));
        }
        return ResponseEntity.status(401).body(new ErrorResponse("인증되지 않은 사용자"));
    }

    record SchoolResponse(String school) {}
    record AccessResponse(boolean allowed) {}
    record ErrorResponse(String message) {}
}