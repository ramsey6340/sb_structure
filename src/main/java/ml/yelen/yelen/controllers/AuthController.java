package ml.yelen.yelen.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ml.yelen.yelen.audit.AuditLog;
import ml.yelen.yelen.dto.AuthDTO;
import ml.yelen.yelen.dto.JwtResponseDTO;
import ml.yelen.yelen.entities.Administrator;
import ml.yelen.yelen.exceptions.BadRequestException;
import ml.yelen.yelen.exceptions.ForbiddenRequestException;
import ml.yelen.yelen.pojo.ResponseMessage;
import ml.yelen.yelen.resources.ConstanteValues;
import ml.yelen.yelen.resources.ErrorMessageValue;
import ml.yelen.yelen.resources.URIs;
import ml.yelen.yelen.resources.enums.ActionType;
import ml.yelen.yelen.security.JwtService;
import ml.yelen.yelen.services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@RequestMapping(URIs.AUTH_PREFIX)
@RestController
@AllArgsConstructor
public class AuthController {
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private AdminService  adminService;

    @GetMapping(URIs.REFRESH_TOKEN_URI)
    @Operation(summary = "Obtenir un nouveau access token")
    public ResponseEntity<ResponseMessage> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(ConstanteValues.AUTHORIZATION);
        if(authHeader != null && authHeader.startsWith(ConstanteValues.BEARER)) {
            String refreshToken = authHeader.substring(ConstanteValues.BEARER_TEXT_LENGTH);
            String username = jwtService.extractUsername(refreshToken);
            return ResponseEntity.ok(ResponseMessage.builder()
                            .body(JwtResponseDTO.builder()
                                    .accessToken(jwtService.GenerateToken(username)).refreshToken(refreshToken).build())
                            .status(HttpStatus.OK.value())
                            .date(LocalDateTime.now())
                            .path(URIs.AUTH_PREFIX+URIs.REFRESH_TOKEN_URI)
                            .message("Creation d'un nouveau access token")
                            .error(null)
                            .build());
        }
        throw new BadRequestException("Erreur d'authentification");
    }

    @PostMapping(URIs.LOGIN_URI)
    public ResponseEntity<ResponseMessage> AuthenticateAndGetToken(@RequestBody AuthDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(),
                        authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            JwtResponseDTO body = JwtResponseDTO.builder()
                    .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
                    .refreshToken(jwtService.GenerateRefreshToken(authRequestDTO.getUsername()))
                    .build();
            String username = jwtService.extractUsername(body.getAccessToken());
            AuditLog auditLog = new AuditLog(null,
                    username, ActionType.LOGIN, LocalDateTime.now(),
                    Administrator.class.getSimpleName(), "Connexion à son compte");
            adminService.loginAdmin(auditLog);
            return ResponseEntity.ok(ResponseMessage.builder()
                            .body(body)
                            .status(HttpStatus.OK.value())
                            .path(URIs.AUTH_PREFIX+URIs.LOGIN_URI)
                            .message("Connexion de l'utilisateur")
                            .date(LocalDateTime.now())
                            .error(null)
                            .build());
        } else {
            throw new UsernameNotFoundException(ErrorMessageValue.USER_NOT_FOUND);
        }
    }

    @PostMapping(URIs.CREATE_ADMIN_URI)
    @Operation(summary = "Creation d'un nouveau utilisateur")
    public ResponseEntity<ResponseMessage> createAdmin(@RequestBody Administrator administrator, HttpServletRequest request) {
        String authHeader = request.getHeader(ConstanteValues.AUTHORIZATION);
        if(authHeader!=null && authHeader.startsWith(ConstanteValues.BEARER)) {
            String token = authHeader.substring(ConstanteValues.BEARER_TEXT_LENGTH);
            String username = jwtService.extractUsername(token);
            AuditLog auditLog = new AuditLog(null,
                    username, ActionType.CREATE, LocalDateTime.now(),
                    Administrator.class.getSimpleName(), "Création d'un nouveau administrateur");
            return adminService.createAdmin(auditLog, administrator);
        }
        throw new ForbiddenRequestException(ErrorMessageValue.ACCESS_FORBIDDEN);
    }
}
