package com.thoersch.examples.persistence.members;

import com.thoersch.examples.representations.members.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
