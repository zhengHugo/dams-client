import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import api.Admin;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;

public class Main {
  public static void main(String[] args) {
    try {
      Registry registry = LocateRegistry.getRegistry("localhost", 1099);
      Admin admin = (Admin) registry.lookup("Admin");
      admin.addAppointment(new AppointmentId("MTLA101122"), AppointmentType.Physician, 3);
    } catch (RemoteException | NotBoundException e) {
      e.printStackTrace();
    }

  }
}
