using System;
using System.Windows.Forms;

namespace WinFormsApp1
{
    //Создайте интерфейс фабрики
    public interface ICharacterFactory
    {
        Character CreateCharacter();
    }

    //Создайте базовый класс Character
    public abstract class Character
    {
        public string Name { get; set; }
        public int Health { get; set; }
        public string Equipment { get; set; }

        public abstract string GetInfo();
    }

    //Создайте конкретные классы персонажей
    public class Pet : Character
    {
        public Pet(string name, int health, string equipment)
        {
            Name = name;
            Health = health;
            Equipment = equipment;
        }

        public override string GetInfo()
        {
            return $"Питомец: {Name}, Здоровье: {Health}, Оружие: {Equipment}";
        }
    }

    public class Npc : Character
    {
        public Npc(string name, int health, string equipment)
        {
            Name = name;
            Health = health;
            Equipment = equipment;
        }

        public override string GetInfo()
        {
            return $"NPC: {Name}, Здоровье: {Health}, Оружие: {Equipment}";
        }
    }

    public class Boss : Character
    {
        public Boss(string name, int health, string equipment)
        {
            Name = name;
            Health = health;
            Equipment = equipment;
        }

        public override string GetInfo()
        {
            return $"Босс: {Name}, Здоровье: {Health}, Оружие: {Equipment}";
        }
    }

    //Создайте конкретные фабрики
    public class PetFactory : ICharacterFactory
    {
        public Character CreateCharacter()
        {
            // Можно добавить настройку характеристик
            return new Pet("Кот", 100, "Мяч");
        }
    }

    public class NpcFactory : ICharacterFactory
    {
        public Character CreateCharacter()
        {
            return new Npc("Торговец", 80, "Кинжал");
        }
    }

    public class BossFactory : ICharacterFactory
    {
        public Character CreateCharacter()
        {
            return new Boss("Дракон", 1000, "Огненное дыхание");
        }
    }

}