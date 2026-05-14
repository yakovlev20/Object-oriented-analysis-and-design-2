import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DatabaseManager {
    private Connection connection;
    private HashMap<String, GameObject> cache = new HashMap<>();

    public DatabaseManager() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:game_objects.db");
        createTable();
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS game_objects (" +
                "id TEXT PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "type TEXT NOT NULL)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    // Сохранение объекта в БД
    public void save(GameObject obj) throws SQLException {
        String sql = "INSERT OR REPLACE INTO game_objects(id, name, type) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, obj.getId());
            pstmt.setString(2, obj.getName());
            pstmt.setString(3, obj.getClass().getSimpleName());
            pstmt.executeUpdate();
            cache.put(obj.getId(), obj); // Обновляем кэш
        }
    }

    // Получение объекта из БД
    public GameObject load(String id) throws SQLException {
        // Сначала проверяем кэш
        if (cache.containsKey(id)) {
            return cache.get(id);
        }

        String sql = "SELECT * FROM game_objects WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                GameObject obj = new Character(rs.getString("id"), rs.getString("name"));
                cache.put(id, obj); // Сохраняем в кэш
                return obj;
            }
        }
        return null;
    }

    // Удаление объекта из БД
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM game_objects WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            cache.remove(id); // Удаляем из кэша
        }
    }

    // Загрузка всех объектов
    public Collection<GameObject> loadAll() throws SQLException {
        Collection<GameObject> result = new ArrayList<>();
        String sql = "SELECT * FROM game_objects";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                GameObject obj = new Character(rs.getString("id"), rs.getString("name"));
                result.add(obj);
                cache.put(obj.getId(), obj);
            }
        }
        return result;
    }
}
