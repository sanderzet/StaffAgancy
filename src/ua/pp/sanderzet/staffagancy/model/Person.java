package ua.pp.sanderzet.staffagancy.model;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Created by sander on 22.04.17.
 */
public class Person {
private  final IntegerProperty id;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty passport;
    private final StringProperty phone;
    private final ObjectProperty<LocalDate> dataOfContract;
    private final StringProperty sanBook;
    private final ObjectProperty<LocalDate> endOfVisa;
    private final StringProperty fileNumber;


//   Empty Constructor
    public Person () {
this(0,null, null, null, null, null, null, null, null);
    }

//    Full constructor

    public Person(int id, String firstName, String lastName, String passport, String phone, LocalDate dataOfContract, String sanBook,
                  LocalDate endOfVisa, String fileNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.passport = new SimpleStringProperty(passport);
        this.phone = new SimpleStringProperty(phone);
        this.dataOfContract = new SimpleObjectProperty<LocalDate>(dataOfContract);
        this.sanBook = new SimpleStringProperty(sanBook);
        this.endOfVisa = new SimpleObjectProperty<LocalDate>(endOfVisa);
        this.fileNumber = new SimpleStringProperty(fileNumber);
    }


public void setId(int id) {this.id.set(id);}
public int getId() {return id.get();}
public IntegerProperty idProperty() {return id;}

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setLastName(String lastName) {this.lastName.set(lastName); }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setPassport(String passport) {
        this.passport.set(passport);
    }

    public String getPassport() {
        return passport.get();
    }

    public StringProperty passportProperty() {
        return passport;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setDataOfContract(LocalDate dataOfContract) {
        this.dataOfContract.set(dataOfContract);
    }

    public LocalDate getDataOfContract() {
        return dataOfContract.get();
    }

    public ObjectProperty<LocalDate> dataOfContractProperty() {
        return dataOfContract;
    }

    public void setSanBook(String sanBook) {
        this.sanBook.set(sanBook);
    }

    public String getSanBook() {
        return sanBook.get();
    }

    public StringProperty sanBookProperty() {
        return sanBook;
    }

    public void setEndOfVisa(LocalDate endOfVisa) {
        this.endOfVisa.set(endOfVisa);
    }

    public LocalDate getEndOfVisa() {
        return endOfVisa.get();
    }

    public ObjectProperty<LocalDate> endOfVisaProperty() {
        return endOfVisa;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber.set(fileNumber);
    }

    public String getFileNumber() {
        return fileNumber.get();
    }

    public StringProperty fileNumberProperty() {
        return fileNumber;
    }



}
