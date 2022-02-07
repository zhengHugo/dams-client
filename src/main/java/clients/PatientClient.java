package clients;

import api.Patient;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import logger.LoggerUtil;
import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;
import model.role.PatientId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PatientClient {

  private final PatientId id;
  private Patient patientRemote;
  private final Logger logger;

  public PatientClient(PatientId id) {
    this.id = id;
    LoggerUtil.createLoggerByClientId(id);
    logger = LogManager.getLogger("logger." + id.getId());
    try {
      setPatientRemote();
    } catch (NotBoundException | RemoteException e) {
      e.printStackTrace();
    }
  }

  public void bookAppointment(AppointmentId appointmentId, AppointmentType type) {
    try {
      boolean success = patientRemote.bookAppointment(this.id, appointmentId, type);
      if (success) {
        logger.info("Booked appointment: %s - %s".formatted(type, appointmentId));
      } else {
        logger.info("Unable to book appointment: %s - %s".formatted(type, appointmentId));
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public List<AppointmentId> getAppointmentSchedule() {
    try {
      var appointmentIds = patientRemote.getAppointmentSchedule(this.id);
      logger.info("Get appointment schedule: %s".formatted(
          appointmentIds.stream()
              .map(appointment -> appointment.getId() + " ")
              .reduce(String::concat)
              .orElse("")
      ));
      return appointmentIds;
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean cancelAppointment(AppointmentType type, AppointmentId id) {
    try {
      return patientRemote.cancelAppointment(this.id, type, id);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    return false;
  }

  private void setPatientRemote() throws RemoteException, NotBoundException {
    Registry registry = LocateRegistry.getRegistry("localhost", 1099);
    patientRemote=  (Patient) switch (id.getCity()) {
      case Montreal -> registry.lookup("PatientMTL");
      case Quebec -> registry.lookup("PatientQUE");
      case Sherbrooke -> registry.lookup("PatientSHE");
    };
  }
}
