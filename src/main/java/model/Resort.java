package model;

public class Resort {
    private Integer dayId;
    private Integer year;
    private Integer time;
    private Integer resortId;
    private Integer skierId;
    private Integer liftId;

    public Resort(int dayId, int year, int time, int resortId, int skierId, int liftId) {
        this.dayId = dayId;
        this.year = year;
        this.time = time;
        this.resortId = resortId;
        this.skierId = skierId;
        this.liftId = liftId;
    }

    public Integer getDayId() {
        return dayId;
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getResortId() {
        return resortId;
    }

    public void setResortId(Integer resortId) {
        this.resortId = resortId;
    }

    public Integer getSkierId() {
        return skierId;
    }

    public void setSkierId(Integer skierId) {
        this.skierId = skierId;
    }

    public Integer getLiftId() {
        return liftId;
    }

    public void setLiftId(Integer liftId) {
        this.liftId = liftId;
    }

    @Override
    public String toString() {
        return "Resort{" +
                "dayId=" + dayId +
                ", year=" + year +
                ", time=" + time +
                ", resortId=" + resortId +
                ", skierId=" + skierId +
                ", liftId=" + liftId +
                '}';
    }

}
