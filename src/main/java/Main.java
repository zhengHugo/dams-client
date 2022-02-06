import clients.AdminClient;
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
import clients.ClientId;
import model.role.AdminId;
import model.role.PatientId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

  private static final Logger logger = LogManager.getLogger();

  public static void main(String[] args) {
    final AdminId mtlAdminId = new AdminId(City.Montreal, 1111);
    final AdminId queAdminId = new AdminId(City.Quebec, 1112);
    final AdminId sheAdminId = new AdminId(City.Sherbrooke, 1113);
    AdminClient mtlAdmin = new AdminClient(mtlAdminId);
    AdminClient queAdmin = new AdminClient(queAdminId);
    AdminClient sheAdmin = new AdminClient(sheAdminId);
    try {
      var appId1 = new AppointmentId(City.Montreal, AppointmentTime.Afternoon, "10022022");
      mtlAdmin.addAppointment(appId1, AppointmentType.Surgeon, 3);
      var appId2 = new AppointmentId(City.Quebec, AppointmentTime.Afternoon, "11022022");
      queAdmin.addAppointment(appId2, AppointmentType.Surgeon, 4);
      var appId3 = new AppointmentId(City.Sherbrooke, AppointmentTime.Afternoon, "10022022");
      sheAdmin.addAppointment(appId3, AppointmentType.Physician, 3);
      mtlAdmin.listAppointmentAvailability(AppointmentType.Surgeon);
    } catch (ParseException e) {
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
