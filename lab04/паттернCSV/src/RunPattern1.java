import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Коллекция объектов (Identity Map)
class IdentityMap {
    private CSVManager csvManager;
    private Map<String, GameObject> cache;

    public IdentityMap() {
        this.csvManager = new CSVManager("game_objects.csv");
        this.cache = new HashMap<>();
        loadFromCSV();
    }

    //public IdentityMap(CSVManager csvManager) {
    //    this.csvManager = csvManager;
    //    this.cache = new HashMap<>();
    //    loadFromCSV();
    //}

    public Collection<GameObject> getAll() {
        return new ArrayList<>(cache.values());
}

    private void loadFromCSV() {
        List<Map<String, String>> records = csvManager.getAllRecords();
        for (Map<String, String> record : records) {
            String id = record.get("id");
            if (cache.containsKey(id)) {
                System.err.println("Предупреждение: дублирующий ID " + id + " в CSV. Пропускаем запись.");
                continue;
            }
            GameObject obj = createGameObjectFromRecord(record);
            if (obj != null) {
                cache.put(id, obj);
            }
        }
    }

    private GameObject createGameObjectFromRecord(Map<String, String> record) {
    try {
        String id = record.get("id");
        String name = record.get("name");
        String type = record.get("type");
        double x = Double.parseDouble(record.getOrDefault("position_x", "0"));
        double y = Double.parseDouble(record.getOrDefault("position_y", "0"));

        if ("character".equals(type)) {
            int health = Integer.parseInt(record.getOrDefault("health", "100"));
            int level = Integer.parseInt(record.getOrDefault("level", "1"));
            return new Character(id, name, x, y, health, level);
        }
    } catch (NumberFormatException e) {
        System.err.println("Ошибка парсинга данных для записи " + record.get("id") + ": " + e.getMessage());
    }
        return null;
    }

    public GameObject get(String id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        Map<String, String> record = csvManager.getRecordById(id);
        if (record != null) {
            GameObject obj = createGameObjectFromRecord(record);
            if (obj != null) {
                cache.put(id, obj);
                return obj;
            }
        }
        return null;
    }

    public void put(GameObject obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Объект не может быть null");
        }
        String id = obj.getId();
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID объекта не может быть пустым");
        }

        // Проверка на дубликат
        if (cache.containsKey(id)) {
            throw new IllegalArgumentException("Объект с ID " + id + " уже существует");
        }


        Map<String, String> record = new HashMap<>();
        record.put("id", obj.getId());
        record.put("name", obj.getName());
        record.put("type", obj.getType());
        record.put("position_x", String.valueOf(obj.getX()));
        record.put("position_y", String.valueOf(obj.getY()));

        if (obj instanceof Character) {
            Character character = (Character) obj;
            record.put("health", String.valueOf(character.getHealth()));
            record.put("level", String.valueOf(character.getLevel()));
        }

        csvManager.addRecord(record);
        cache.put(obj.getId(), obj);
    }

    public void update(GameObject obj) {
        Map<String, String> record = new HashMap<>();
        record.put("id", obj.getId());
        record.put("name", obj.getName());
        record.put("type", obj.getType());
        record.put("position_x", String.valueOf(obj.getX()));
        record.put("position_y", String.valueOf(obj.getY()));
        
        if (obj instanceof Character) {
            Character character = (Character) obj;
            record.put("health", String.valueOf(character.getHealth()));
            record.put("level", String.valueOf(character.getLevel()));
        }
        
        csvManager.updateRecord(obj.getId(), record);
        cache.put(obj.getId(), obj);
    }

    public void remove(String id) {
        csvManager.deleteRecord(id);
        cache.remove(id);
    }
}


// Базовый класс для игровых объектов
abstract class GameObject {
    private String id;
    private String name;
    private String type;
    private double x, y;

    public GameObject(String id, String name, String type, double x, double y) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    // Геттеры
    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public double getX() { return x; }
    public double getY() { return y; }

    // Сеттеры
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}


// Пример класса персонажа
class Character extends GameObject {
    private int health;
    private int level;

    public Character(String id, String name, double x, double y, int health, int level) {
        super(id, name, "character", x, y);
        this.health = health;
        this.level = level;
    }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
}

// Основной класс для демонстрации
public class RunPattern1 {
    public static void main(String[] args) {
        try {
            IdentityMap identityMap = new IdentityMap();

            // Пример использования — создаём экземпляр Character вместо GameObject
            GameObject obj1 = new Character("1", "Player", 10.0, 20.0, 100, 1);
            identityMap.put(obj1);

            GameObject retrieved = identityMap.get("1");
            System.out.println("Retrieved: " + retrieved.getName());

            // Дополнительно: демонстрируем работу с полями Character
            if (retrieved instanceof Character) {
                Character character = (Character) retrieved;
                System.out.println("Health: " + character.getHealth());
                System.out.println("Level: " + character.getLevel());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}