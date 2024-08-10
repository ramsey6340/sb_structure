package ml.yelen.yelen.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ml.yelen.yelen.resources.enums.ActionType;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType action;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime date;
    @Column(nullable = false)
    private String resource;
    @Column
    private String details;

    // Standard getters and setters
}
