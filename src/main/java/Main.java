import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;

import api.Admin;
import model.appointment.AppointmentId;
import model.appointment.AppointmentTime;
import model.appointment.AppointmentType;
import model.common.City;
import model.common.ClientId;
import model.role.AdminId;
import model.role.PatientId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

  private static final Logger logger = LogManager.getLogger();

  public static void main(String[] args) {
    final AdminId ID = new AdminId(City.Montreal, 1111);
    try {
      Admin admin = (Admin) getAssociatedRemote(ID);
      assert admin != null;
      var appointmentId = new AppointmentId(City.Montreal, AppointmentTime.Afternoon, "10022022");
      admin.addAppointment(appointmentId, AppointmentType.Physician, 3);
      admin.removeAppointment(appointmentId, AppointmentType.Physician);
    } catch (RemoteException | NotBoundException | ParseException e) {
      e.printStackTrace();
    }

  }

  private static Remote getAssociatedRemote(ClientId id) throws RemoteException, NotBoundException {
    Registry registry = LocateRegistry.getRegistry("localhost", 1099);
    if (id.getClass() == AdminId.class) {
      return switch (id.getCity()) {
        case Montreal -> registry.lookup("AdminMTL");
        case Quebec -> registry.lookup("AdminQUE");
        case Sherbrooke -> registry.lookup("AdminSHE");
      };
    } else if (id.getClass() == PatientId.class){
      return switch (id.getCity()) {
        case Montreal -> registry.lookup("PatientMTL");
        case Quebec -> registry.lookup("PatientQUE");
        case Sherbrooke -> registry.lookup("PatientSHE");
      };
    }
    return null;
  }
}
