package ua.pp.sanderzet.staffagancy.model;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Created by sander on 22.04.17.
 */
public class Job {
    private final IntegerProperty rowid;
    private final IntegerProperty idPerson;
    private final StringProperty place;
    private final StringProperty firm;
    private final StringProperty position;
    private final ObjectProperty<LocalDate> start;
    private final ObjectProperty<LocalDate> end;

//    Default constructor

    public Job()
    {
        this(0,0, null,null,null,null,null);
    }

    public Job(Integer rowid, Integer idPerson, String place, String firm, String position, LocalDate start, LocalDate end) {
        this.rowid = new SimpleIntegerProperty(rowid);
        this.idPerson = new SimpleIntegerProperty(idPerson);
        this.place = new SimpleStringProperty(place);
        this.firm = new SimpleStringProperty(firm);
        this.position = new SimpleStringProperty(position);
        this.start = new SimpleObjectProperty<>(start);
        this.end = new SimpleObjectProperty<>(end);
    }

    public void setRowid(int rowid) {
        this.rowid.set(rowid);
    }

    public int getRowid() {
        return rowid.get();
    }

    public IntegerProperty rowidProperty() {
        return rowid;
    }

    public void setIdPerson(Integer idPerson) {this.idPerson.set(idPerson);}
public int getIdPerson () { return idPerson.get();}
public IntegerProperty idPersonProperty() {return idPerson;}
    public void setPlace(String place) {
        this.place.set(place);
    }

    public String getPlace() {
        return place.get();
    }

    public StringProperty placeProperty() {
        return place;
    }

    public void setFirm(String firm) {
        this.firm.set(firm);
    }

    public String getFirm() {
        return firm.get();
    }

    public StringProperty firmProperty() {
        return firm;
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public String getPosition() {
        return position.get();
    }

    public StringProperty positionProperty() {
        return position;
    }

    public void setStart(LocalDate start) {
        this.start.set(start);
    }

    public LocalDate getStart() {
        return start.get();
    }

    public ObjectProperty<LocalDate> startProperty() {
        return start;
    }

    public void setEnd(LocalDate end) {
        this.end.set(end);
    }

    public LocalDate getEnd() {
        return end.get();
    }

    public ObjectProperty<LocalDate> endProperty() {
        return end;
    }
}
