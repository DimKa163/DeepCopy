public class Women {
    private String name;
    private Man man;
    public Women(String name, Man man){
        this.name = name;
        this.man = man;
    }

    public String getName(){
        return name;
    }

    public Man getMan(){
        return man;
    }
}
