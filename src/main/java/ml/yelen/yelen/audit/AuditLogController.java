package ml.yelen.yelen.audit;

import ml.yelen.yelen.resources.URIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(URIs.ALL_AUDIT_LOG_URI)
public class AuditLogController {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @GetMapping
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }
}
