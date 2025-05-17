package dev.gaau.messenger.repository;

import dev.gaau.messenger.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}