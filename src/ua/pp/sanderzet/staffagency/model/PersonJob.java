package ua.pp.sanderzet.staffagency.model;

import javafx.beans.property.StringProperty;

/**
 * Created by alzet on 14.06.17.
 */
public class PersonJob {
    private final StringProperty name;
    private final StringProperty phone;
    private final StringProperty firm;
    private final StringProperty place;
    private final StringProperty position;

//    Full constructor

    public PersonJob(StringProperty name, StringProperty phone, StringProperty firm, StringProperty place, StringProperty position) {
        this.name = name;
        this.phone = phone;
        this.firm = firm;
        this.place = place;
        this.position = position;
    }
// Empty constructor
    public PersonJob() {
        this(null, null, null, null, null);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getFirm() {
        return firm.get();
    }

    public StringProperty firmProperty() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm.set(firm);
    }

    public String getPlace() {
        return place.get();
    }

    public StringProperty placeProperty() {
        return place;
    }

    public void setPlace(String place) {
        this.place.set(place);
    }

    public String getPosition() {
        return position.get();
    }

    public StringProperty positionProperty() {
        return position;
    }

    public void setPosition(String position) {
        this.position.set(position);
    }
}
