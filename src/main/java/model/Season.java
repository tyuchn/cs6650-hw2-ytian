package model;

public class Season {
    private Integer year;

    public Season (int year) {
        this.year = year;
    }

    public Integer getYear () {
        return this.year;
    }

    @Override
    public String toString() {
        return "Season{" +
                "year=" + year +
                '}';
    }
}
