package ml.yelen.yelen.services;

import lombok.AllArgsConstructor;
import ml.yelen.yelen.audit.AuditLog;
import ml.yelen.yelen.entities.Administrator;
import ml.yelen.yelen.exceptions.BadRequestException;
import ml.yelen.yelen.pojo.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{


    @Override
    public ResponseEntity<ResponseMessage> createAdmin(AuditLog auditLog, Administrator administrator) {
        try {
            return null;
        }catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public void loginAdmin(AuditLog auditLog) {}
}
