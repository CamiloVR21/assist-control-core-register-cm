package cl.bennu.assistcontrol.domain;

import cl.bennu.commons.domain.base.BaseDomain;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)
public class Register extends BaseDomain implements Serializable {

    private Long employeeId;

    private LocalDate day;
    private LocalTime startOfTheDay;
    private LocalTime entry;
    private LocalTime exit;
    private LocalTime breakTime;
    private LocalTime backToWork;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Register valueOf(Long id) {
        Register obj = new Register();
        obj.setId(id);
        return obj;
    }
}
