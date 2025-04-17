package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Register;
import cl.bennu.assistcontrol.domain.query.RegisterQuery;
import cl.bennu.assistcontrol.request.SaveRegisterRequest;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
import cl.bennu.commons.exception.UniqueException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;

import java.sql.Date;
import java.util.List;

@ApplicationScoped
@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
public class RegisterResource extends BaseResource {

    private @Inject AssistControlService assistControlService;

    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<Register> registers = assistControlService.getAllRegistry(token);
        return Response.ok(registers).build();
    }

    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token,
                        @PathParam("id") Long id) {
        Register result = assistControlService.getRegistryById(token, id);
        return Response.ok(result).build();
    }


    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token,
                         @QueryParam("day") Date day,
                         @QueryParam("employeeId") Long employeeId) {
        RegisterQuery query = new RegisterQuery();
        query.setDay(day);
        if (employeeId != null) {
            query.setEmployeeId(employeeId);
        }
        List<Register> registers = assistControlService.findRegistryByQuery(token, query);
        return Response.ok(registers).build();
    }

    @POST
    @SneakyThrows
    public Response saveRegister(@HeaderParam("Authorization") String token,
                                 SaveRegisterRequest request)
            throws NoDataException, UniqueException {
        Register reg = assistControlService.processRegister(token, request);
        return Response.ok(reg).build();
    }

    @SneakyThrows
    @PUT
    public Response update(@HeaderParam("Authorization") String token, Register register) {
        if (register == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información de registro");
        }
        if (register.getId() == null) {
            throw new NoDataException("El id del registro es requerido para una actualización");
        }
        assistControlService.saveRegistry(token, register, HttpMethod.PUT);
        return Response.ok().build();
    }

    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Register register = assistControlService.deleteRegistryById(token, id);
        return Response.ok(register).build();
    }
}
