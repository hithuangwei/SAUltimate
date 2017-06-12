package inter;

/**
 * Created by Huang on 2017/5/19.
 */
public interface Observer
{
    public void updateMessage(Subject item, String opType, String username);

}
