package com.secondbrainai.utils;

import com.secondbrainai.security.UserDetails;
import jakarta.ws.rs.core.SecurityContext;

public class SecurityUtils {

    public static UserDetails extractUserDetails(SecurityContext securityContext) {
        return (UserDetails) securityContext.getUserPrincipal();
    }

}
