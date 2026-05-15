import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.SwingUtilities;


// Базовый класс для игровых объектов
abstract class GameObject {
    protected String id;
    protected String name;

    public GameObject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

// Коллекция объектов (Identity Map)
class IdentityMap {
    private DatabaseManager db;
    private HashMap<String, GameObject> objects = new HashMap<>();

    public IdentityMap() {
        try {
            this.db = new DatabaseManager();
            loadFromDatabase();
        } catch (SQLException e) {
            System.err.println("Ошибка инициализации базы данных:");
            e.printStackTrace();
            throw new RuntimeException("Не удалось инициализировать базу данных", e);
        }
    }

    private void loadFromDatabase() {
        try {
            Collection<GameObject> loaded = db.loadAll();
            for (GameObject obj : loaded) {
                objects.put(obj.getId(), obj);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка загрузки данных из БД:");
            e.printStackTrace();
        }
    }

    public void add(GameObject obj) {
        objects.put(obj.getId(), obj);
        try {
            db.save(obj);
        } catch (SQLException e) {
            System.err.println("Ошибка сохранения объекта в БД: " + obj.getId());
            e.printStackTrace();
        }
    }

    public GameObject get(String id) {
        if (objects.containsKey(id)) {
            return objects.get(id);
        }

        try {
            GameObject obj = db.load(id);
            if (obj != null) {
                objects.put(id, obj);
            }
            return obj;
        } catch (SQLException e) {
            System.err.println("Ошибка загрузки объекта из БД: " + id);
            e.printStackTrace();
            return null;
        }
    }

    public void remove(String id) {
        objects.remove(id);
        try {
            db.delete(id);
        } catch (SQLException e) {
            System.err.println("Ошибка удаления объекта из БД: " + id);
            e.printStackTrace();
        }
    }

    public Collection<GameObject> getAll() {
        return objects.values();
    }
}

// Пример класса персонажа
class Character extends GameObject {
    public Character(String id, String name) {
        super(id, name);
    }
    // Дополнительные методы
}

// Основной класс для демонстрации
public class RunPattern1 {
    public static void main(String[] args) {
        IdentityMap collection = new IdentityMap();

        // Запускаем GUI в EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            GameObjectGUI gui = new GameObjectGUI(collection);
            gui.setVisible(true);
        });
    }
}