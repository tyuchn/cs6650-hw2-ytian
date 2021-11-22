package model;

public class Resort {
    private Integer year;
    private Integer resortId;

    public Resort(int year, int resortId) {
        this.year = year;
        this.resortId = resortId;
    }

    public Integer getYear () {
        return this.year;
    }

    public Integer getResortId() {
        return this.resortId;
    }

    @Override
    public String toString() {
        return "Resort{" +
                "year=" + year +
                ", resortId=" + resortId +
                '}';
    }
}
