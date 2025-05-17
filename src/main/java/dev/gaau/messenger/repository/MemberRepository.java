package dev.gaau.messenger.repository;

import dev.gaau.messenger.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}