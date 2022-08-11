package com.nhnacademy.marketgg.auth.session.rdb;

import com.nhnacademy.marketgg.auth.session.rdb.entity.SessionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionIdRepository extends JpaRepository<SessionId, String> {
}
