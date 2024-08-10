package ml.yelen.yelen.services;

import ml.yelen.yelen.audit.AuditLog;
import ml.yelen.yelen.entities.AdminRole;
import ml.yelen.yelen.entities.Administrator;
import ml.yelen.yelen.repositories.AdminRepository;
import ml.yelen.yelen.repositories.AdminRoleRepository;
import ml.yelen.yelen.resources.enums.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminInitializationService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AdminRoleRepository adminRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createDefaultAdmin(AuditLog auditLog) {
        if (adminRepository.count() == 0) {
            AdminRole adminRole = new AdminRole();
            adminRole.setName(RoleName.ADMIN.name());
            adminRoleRepository.save(adminRole);

            Administrator admin = new Administrator();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("@dmin321"));
            admin.getRoles().add(adminRole);

            adminRepository.save(admin);
        }
    }
}

