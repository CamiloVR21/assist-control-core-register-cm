package cl.bennu.assistcontrol.request;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SaveRegisterRequest {

    private Long employeeId;
    private LocalDate day;
    private LocalTime startOfTheDay;
    private LocalTime entry;
    private LocalTime exit;
    private LocalTime breakTime;
    private LocalTime backToWork;
}
