package data;
import data.*;
import controllers.*;

import server.*;
import client.*;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    String Name;
    String Country;
    int Age;                            //in years
    double Height;                      //in meters
    String Club;
    String Position;                    // (can be Batsman, Baller, Wicketkeeper, Allrounder)
    int Number;     
    String noNumber;                    //(Jersey number)
    long Weekly_Salary;
    boolean available;

    public Player(String Name, String Country, int Age, double Height, String Club, String Position, int Number, long Weekly_Salary, boolean available) {
        this.Name = Name;
        this.Country = Country;
        this.Age = Age;
        this.Height = Height;
        this.Club = Club;
        this.Position = Position;
        this.Number = Number;
        this.Weekly_Salary = Weekly_Salary;
        this.available = available;
    
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setAge(int age) {
        Age = age;
    }

    public void setHeight(double height) {
        Height = height;
    }

    public void setClub(String club) {
        Club = club;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public void setWeekly_Salary(long weekly_Salary) {
        Weekly_Salary = weekly_Salary;
    }

    public String getCountry() {
        return Country;
    }

    public int getAge() {
        return Age;
    }

    public double getHeight() {
        return Height;
    }

    public String getClub() {
        return Club;
    }

    public String getPosition() {
        return Position;
    }

    public int getNumber() {
        return Number;
    }

    public long getWeekly_Salary() {
        return Weekly_Salary;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }



    
    @Override
    public String toString() {
        
        return "Name: " + Name + "\n" +
        "Country: " + Country + "\n" +
        "Age: " + Age + "\n" +
        "Height: " + Height + " meters\n" +
        "Club: " + Club + "\n" +
        "Position: " + Position + "\n" +
        "Jersey Number: " + (Number == -1 ? "N/A" : Number) + "\n" +
        "Weekly Salary: " + Weekly_Salary+"\n";
    }
    public String getNoNumber() {
        return noNumber;
    }



}
