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
    private final ObjectProperty<LocalDate> dateOfContract;
    private final StringProperty sanBook;
    private final ObjectProperty<LocalDate> endOfVisa;
    private final StringProperty fileNumber;
    private final ObjectProperty<LocalDate> dateQuit;
private final StringProperty document;
private final StringProperty usualNote;
private final StringProperty criticalNote;
//
//   Empty Constructor
    public Person () {
this(0,null, null, null, null, null,
        null, null, null, null, null, null, null);
    }

//    Full constructor

    public Person(int id, String firstName, String lastName, String passport, String phone, LocalDate dataOfContract,
                  String sanBook, LocalDate endOfVisa, String fileNumber, LocalDate dateQuit, String document,
                  String usualNote, String criticalNote) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.passport = new SimpleStringProperty(passport);
        this.phone = new SimpleStringProperty(phone);
        this.dateOfContract = new SimpleObjectProperty<>(dataOfContract);
        this.sanBook = new SimpleStringProperty(sanBook);
        this.endOfVisa = new SimpleObjectProperty<>(endOfVisa);
        this.fileNumber = new SimpleStringProperty(fileNumber);
        this.dateQuit = new SimpleObjectProperty<>(dateQuit);
        this.document = new SimpleStringProperty(document);
        this.usualNote = new SimpleStringProperty(usualNote);
        this.criticalNote = new SimpleStringProperty(criticalNote);
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

    public void setDateOfContract(LocalDate dateOfContract) {
        this.dateOfContract.set(dateOfContract);
    }

    public LocalDate getDateOfContract() {
        return dateOfContract.get();
    }

    public ObjectProperty<LocalDate> dateOfContractProperty() {
        return dateOfContract;
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

    public void setDateQuit(LocalDate dateQuit) {this.dateQuit.set(dateQuit);}

    public LocalDate getDateQuit () {return dateQuit.get();}

    public ObjectProperty<LocalDate> dateQuitProperty (){return dateQuit; }

    public void setDocument(String document) {this.document.set(document);}

    public String getDocument() {return document.get();}

    public StringProperty documentProperty() {return document;}

    public void setUsualNote(String usualNote) {this.usualNote.set(usualNote);}

    public String getUsualNote() {return usualNote.get();}

    public StringProperty usualNoteProperty () {return usualNote;}


    public void setCriticalNote (String criticalNote) {this.criticalNote.set(criticalNote);}

    public String getCriticalNote () {return criticalNote.get();}

    public StringProperty criticalNoteProperty () {return criticalNote;}




}
