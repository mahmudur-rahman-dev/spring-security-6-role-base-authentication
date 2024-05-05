package global.digital.signage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Objects;

import static global.digital.signage.util.DateTimeUtil.getKoreanCurrentDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {
    @Column(name = "REGUSERID")
    @JsonIgnore
    private String registeredUserId;

    @Column(name = "REGDT")
    private Date registrationDate;

    @Column(name = "MODUSERID" )
    @JsonIgnore
    private String modifiedUserId;

    @Column(name = "MODDT")
    @UpdateTimestamp
    private Date modificationDate;

    @PrePersist
    protected void onCreate() {
        if(Objects.isNull(registrationDate)) registrationDate = getKoreanCurrentDateTime();
        if(Objects.isNull(modificationDate)) modificationDate = getKoreanCurrentDateTime();
        if(Objects.isNull(registeredUserId)) registeredUserId = "SYSTEM";
        if(Objects.isNull(modifiedUserId)) modifiedUserId = "SYSTEM";
    }

    @PreUpdate
    protected void onUpdate() {
        modificationDate = getKoreanCurrentDateTime();
        if(Objects.isNull(modifiedUserId)) modifiedUserId = "SYSTEM";
    }
}