package model;

public class LiftRide {
    private Integer skierId;
    private Integer dayId;
    private Integer time;
    private Integer liftId;


    public LiftRide(Integer skierId, Integer dayId, Integer time, Integer liftId) {
        this.skierId = skierId;
        this.dayId = dayId;
        this.time = time;
        this.liftId = liftId;
    }

    public Integer getSkierId() {
        return skierId;
    }

    public void setSkierId(Integer skierId) {
        this.skierId = skierId;
    }


    public Integer getDayId() {
        return dayId;
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getLiftId() {
        return liftId;
    }

    public void setLiftId(Integer liftId) {
        this.liftId = liftId;
    }

    @Override
    public String toString() {
        return "LiftRide{" +
                "skierId=" + skierId +
                ", dayId=" + dayId +
                ", time=" + time +
                ", liftId=" + liftId +
                '}';
    }
}
