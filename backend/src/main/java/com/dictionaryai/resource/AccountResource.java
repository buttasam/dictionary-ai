package com.dictionaryai.resource;

import com.dictionaryai.dao.UserDao;
import com.dictionaryai.model.CredentialsHash;
import com.dictionaryai.utils.PasswordUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;

@Path("/account")
@ApplicationScoped
public class AccountResource {

    @Inject
    public AccountResource(UserDao userDao) {
        this.userDao = userDao;
    }

    private final UserDao userDao;

    @Path("/signup")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUp(@Valid @NotNull SignUpRequest signUpRequest) {
        if (!signUpRequest.password().equals(signUpRequest.passwordAgain())) {
            throw new BadRequestException("Passwords do not match");
        }

        if (userDao.userExists(signUpRequest.username())) {
            throw new BadRequestException("Username already exists");
        }

        byte[] salt = PasswordUtils.generateSalt();
        byte[] passwordHash = PasswordUtils.hashPassword(signUpRequest.password(), salt);

        CredentialsHash credentialsHash = new CredentialsHash(passwordHash, salt);
        userDao.insertUser(signUpRequest.username(), credentialsHash);

        return Response.ok().build();
    }

    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public LoginResponse login(@Valid @NotNull Credentials credential) {
        CredentialsHash credentialsHash = userDao.findByUserName(credential.username())
                .orElseThrow(() -> new NotFoundException("User not found"));

        byte[] passwordHashCheck = PasswordUtils.hashPassword(credential.password(), credentialsHash.salt());

        if (Arrays.equals(passwordHashCheck, credentialsHash.passwordHash())) {
            String accessToken = PasswordUtils.generateAccessToken();
            userDao.updateAccessToken(credential.username(), accessToken);
            return new LoginResponse(accessToken);
        }

        throw new BadRequestException("Invalid credentials");
    }

    public record Credentials(String username, String password) {
    }

    public record SignUpRequest(@NotBlank String username, @NotBlank String password, @NotBlank String passwordAgain) {
    }

    public record LoginResponse(String accessToken) {
    }

}
