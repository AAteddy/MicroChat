
package com.tedsaasfaha.MicroChat.util;

import io.github.cdimascio.dotenv.Dotenv;

public class JwtConstant {
    private static final Dotenv dotenv = Dotenv.load();

    // JWT Configuration
    public static final String SECRET_KEY = dotenv.get("JWT_SECRET_KEY");
    public static final long ACCESS_TOKEN_VALIDITY = 2 * 60 * 60 * 1000; // 2 hrs
    public static final long REFRESH_TOKEN_VALIDITY = 12 * 60 * 60 * 1000; // 12 hrs


    // Gmail SMTP Configuration
    public static final String GMAIL_USERNAME = dotenv.get("GMAIL_USERNAME", "default-email@gmail.com");
    public static final String GMAIL_PASSWORD = dotenv.get("GMAIL_PASSWORD", "default-password");
}
//
