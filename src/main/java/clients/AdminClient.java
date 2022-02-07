package clients;

import api.Admin;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import logger.LoggerUtil;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;
import model.role.AdminId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdminClient {

  private final AdminId id;
  private Admin adminRemote;
  private final Logger logger;

  public AdminClient(AdminId id) {
    this.id = id;
    LoggerUtil.createLoggerByClientId(id);
    logger = LogManager.getLogger("logger." + id.getId());
    try {
      setAdminRemote();
    } catch (RemoteException | NotBoundException e) {
      e.printStackTrace();
    }
  }

  public void addAppointment(AppointmentId id, AppointmentType type, int capacity){
    try {
      boolean success = adminRemote.addAppointment(id, type, capacity);
      if (success) {
        logger.info("Added appointment: %s - %s".formatted(type, id));
      } else {
        logger.info("Unable to add appointment %s - %s".formatted(type, id));
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public void removeAppointment(AppointmentId id, AppointmentType type){
    try {
      String message = adminRemote.removeAppointment(id, type);
      logger.info(message);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public void listAppointmentAvailability (AppointmentType type) {
    try {
      var availabilities = adminRemote.listAppointmentAvailability(type);
      StringBuilder stringBuilder = new StringBuilder("Appointment availabilities of ");
      stringBuilder.append(type).append(" - ");
      for (var availability : availabilities) {
        stringBuilder
            .append(availability.appointmentId())
            .append(" ")
            .append(availability.availability())
            .append(", ");
      }
      // replace the last ", " with "."
      stringBuilder.replace(stringBuilder.length()- 2, stringBuilder.length(), ".");
      logger.info(stringBuilder.toString());
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  private void setAdminRemote() throws RemoteException, NotBoundException {
    Registry registry = LocateRegistry.getRegistry("localhost", 1099);
    adminRemote = (Admin) switch (id.getCity()) {
      case Montreal -> registry.lookup("AdminMTL");
      case Quebec -> registry.lookup("AdminQUE");
      case Sherbrooke -> registry.lookup("AdminSHE");
    };
  }


}
