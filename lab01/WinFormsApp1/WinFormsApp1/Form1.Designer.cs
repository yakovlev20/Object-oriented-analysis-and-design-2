namespace WinFormsApp1
{
    partial class Form1
    {
        private System.ComponentModel.IContainer components = null;
        private System.Windows.Forms.ComboBox comboBoxCharacterType;
        private System.Windows.Forms.Button buttonCreate;
        private System.Windows.Forms.TextBox textBoxInfo;
        private System.Windows.Forms.Label labelSelectType;
        private System.Windows.Forms.Label labelInfo;
        private System.Windows.Forms.TableLayoutPanel tableLayout;

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1200, 800);
            this.Text = "Создание персонажей";

            // Инициализация элементов
            this.comboBoxCharacterType = new System.Windows.Forms.ComboBox();
            this.buttonCreate = new System.Windows.Forms.Button();
            this.textBoxInfo = new System.Windows.Forms.TextBox();
            this.labelSelectType = new System.Windows.Forms.Label();
            this.labelInfo = new System.Windows.Forms.Label();
            this.tableLayout = new System.Windows.Forms.TableLayoutPanel();

            // Настройка метки для выбора типа
            this.labelSelectType.Text = "Выберите тип персонажа:";
            this.labelSelectType.Font = new System.Drawing.Font("Arial", 10, System.Drawing.FontStyle.Bold);
            this.labelSelectType.AutoSize = true;
            this.labelSelectType.Dock = System.Windows.Forms.DockStyle.Fill;
            this.labelSelectType.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;

            // Настройка метки для информации
            this.labelInfo.Text = "Информация о персонаже:";
            this.labelInfo.Font = new System.Drawing.Font("Arial", 10, System.Drawing.FontStyle.Bold);
            this.labelInfo.AutoSize = true;
            this.labelInfo.Dock = System.Windows.Forms.DockStyle.Fill;
            this.labelInfo.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;

            // Настройка ComboBox
            this.comboBoxCharacterType.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.comboBoxCharacterType.Font = new System.Drawing.Font("Arial", 10);
            this.comboBoxCharacterType.Dock = System.Windows.Forms.DockStyle.Fill;
            this.comboBoxCharacterType.Margin = new System.Windows.Forms.Padding(3, 3, 10, 3);

            // Настройка Button
            this.buttonCreate.Text = "Создать персонажа";
            this.buttonCreate.Font = new System.Drawing.Font("Arial", 10, System.Drawing.FontStyle.Bold);
            this.buttonCreate.Dock = System.Windows.Forms.DockStyle.Fill;
            this.buttonCreate.Margin = new System.Windows.Forms.Padding(3, 10, 3, 10);
            this.buttonCreate.BackColor = System.Drawing.Color.LightSkyBlue;
            this.buttonCreate.FlatStyle = System.Windows.Forms.FlatStyle.Flat;

            // Настройка TextBox
            this.textBoxInfo.Multiline = true;
            this.textBoxInfo.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.textBoxInfo.Font = new System.Drawing.Font("Consolas", 10);
            this.textBoxInfo.ReadOnly = true;
            this.textBoxInfo.Dock = System.Windows.Forms.DockStyle.Fill;
            this.textBoxInfo.Margin = new System.Windows.Forms.Padding(10, 3, 3, 3);
            this.textBoxInfo.BackColor = System.Drawing.Color.White;
            this.textBoxInfo.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;

            // Настройка таблицы для организации элементов
            this.tableLayout.ColumnCount = 2;
            this.tableLayout.RowCount = 3;
            this.tableLayout.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayout.Padding = new System.Windows.Forms.Padding(15);
            this.tableLayout.BackColor = System.Drawing.Color.LightGray;

            // Настройка столбцов
            this.tableLayout.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 200F));
            this.tableLayout.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));

            // Настройка строк
            this.tableLayout.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 40F));
            this.tableLayout.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 50F));
            this.tableLayout.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));

            // Расположение элементов в таблице
            // Первая строка: метка + комбобокс
            this.tableLayout.Controls.Add(this.labelSelectType, 0, 0);
            this.tableLayout.Controls.Add(this.comboBoxCharacterType, 1, 0);

            // Вторая строка: кнопка (занимает оба столбца)
            this.tableLayout.SetColumnSpan(this.buttonCreate, 2);
            this.tableLayout.Controls.Add(this.buttonCreate, 0, 1);

            // Третья строка: метка + текстовое поле
            this.tableLayout.Controls.Add(this.labelInfo, 0, 2);
            this.tableLayout.Controls.Add(this.textBoxInfo, 1, 2);

            // Добавляем таблицу на форму
            this.Controls.Add(this.tableLayout);

            // Настройка формы
            this.BackColor = System.Drawing.Color.LightGray;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.MinimumSize = new System.Drawing.Size(500, 300);
        }
        #endregion
    }
}