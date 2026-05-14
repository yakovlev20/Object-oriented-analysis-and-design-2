import java.util.Collection;
import java.util.HashMap;

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
    private HashMap<String, GameObject> objects = new HashMap<>();

    // Добавление объекта
    public void add(GameObject obj) {
        objects.put(obj.getId(), obj);
    }

    // Получение объекта по ID
    public GameObject get(String id) {
        return objects.get(id);
    }

    // Удаление объекта по ID
    public void remove(String id) {
        objects.remove(id);
    }

    // Получение всех объектов
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
public class RunPattern {
    public static void main(String[] args) {
        IdentityMap collection = new IdentityMap();

        // Создаем или получаем персонажа
        String charId = "player1";
        GameObject player = collection.get(charId);
        if (player == null) {
            player = new Character(charId, "Hero");
            collection.add(player);
        }

        // Повторный запрос
        GameObject samePlayer = collection.get(charId);
        System.out.println("Объект: " + samePlayer.getName()); // Выведет "Hero"
    }
}
      