using System;
using System.Windows.Forms;

namespace WinFormsApp1
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
   
            // Инициализация элементов UI
            comboBoxCharacterType.Items.AddRange(new string[] {
                "Питомец",
                "NPC",
                "Дракон-Босс",
                "Орк-Босс с топором",
                "Рыцарь-Босс со щитом"
            });
            comboBoxCharacterType.SelectedIndex = 0;

            // Обработчик события
            buttonCreate.Click += ButtonCreate_Click;
        }

        private void ButtonCreate_Click(object sender, EventArgs e)
        {
            string selectedType = comboBoxCharacterType.SelectedItem.ToString();
            AbstractCharacterFactory factory;

            // В зависимости от типа создаем соответствующую фабрику
            factory = selectedType switch
            {
                "Питомец" => new PetFactory(),
                "NPC" => new NpcFactory(),
                "Дракон-Босс" => new BossFactory(),
                "Орк-Босс с топором" => new BossWithAxeFactory(),
                "Рыцарь-Босс со щитом" => new BossWithShieldFactory(),
                _ => null
            };

            if (factory != null)
            {
                // Создаем клиента с выбранной фабрикой
                CharacterClient client = new CharacterClient(factory);

                // Вместо вызова DisplayInfo() получаем информацию и отображаем в textBoxInfo
                string info = client.GetCharacterInfo();
                textBoxInfo.Text = info;
            }
            else
            {
                MessageBox.Show("Выберите тип персонажа", "Ошибка",
                    MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
        }

        //Без паттерна (для сравнения)
        private void buttonCreateWithoutPattern_Click(object sender, EventArgs e)
        {
            string selectedType = comboBoxCharacterType.SelectedItem.ToString();
            AbstractCharacter character;
            AbstractEquipment equipment;

            switch (selectedType)
            {
                case "Питомец":
                    character = new Pet("Кот", 100);
                    equipment = new MeleeWeapon("Когти", 20);
                    break;
                case "NPC":
                    character = new Npc("Торговец", 50);
                    equipment = new MeleeWeapon("Кинжал", 30);
                    break;
                case "Дракон-Босс":
                    character = new Boss("Дракон", 1000);
                    equipment = new MagicWeapon("Огненное дыхание", 200);
                    break;
                case "Орк-Босс с топором":
                    character = new Boss("Орк-Вождь", 800);
                    equipment = new MeleeWeapon("Топор разрушения", 150);
                    break;
                case "Рыцарь-Босс со щитом":
                    character = new Boss("Рыцарь-Защитник", 1200);
                    equipment = new MeleeWeapon("Щит праведности", 80);
                    break;
                default:
                    MessageBox.Show("Выберите тип персонажа");
                    return;
            }

            // Вывод информации о персонаже и экипировке
            string info = character.GetInfo() + "\n" +
                         equipment.GetInfo() + "\n" +
                         $"Общая характеристика: {character.Name} с {equipment.Name}";

            textBoxInfo.Text = info;
        }
    }


}