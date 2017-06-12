package inter;

/**
 * Created by Huang on 2017/5/19.
 */
public interface Subject
{


    public void attach(Observer observer);

    public void detach(Observer observer);

    public void notifyMessage(String opType, String username);

}
