import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

// Убираем дженерик — DatabaseManager должен быть конкретным классом
class SimpleGameObjectManager {
    private DatabaseManager db;

    public SimpleGameObjectManager() {
        try {
            this.db = new DatabaseManager();
        } catch (SQLException e) {
            System.err.println("Ошибка инициализации базы данных:");
            e.printStackTrace();
            throw new RuntimeException("Не удалось инициализировать базу данных", e);
        }
    }

    // Каждый вызов — прямое обращение к БД
    public GameObject get(String id) {
        try {
            GameObject obj = db.load(id);
            if (obj != null) {
                System.out.println("Загружен из БД: " + obj.getName());
            }
            return obj;
        } catch (SQLException e) {
            System.err.println("Ошибка загрузки объекта из БД: " + id);
            e.printStackTrace();
            return null;
        }
    }

    // Каждое сохранение — прямое обращение к БД
    public void add(GameObject obj) {
        try {
            db.save(obj);
            System.out.println("Сохранён в БД: " + obj.getName());
        } catch (SQLException e) {
            System.err.println("Ошибка сохранения объекта в БД: " + obj.getId());
            e.printStackTrace();
        }
    }

    // Удаление — прямое обращение к БД
    public void remove(String id) {
        try {
            db.delete(id);
            System.out.println("Удален из БД: ID " + id);
        } catch (SQLException e) {
            System.err.println("Ошибка удаления объекта из БД: " + id);
            e.printStackTrace();
        }
    }

    // Каждый раз — полный запрос к БД
    public Collection<GameObject> getAll() {
        try {
            Collection<GameObject> allObjects = db.loadAll();
            System.out.println("Загружены ВСЕ объекты из БД (количество: " + allObjects.size() + ")");
            return allObjects;
        } catch (SQLException e) {
            System.err.println("Ошибка загрузки всех объектов из БД:");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}