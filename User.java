import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private List<String> results;

    public User(String username) {
        this.username = username;
        this.results = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public List<String> getResults() {
        return results;
    }

    public void addResult(String result) {
        results.add(result);
    }
}
