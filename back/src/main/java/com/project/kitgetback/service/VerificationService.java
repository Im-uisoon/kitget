package com.project.kitgetback.service;

import com.project.kitgetback.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class VerificationService {
    private final MemberRepository memberRepository;

    public VerificationService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean verifyMember(String school, String name, String code) {
        return memberRepository.existsBySchoolAndNameAndCode(school, name, code);
    }
}