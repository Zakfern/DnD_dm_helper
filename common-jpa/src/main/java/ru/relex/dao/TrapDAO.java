package ru.relex.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.relex.entity.AppUser;
import ru.relex.entity.Trap;

public interface TrapDAO extends JpaRepository<Trap, Long> {
}
