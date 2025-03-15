package com.members.membersService.controller;

import com.members.membersService.model.Member;
import com.members.membersService.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public Member addMember(@RequestBody Member member) {
        return memberService.registerMember(member);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }
}
