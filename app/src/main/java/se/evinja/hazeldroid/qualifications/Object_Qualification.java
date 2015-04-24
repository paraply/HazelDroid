package se.evinja.hazeldroid.qualifications;


public class Object_Qualification {
    public String title;
    private int amount;

    public Object_Qualification(String title, int amount){
        this.title = title;
        this.amount = amount;
    }

    public String getWorkerString(){
        return Integer.toString(amount) + " workers has this qualification";
    }
}
