package com.dictionaryai.security;

import com.dictionaryai.dao.UserDao;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.security.Principal;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class TokenAuthFilter implements ContainerRequestFilter {

    private static final String BEARER = "Bearer ";

    private final UserDao userDao;

    @Inject
    public TokenAuthFilter(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(BEARER)) {
            userDao.findUserByAccessToken(authHeader.substring(BEARER.length()))
                    .ifPresentOrElse(
                            userDetails -> requestContext.setSecurityContext(new SecurityContext() {
                                @Override
                                public Principal getUserPrincipal() {
                                    return userDetails;
                                }

                                @Override
                                public boolean isUserInRole(String role) {
                                    return false;
                                }

                                @Override
                                public boolean isSecure() {
                                    return false;
                                }

                                @Override
                                public String getAuthenticationScheme() {
                                    return "";
                                }
                            }),
                            () -> requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build()));
        } else {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
