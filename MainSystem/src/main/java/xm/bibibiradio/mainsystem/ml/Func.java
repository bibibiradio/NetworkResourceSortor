package xm.bibibiradio.mainsystem.ml;

public interface Func {
    public Item getResult(Item item) throws Exception;
    public Item getDeltaResult(Item item) throws Exception;
}
