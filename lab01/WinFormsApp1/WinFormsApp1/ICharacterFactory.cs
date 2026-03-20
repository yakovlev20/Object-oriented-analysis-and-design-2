using System;
using System.Windows.Forms;

namespace WinFormsApp1
{
    // Абстрактная фабрика
    public abstract class AbstractCharacterFactory
    {
        public abstract AbstractCharacter CreateCharacter();
        public abstract AbstractEquipment CreateEquipment();
    }

    // Абстрактный класс персонажа
    public abstract class AbstractCharacter
    {
        public string Name { get; set; }
        public int Health { get; set; }

        public abstract string GetInfo();
    }

    // Абстрактный класс экипировки
    public abstract class AbstractEquipment
    {
        public string Name { get; set; }
        public int AttackPower { get; set; }

        public abstract string GetInfo();
    }

    // Конкретные классы персонажей
    public class Pet : AbstractCharacter
    {
        public Pet(string name, int health)
        {
            Name = name;
            Health = health;
        }

        public override string GetInfo()
        {
            return $"Питомец: {Name},\nЗдоровье: {Health}";
        }
    }

    public class Npc : AbstractCharacter
    {
        public Npc(string name, int health)
        {
            Name = name;
            Health = health;
        }

        public override string GetInfo()
        {
            return $"NPC: {Name},\nЗдоровье: {Health}";
        }
    }

    public class Boss : AbstractCharacter
    {
        public Boss(string name, int health)
        {
            Name = name;
            Health = health;
        }

        public override string GetInfo()
        {
            return $"Босс: {Name},\nЗдоровье: {Health}";
        }
    }

    // Конкретные классы экипировки
    public class MeleeWeapon : AbstractEquipment
    {
        public MeleeWeapon(string name, int attackPower)
        {
            Name = name;
            AttackPower = attackPower;
        }

        public override string GetInfo()
        {
            return $"Ближнее оружие: {Name},\nСила атаки оружия: {AttackPower}";
        }
    }

    public class RangedWeapon : AbstractEquipment
    {
        public RangedWeapon(string name, int attackPower)
        {
            Name = name;
            AttackPower = attackPower;
        }

        public override string GetInfo()
        {
            return $"Дальнее оружие: {Name},\nСила атаки оружия: {AttackPower}";
        }
    }

    public class MagicWeapon : AbstractEquipment
    {
        public MagicWeapon(string name, int attackPower)
        {
            Name = name;
            AttackPower = attackPower;
        }

        public override string GetInfo()
        {
            return $"Магическое оружие: {Name},\nСила атаки оружия: {AttackPower}";
        }
    }

    // Конкретные фабрики
    public class PetFactory : AbstractCharacterFactory
    {
        public override AbstractCharacter CreateCharacter()
        {
            return new Pet("Кот", 100);
        }

        public override AbstractEquipment CreateEquipment()
        {
            return new MeleeWeapon("Когти", 20);
        }
    }

    public class NpcFactory : AbstractCharacterFactory
    {
        public override AbstractCharacter CreateCharacter()
        {
            return new Npc("Торговец", 50);
        }

        public override AbstractEquipment CreateEquipment()
        {
            return new MeleeWeapon("Кинжал", 30);
        }
    }

    public class BossFactory : AbstractCharacterFactory
    {
        public override AbstractCharacter CreateCharacter()
        {
            return new Boss("Дракон", 1000);
        }

        public override AbstractEquipment CreateEquipment()
        {
            return new MagicWeapon("Огненное дыхание", 200);
        }
    }

    // Босс с топором (альтернативная фабрика)
    public class BossWithAxeFactory : AbstractCharacterFactory
    {
        public override AbstractCharacter CreateCharacter()
        {
            return new Boss("Орк-Вождь", 800);
        }

        public override AbstractEquipment CreateEquipment()
        {
            return new MeleeWeapon("Топор разрушения", 150);
        }
    }

    // Босс со щитом (альтернативная фабрика)
    public class BossWithShieldFactory : AbstractCharacterFactory
    {
        public override AbstractCharacter CreateCharacter()
        {
            return new Boss("Рыцарь-Защитник", 1200);
        }

        public override AbstractEquipment CreateEquipment()
        {
            return new MeleeWeapon("Щит праведности", 80);
        }
    }

    // Клиент
    public class CharacterClient
    {
        private AbstractCharacter character;
        private AbstractEquipment equipment;

        public CharacterClient(AbstractCharacterFactory factory)
        {
            character = factory.CreateCharacter();
            equipment = factory.CreateEquipment();
        }

        // Метод для получения информации в виде строки
        public string GetCharacterInfo()
        {
            //return character.GetInfo() + "\n\n" +
            //       equipment.GetInfo() + "\n\n" +
            //       $"Общая характеристика: {character.Name} с {equipment.Name}";
            return character.GetInfo() + Environment.NewLine + Environment.NewLine +
               equipment.GetInfo() + Environment.NewLine + Environment.NewLine +
               $"Общая характеристика: {character.Name} с {equipment.Name}";
        }

        // Старый метод для вывода в MessageBox (можно оставить для обратной совместимости)
        public void DisplayInfo()
        {
            string info = GetCharacterInfo();
            MessageBox.Show(info, "Информация о персонаже");
        }
    }
}