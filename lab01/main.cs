using System;

// Абстрактная фабрика
public interface ICharacterFactory
{
    Character CreateCharacter();
}

// Конкретная фабрика для питомца
public class PetFactory : ICharacterFactory
{
    public Character CreateCharacter()
    {
        return new Pet("Cat", 50, "Claws");
    }
}

// Конкретная фабрика для NPC
public class NpcFactory : ICharacterFactory
{
    public Character CreateCharacter()
    {
        return new Npc("Villager", 100, "Dagger");
    }
}

// Конкретная фабрика для босса
public class BossFactory : ICharacterFactory
{
    public Character CreateCharacter()
    {
        return new Boss("Dragon", 1000, "Fire Breath");
    }
}

// Абстрактный продукт
public abstract class Character
{
    public string Name { get; protected set; }
    public int Health { get; protected set; }
    public string Equipment { get; protected set; }

    public abstract void ShowInfo();
}

// Конкретный продукт: питомец
public class Pet : Character
{
    public Pet(string name, int health, string equipment)
    {
        Name = name;
        Health = health;
        Equipment = equipment;
    }

    public override void ShowInfo()
    {
        Console.WriteLine($"Питомец: {Name}, Здоровье: {Health}, Оружие: {Equipment}");
    }
}

// Конкретный продукт: NPC
public class Npc : Character
{
    public Npc(string name, int health, string equipment)
    {
        Name = name;
        Health = health;
        Equipment = equipment;
    }

    public override void ShowInfo()
    {
        Console.WriteLine($"NPC: {Name}, Здоровье: {Health}, Оружие: {Equipment}");
    }
}

// Конкретный продукт: босс
public class Boss : Character
{
    public Boss(string name, int health, string equipment)
    {
        Name = name;
        Health = health;
        Equipment = equipment;
    }

    public override void ShowInfo()
    {
        Console.WriteLine($"Босс: {Name}, Здоровье: {Health}, Оружие: {Equipment}");
    }
}

// Клиентский код
class Program
{
    static void Main()
    {
        // Создаем фабрики для разных персонажей
        ICharacterFactory petFactory = new PetFactory();
        ICharacterFactory npcFactory = new NpcFactory();
        ICharacterFactory bossFactory = new BossFactory();

        // Создаем персонажей
        Character pet = petFactory.CreateCharacter();
        Character npc = npcFactory.CreateCharacter();
        Character boss = bossFactory.CreateCharacter();

        // Вывод информации
        pet.ShowInfo();
        npc.ShowInfo();
        boss.ShowInfo();
    }
}