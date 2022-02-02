import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;

import api.Admin;
import model.City;
import model.appointment.AppointmentId;
import model.appointment.AppointmentTime;
import model.appointment.AppointmentType;
import model.common.ClientId;
import model.role.AdminId;
import model.role.PatientId;

public class Main {
  public static void main(String[] args) {
    final AdminId ID = new AdminId(City.Montreal, 1111);
    try {
      Admin admin = (Admin) getAssociatedRemote(ID);
      assert admin != null;
      admin.addAppointment(new AppointmentId(City.Montreal, AppointmentTime.Afternoon, "10022022"), AppointmentType.Physician, 3);
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
