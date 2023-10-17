public class DataModel {
    private int year;
    private String[] states;

    public DataModel(int year, String[] states) {
        this.year = year;
        this.states = states;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }
}
