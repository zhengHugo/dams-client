import clients.AdminClient;
import clients.PatientClient;
import java.text.ParseException;

import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentTime;
import model.appointment.AppointmentType;
import model.common.City;
import model.role.AdminId;
import model.role.PatientId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

  private static final Logger logger = LogManager.getLogger();

  public static void main(String[] args) {
    final AdminId mtlAdminId = new AdminId(City.Montreal, 1111);
    AdminClient mtlAdmin = new AdminClient(mtlAdminId);
    final PatientId mtlPatientId = new PatientId(City.Montreal, 1111);
    PatientClient mtlPatient = new PatientClient(mtlPatientId);
    try {
      AppointmentId appId1 =
          new AppointmentId(City.Montreal, AppointmentTime.Afternoon, "10022022");
      mtlAdmin.addAppointment(appId1, AppointmentType.Surgeon, 3);
      var appId2 = new AppointmentId(City.Montreal, AppointmentTime.Morning, "11022022");
      mtlAdmin.addAppointment(appId2, AppointmentType.Physician, 4);
      mtlPatient.bookAppointment(appId1, AppointmentType.Surgeon);
      mtlPatient.getAppointmentSchedule();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    //    final AdminId queAdminId = new AdminId(City.Quebec, 1112);
    //    final AdminId sheAdminId = new AdminId(City.Sherbrooke, 1113);
    //    AdminClient queAdmin = new AdminClient(queAdminId);
    //    AdminClient sheAdmin = new AdminClient(sheAdminId);

  }
}
