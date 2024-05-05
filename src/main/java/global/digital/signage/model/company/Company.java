package global.digital.signage.model.company;

import com.fasterxml.jackson.annotation.JsonFormat;
import global.digital.signage.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "companies")
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @Column(length = 60)
    private String companyName;

    @Column(length = 60)
    private String companyCity;

    @Column(length = 20)
    private String companyPhone;

    @Column(name = "company_email", length = 60)
    private String companyEmail;

    private Integer numberOfLicense;

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    private String companyAddress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date companyExpiryDate;
}
