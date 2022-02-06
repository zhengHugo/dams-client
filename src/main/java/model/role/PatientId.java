package model.role;

import model.common.City;
import clients.ClientId;

public class PatientId implements ClientId {
  private final City city;
  private final int number;

  PatientId(City city, int number) {
    this.city = city;
    this.number = number;
  }

  public String getId() {
    return city.code + "P" + number;
  }

  @Override
  public City getCity() {
    return this.city;
  }

  @Override
  public String toString() {
    return this.getId();
  }
}
