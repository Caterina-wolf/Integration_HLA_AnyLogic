package main.backend.it.cl.hla.master.DTO;

/*Class that rapresent the model for the Car*/

public class CarDTO {

    private String name;
    private String licensePlate;
    private String color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getColor() {
        return color;

    }

    public void setColor(String color) {
        this.color = color;
    }

}
