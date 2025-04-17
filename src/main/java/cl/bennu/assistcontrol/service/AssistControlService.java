package cl.bennu.assistcontrol.service;

import cl.bennu.assistcontrol.domain.Register;
import cl.bennu.assistcontrol.domain.query.RegisterQuery;
import cl.bennu.assistcontrol.mapper.RegisterMapper;
import cl.bennu.assistcontrol.request.SaveRegisterRequest;
import cl.bennu.commons.exception.NoDataException;
import cl.bennu.commons.exception.UniqueException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class AssistControlService {

    @Inject
    RegisterMapper registerMapper;

    public Register getRegistryById(String token, Long id) throws NoDataException {
        if (id == null) {
            throw new NoDataException("No se especificó el ID del registro.");
        }
        Register found = registerMapper.get(id);
        if (found == null) {
            throw new NoDataException("Registro no encontrado: ID " + id);
        }
        return found;
    }

    public List<Register> getAllRegistry(String token) {
        return registerMapper.getAll();
    }

    public List<Register> findRegistryByQuery(String token, RegisterQuery q) {
        return registerMapper.findByQuery(q);
    }

    @Transactional(rollbackOn = {NoDataException.class, UniqueException.class})
    public void saveRegistry(String token, Register r, String method)
            throws NoDataException, UniqueException {
        validateRegistry(r, method);
        if ("POST".equalsIgnoreCase(method)) {
            registerMapper.insert(r);
        } else {
            registerMapper.update(r);
        }
    }

    private void validateRegistry(Register r, String method)
            throws NoDataException, UniqueException {
        if (r == null) {
            throw new NoDataException("La información del registro está vacía");
        }
        if (r.getEmployeeId() == null) {
            throw new NoDataException("Debe especificar employeeId");
        }
        if (r.getDay() == null) {
            throw new NoDataException("Se requiere el campo day");
        }
        if (r.getStartOfTheDay() == null) {
            throw new NoDataException("Se requiere el campo startOfTheDay");
        }
        if (r.getEntry() == null) {
            throw new NoDataException("Se requiere el campo entry");
        }
        if (r.getExit() == null) {
            throw new NoDataException("Se requiere el campo exit");
        }

        // Unicidad: mismo employeeId + misma fecha + misma entrada/salida
        RegisterQuery q = new RegisterQuery();
        q.setEmployeeId(r.getEmployeeId());
        q.setDay(Date.valueOf(r.getDay()));
        q.setEntry(Time.valueOf(r.getEntry()));
        q.setExit(Time.valueOf(r.getExit()));
        List<Register> existing = registerMapper.findByQuery(q);

        if ("POST".equalsIgnoreCase(method)) {
            if (!existing.isEmpty()) {
                throw new UniqueException("Ya existe registro para ese empleado y horario");
            }
        } else {
            for (Register ex : existing) {
                if (!ex.getId().equals(r.getId())) {
                    throw new UniqueException("Conflicto: otro registro con mismos datos");
                }
            }
        }
    }

    public Register deleteRegistryById(String token, Long id) throws NoDataException {
        if (id == null) {
            throw new NoDataException("Se requiere el ID del registro para eliminar");
        }
        Register existing = registerMapper.get(id);
        if (existing == null) {
            throw new NoDataException("Registro no encontrado: ID " + id);
        }
        registerMapper.delete(id);
        return existing;
    }

    @Transactional(rollbackOn = {NoDataException.class, UniqueException.class})
    public Register processRegister(String token, SaveRegisterRequest req)
            throws NoDataException, UniqueException {
        if (req == null) {
            throw new NoDataException("Solicitud de registro vacía");
        }
        Register r = new Register();
        r.setEmployeeId(req.getEmployeeId());
        r.setDay(req.getDay() != null ? req.getDay() : LocalDate.now());
        r.setStartOfTheDay(req.getStartOfTheDay() != null ? req.getStartOfTheDay() : LocalTime.now());
        r.setEntry(req.getEntry() != null ? req.getEntry() : LocalTime.now());
        r.setExit(req.getExit() != null ? req.getExit() : LocalTime.now());
        r.setBreakTime(req.getBreakTime());
        r.setBackToWork(req.getBackToWork());

        saveRegistry(token, r, "POST");
        return r;
    }
}
