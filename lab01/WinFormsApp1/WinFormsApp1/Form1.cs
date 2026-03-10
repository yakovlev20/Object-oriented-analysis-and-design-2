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
            comboBoxCharacterType.Items.AddRange(new string[] { "Питомец", "NPC", "Босс" });
            comboBoxCharacterType.SelectedIndex = 0;

            // Обработчик события
            buttonCreate.Click += ButtonCreate_Click;
        }

        private void ButtonCreate_Click(object sender, EventArgs e)
        {
            string selectedType = comboBoxCharacterType.SelectedItem.ToString();
            Character character;

            // В зависимости от типа создаем фабрику
            ICharacterFactory factory = selectedType switch
            {
                "Питомец" => new PetFactory(),
                "NPC" => new NpcFactory(),
                "Босс" => new BossFactory(),
                _ => null
            };

            if (factory != null)
            {
                character = factory.CreateCharacter();
                textBoxInfo.Text = character.GetInfo();
            }
        }

        //Без паттерна
        private void buttonCreate_Click1(object sender, EventArgs e)
        {
            string selectedType = comboBoxCharacterType.SelectedItem.ToString();
            Character character;

            switch (selectedType)
            {
                case "Питомец":
                    character = new Pet("Кот", 100, "Мяч");
                    break;
                case "NPC":
                    character = new Npc("Торговец", 80, "Кинжал");
                    break;
                case "Босс":
                    character = new Boss("Дракон", 1000, "Огненное дыхание");
                    break;
                default:
                    MessageBox.Show("Выберите тип персонажа");
                    return;
            }

            // Вывод информации о персонаже
            textBoxInfo.Text = character.GetInfo();
        }
    }
}