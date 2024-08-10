package ml.yelen.yelen.config;

import ml.yelen.yelen.audit.AuditLog;
import ml.yelen.yelen.entities.Administrator;
import ml.yelen.yelen.resources.enums.ActionType;
import ml.yelen.yelen.services.AdminInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DefaultAdminInitializer implements ApplicationRunner {
    @Autowired
    private AdminInitializationService adminInitializationService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AuditLog auditLog = new AuditLog(null, "Yelen System", ActionType.CREATE,
                LocalDateTime.now(), Administrator.class.getSimpleName(),
                "Création d'un nouveau administrateur");
        adminInitializationService.createDefaultAdmin(auditLog);
    }
}
