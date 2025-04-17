package cl.bennu.assistcontrol.domain.query;

import cl.bennu.assistcontrol.enums.ContractTypeEnum;
import cl.bennu.assistcontrol.enums.GenderEnum;
import cl.bennu.assistcontrol.enums.MaritalStatusEnum;
import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)
public class EmployeeQuery extends BaseDomain implements Serializable {

    private Long id;
    private Long branchId;
    private Long jobSchedulerId;
    private Long communeId;
    private Long countryId;
    private GenderEnum gender;
    private MaritalStatusEnum maritalStatus;
    private ContractTypeEnum contractType;
    private Long jobTypeId;
    private String code;
    private String name;
    private String lastName;
    private String motherLastName;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String address;
    private LocalDate contractDate;
    private LocalDate contractEndDate;
    private Boolean active;

}
