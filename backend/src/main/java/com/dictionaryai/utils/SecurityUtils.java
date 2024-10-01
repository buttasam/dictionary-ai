package com.dictionaryai.utils;

import com.dictionaryai.security.UserDetails;
import jakarta.ws.rs.core.SecurityContext;

public class SecurityUtils {

    public static UserDetails extractUserDetails(SecurityContext securityContext) {
        return (UserDetails) securityContext.getUserPrincipal();
    }

}
