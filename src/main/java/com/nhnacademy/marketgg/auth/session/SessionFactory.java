package com.nhnacademy.marketgg.auth.session;

import com.nhnacademy.marketgg.auth.session.rdb.RdbSession;
import com.nhnacademy.marketgg.auth.session.rdb.entity.SessionId;
import com.nhnacademy.marketgg.auth.session.redis.RedisSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionFactory {

    public static RedisSession redis(String sessionId) {
        return new RedisSession(sessionId);
    }

    public static RdbSession rdb(SessionId sessionId) {
        return new RdbSession(sessionId);
    }

}
