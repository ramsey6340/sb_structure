package ml.yelen.yelen.services;

import ml.yelen.yelen.audit.AuditLog;
import ml.yelen.yelen.entities.Administrator;
import ml.yelen.yelen.pojo.ResponseMessage;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    ResponseEntity<ResponseMessage> createAdmin(AuditLog auditLog, Administrator administrator);
    void loginAdmin(AuditLog auditLog);
}
