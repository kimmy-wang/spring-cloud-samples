/*
 *
 * MIT License
 *
 * Copyright (c) 2019 hdmcp.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.hdmcp.core.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by WANGY
 *
 * @author WANGY
 */
public final class JwtUtil {

    private static final String SECRET = "qazwsx123444$#%#()*&& asdaswwi1235 ?;!@#kmmmpom in***xx**&";
    private static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_AUTH = "Authorization";

    public static final String EXPIRED = "Y";
    public static final String NOT_EXPIRED = "N";

    public static final String TOKEN_F_ID = "id";
    public static final String TOKEN_F_USERNAME = "username";
    public static final String TOKEN_F_IS_EXPIRED = "isExpired";

    public static synchronized String generateToken(String userId, String username) {

        HashMap<String, Object> map = new HashMap<>();
        map.put(TOKEN_F_ID, userId);
        map.put(TOKEN_F_USERNAME, username);
        String jwt = Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, UUID.randomUUID().toString())
                .setSubject("user info").setClaims(map)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .compact();
        return String.format("%s %s", TOKEN_PREFIX, jwt);
    }

    public static synchronized Map<String, String> validateToken(String token) {
        HashMap<String, String> map = new HashMap<>();
        try {
            Map<String, Object> body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            String id = (String) body.get(TOKEN_F_ID);
            String username = (String) body.get(TOKEN_F_USERNAME);
            map.put(TOKEN_F_ID, id);
            map.put(TOKEN_F_USERNAME, username);
            map.put(TOKEN_F_IS_EXPIRED, NOT_EXPIRED);
        } catch (ExpiredJwtException e) {
            map.put(TOKEN_F_IS_EXPIRED, EXPIRED);
        } catch (MalformedJwtException e) {

        }
        return map;
    }
}
