using System;
using System.Drawing;
using System.Windows.Forms;

namespace WinFormsApp1
{
    public partial class Form1 : Form
    {
        private PictureBox pictureBoxCharacter;
        private Label pictureLabel;

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

            // Создаем и настраиваем элементы для отображения картинки
            InitializePictureBox();

            // Обработчики событий
            buttonCreate.Click += ButtonCreate_Click;
            buttonCreateWithoutPattern.Click += buttonCreateWithoutPattern_Click;

            // Загружаем картинку по умолчанию при запуске
            LoadCharacterImage(comboBoxCharacterType.SelectedItem.ToString());
        }

        private void InitializePictureBox()
        {
            // Надпись для картинки
            pictureLabel = new Label
            {
                Text = "Изображение персонажа:",
                Location = new Point(20, 150),
                Size = new Size(200, 20),
                Font = new Font("Arial", 10, FontStyle.Bold)
            };

            // PictureBox для отображения картинки
            pictureBoxCharacter = new PictureBox
            {
                Location = new Point(20, 170),
                Size = new Size(200, 200),
                SizeMode = PictureBoxSizeMode.Zoom,
                BorderStyle = BorderStyle.FixedSingle,
                BackColor = Color.LightGray
            };

            // Добавляем элементы на форму
            this.Controls.Add(pictureLabel);
            this.Controls.Add(pictureBoxCharacter);

            // Перемещаем textBoxInfo, чтобы освободить место для картинки
            textBoxInfo.Location = new Point(230, 170);
            textBoxInfo.Size = new Size(250, 200);

            // Увеличиваем размер формы, если нужно
            this.ClientSize = new Size(500, 400);
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

                // Загружаем картинку персонажа
                LoadCharacterImage(selectedType);
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

            // Загружаем картинку персонажа
            LoadCharacterImage(selectedType);
        }

        private void LoadCharacterImage(string characterType)
        {
            try
            {
                // Определяем имя файла в зависимости от типа персонажа
                string imageName = characterType switch
                {
                    "Питомец" => "pet.png",
                    "NPC" => "npc.png",
                    "Дракон-Босс" => "dragon_boss.png",
                    "Орк-Босс с топором" => "orc_boss.png",
                    "Рыцарь-Босс со щитом" => "knight_boss.png",
                    _ => "default.png"
                };

                // Путь к файлу изображения
                string imagePath = System.IO.Path.Combine("images", imageName);

                // Проверяем существование файла
                if (System.IO.File.Exists(imagePath))
                {
                    // Загружаем изображение из файла
                    pictureBoxCharacter.Image = Image.FromFile(imagePath);
                }
                else
                {
                    // Если файл не найден, создаем заглушку
                    CreatePlaceholderImage(characterType);
                }
            }
            catch (Exception ex)
            {
                // В случае ошибки создаем заглушку
                CreatePlaceholderImage(characterType);
                textBoxInfo.Text += $"\n\nОшибка загрузки изображения: {ex.Message}";
            }
        }

        private void CreatePlaceholderImage(string characterType)
        {
            // Создаем простое изображение-заглушку
            Bitmap placeholder = new Bitmap(pictureBoxCharacter.Width, pictureBoxCharacter.Height);

            using (Graphics g = Graphics.FromImage(placeholder))
            {
                // Заливаем фон
                g.Clear(Color.LightGray);

                // Настраиваем шрифт
                using (Font font = new Font("Arial", 10, FontStyle.Bold))
                using (StringFormat sf = new StringFormat())
                {
                    sf.Alignment = StringAlignment.Center;
                    sf.LineAlignment = StringAlignment.Center;

                    // Рисуем текст с названием персонажа
                    g.DrawString($"Изображение:\n{characterType}",
                                font,
                                Brushes.Black,
                                new RectangleF(0, 0, placeholder.Width, placeholder.Height),
                                sf);
                }

                // Рисуем рамку
                g.DrawRectangle(Pens.DarkGray, 0, 0, placeholder.Width - 1, placeholder.Height - 1);
            }

            // Устанавливаем изображение-заглушку
            pictureBoxCharacter.Image = placeholder;
        }
    }
}