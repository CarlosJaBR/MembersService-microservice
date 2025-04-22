package com.members.membersService.controller;

import com.members.membersService.model.Member;
import com.members.membersService.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/")
    @Operation(
        summary = "Add a new member",
        description = "This endpoint allows registering a new member in the system. " +
                      "The member must provide valid information, and the user making the request must have the ADMIN role."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Member added successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden, ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    public Member addMember(@RequestBody Member member) {
        return memberService.registerMember(member);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    @Operation(
        summary = "Get all members",
        description = "This endpoint retrieves a list of all members registered in the system. " +
                      "The ADMIN role is required to access this information."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of members retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden, ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }
    @GetMapping("/debug-token")
    public ResponseEntity<?> debugToken(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se recibió token JWT");
        }
        return ResponseEntity.ok(jwt.getClaims());
    }
}
