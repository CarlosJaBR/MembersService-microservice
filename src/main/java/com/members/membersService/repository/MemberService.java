package com.members.membersService.repository;

import com.members.membersService.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberService extends JpaRepository<Member, Long> {}
