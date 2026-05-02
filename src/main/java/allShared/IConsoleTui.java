package allShared;

public interface IConsoleTui {

    void title(String title, String subtitle);

    void section(String title);

    void check(String operation, Object result, String expected);

    void info(String operation, Object result);

    void note(String title, String message);
}
