package ml.yelen.yelen.audit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuditAspectService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Before("execution(* ml.yelen.yelen.services.*.*(..)) && args(auditLog,..)")
    public void logActivity(AuditLog auditLog) {
        auditLogRepository.save(auditLog);
    }
}
