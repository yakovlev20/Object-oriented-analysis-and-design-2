// WindowsProject2.cpp : Определяет точку входа для приложения.
//

#include "framework.h"
#include "WindowsProject2.h"

#include "Character.h"
#include <sstream>
#include <memory>
#include <string>

#define MAX_LOADSTRING 100

#define ID_BUTTON_CREATE 1001
#define ID_BUTTON_ADD_SHIELD 1002
#define ID_BUTTON_ADD_MAGIC 1003
#define ID_BUTTON_ADD_STRENGTH 1004
#define ID_BUTTON_ADD_SPEED 1005
#define ID_BUTTON_RESET 1006
#define ID_COMBO_CHARACTER 1007
#define ID_COMBO_DECORATORS 1008  // Новый ID для ComboBox с декораторами

// Глобальные переменные:
HINSTANCE hInst;                                // текущий экземпляр
WCHAR szTitle[MAX_LOADSTRING];                  // Текст строки заголовка
WCHAR szWindowClass[MAX_LOADSTRING];            // имя класса главного окна

// Глобальные переменные для управления персонажем
std::shared_ptr<AbstractCharacter> currentCharacter;
HWND hComboCharacter;
HWND hButtonCreate;
HWND hButtonShield;
HWND hButtonMagic;
HWND hButtonStrength;
HWND hButtonSpeed;
HWND hButtonReset;
HWND hStaticInfo;

HWND hStaticStats = nullptr;
HWND hStaticBuffs = nullptr;

// Отправить объявления функций, включенных в этот модуль кода:
ATOM                MyRegisterClass(HINSTANCE hInstance);
BOOL                InitInstance(HINSTANCE, int);
LRESULT CALLBACK    WndProc(HWND, UINT, WPARAM, LPARAM);
INT_PTR CALLBACK    About(HWND, UINT, WPARAM, LPARAM);

void UpdateCharacterInfo(HWND hWnd);
void CreateControls(HWND hWnd);
void ResetCharacter();

int APIENTRY wWinMain(_In_ HINSTANCE hInstance,
    _In_opt_ HINSTANCE hPrevInstance,
    _In_ LPWSTR    lpCmdLine,
    _In_ int       nCmdShow)
{
    UNREFERENCED_PARAMETER(hPrevInstance);
    UNREFERENCED_PARAMETER(lpCmdLine);

    // Инициализация глобальных строк
    LoadStringW(hInstance, IDS_APP_TITLE, szTitle, MAX_LOADSTRING);
    LoadStringW(hInstance, IDC_WINDOWSPROJECT2, szWindowClass, MAX_LOADSTRING);
    MyRegisterClass(hInstance);

    // Выполнить инициализацию приложения:
    if (!InitInstance(hInstance, nCmdShow))
    {
        return FALSE;
    }

    HACCEL hAccelTable = LoadAccelerators(hInstance, MAKEINTRESOURCE(IDC_WINDOWSPROJECT2));

    MSG msg;

    // Цикл основного сообщения:
    while (GetMessage(&msg, nullptr, 0, 0))
    {
        if (!TranslateAccelerator(msg.hwnd, hAccelTable, &msg))
        {
            TranslateMessage(&msg);
            DispatchMessage(&msg);
        }
    }

    return (int)msg.wParam;
}


// Регистрирует класс окна
ATOM MyRegisterClass(HINSTANCE hInstance)
{
    WNDCLASSEXW wcex;

    wcex.cbSize = sizeof(WNDCLASSEX);

    wcex.style = CS_HREDRAW | CS_VREDRAW;
    wcex.lpfnWndProc = WndProc;
    wcex.cbClsExtra = 0;
    wcex.cbWndExtra = 0;
    wcex.hInstance = hInstance;
    wcex.hIcon = LoadIcon(hInstance, MAKEINTRESOURCE(IDI_WINDOWSPROJECT2));
    wcex.hCursor = LoadCursor(nullptr, IDC_ARROW);
    wcex.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
    wcex.lpszMenuName = MAKEINTRESOURCEW(IDC_WINDOWSPROJECT2);
    wcex.lpszClassName = szWindowClass;
    wcex.hIconSm = LoadIcon(wcex.hInstance, MAKEINTRESOURCE(IDI_SMALL));

    return RegisterClassExW(&wcex);
}


// Сохраняет маркер экземпляра и создает главное окно
BOOL InitInstance(HINSTANCE hInstance, int nCmdShow)
{
    hInst = hInstance; // Сохранить маркер экземпляра в глобальной переменной

    HWND hWnd = CreateWindowW(szWindowClass, szTitle, WS_OVERLAPPEDWINDOW,
        400, 200, 900, 700, nullptr, nullptr, hInstance, nullptr); // Увеличил размер окна

    if (!hWnd)
    {
        return FALSE;
    }

    ShowWindow(hWnd, nCmdShow);
    UpdateWindow(hWnd);

    return TRUE;
}

void CreateControls(HWND hWnd)
{
    // Левая колонка - выбор персонажа и улучшения
    CreateWindowW(L"STATIC", L"Создание персонажа:",
        WS_CHILD | WS_VISIBLE | SS_LEFT,
        20, 20, 200, 25, hWnd, nullptr, hInst, nullptr);

    // Создание ComboBox для выбора персонажа
    hComboCharacter = CreateWindowW(L"COMBOBOX", L"",
        WS_CHILD | WS_VISIBLE | CBS_DROPDOWNLIST | WS_VSCROLL,
        20, 50, 200, 200, hWnd, (HMENU)ID_COMBO_CHARACTER, hInst, nullptr);

    // Добавление элементов в ComboBox
    SendMessage(hComboCharacter, CB_ADDSTRING, 0, (LPARAM)L"Питомец");
    SendMessage(hComboCharacter, CB_ADDSTRING, 0, (LPARAM)L"NPC");
    SendMessage(hComboCharacter, CB_ADDSTRING, 0, (LPARAM)L"Босс");
    SendMessage(hComboCharacter, CB_SETCURSEL, 0, 0); // Выбрать первый элемент

    // Кнопка создания персонажа
    hButtonCreate = CreateWindowW(L"BUTTON", L"Создать персонажа",
        WS_CHILD | WS_VISIBLE | BS_PUSHBUTTON,
        240, 50, 150, 30, hWnd, (HMENU)ID_BUTTON_CREATE, hInst, nullptr);

    // Группа улучшений
    CreateWindowW(L"STATIC", L"Доступные улучшения:",
        WS_CHILD | WS_VISIBLE | SS_LEFT,
        20, 100, 200, 25, hWnd, nullptr, hInst, nullptr);

    // Кнопка добавления щита
    hButtonShield = CreateWindowW(L"BUTTON", L"Добавить Щит (+20 к броне)",
        WS_CHILD | WS_VISIBLE | BS_PUSHBUTTON,
        20, 130, 250, 30, hWnd, (HMENU)ID_BUTTON_ADD_SHIELD, hInst, nullptr);

    // Кнопка добавления магической ауры
    hButtonMagic = CreateWindowW(L"BUTTON", L"Добавить Магическую ауру (+25 к урону, +0.3 к скорости)",
        WS_CHILD | WS_VISIBLE | BS_PUSHBUTTON,
        20, 170, 350, 30, hWnd, (HMENU)ID_BUTTON_ADD_MAGIC, hInst, nullptr);

    // Кнопка добавления усиления силы
    hButtonStrength = CreateWindowW(L"BUTTON", L"Добавить Усиление силы (+100 HP, +35 к урону, -0.2 к скорости)",
        WS_CHILD | WS_VISIBLE | BS_PUSHBUTTON,
        20, 210, 400, 30, hWnd, (HMENU)ID_BUTTON_ADD_STRENGTH, hInst, nullptr);

    // Кнопка добавления ускорения
    hButtonSpeed = CreateWindowW(L"BUTTON", L"Добавить Ускорение (+0.5 к скорости, -5 к броне)",
        WS_CHILD | WS_VISIBLE | BS_PUSHBUTTON,
        20, 250, 300, 30, hWnd, (HMENU)ID_BUTTON_ADD_SPEED, hInst, nullptr);

    // Кнопка сброса
    hButtonReset = CreateWindowW(L"BUTTON", L"Сбросить персонажа",
        WS_CHILD | WS_VISIBLE | BS_PUSHBUTTON,
        20, 290, 200, 30, hWnd, (HMENU)ID_BUTTON_RESET, hInst, nullptr);

    // Правая колонка - информация о персонаже
    CreateWindowW(L"STATIC", L"Характеристики персонажа:",
        WS_CHILD | WS_VISIBLE | SS_LEFT,
        450, 20, 200, 25, hWnd, nullptr, hInst, nullptr);

    // Статическое текстовое поле для отображения характеристик
    hStaticStats = CreateWindowW(L"STATIC", L"Персонаж не создан",
        WS_CHILD | WS_VISIBLE | SS_LEFT | WS_BORDER | SS_LEFTNOWORDWRAP,
        450, 50, 400, 200, hWnd, nullptr, hInst, nullptr);

    // Блок со списком примененных баффов
    CreateWindowW(L"STATIC", L"Активные баффы:",
        WS_CHILD | WS_VISIBLE | SS_LEFT,
        450, 260, 200, 25, hWnd, nullptr, hInst, nullptr);

    // Статическое текстовое поле для отображения баффов
    hStaticBuffs = CreateWindowW(L"STATIC", L"Нет активных баффов",
        WS_CHILD | WS_VISIBLE | SS_LEFT | WS_BORDER | SS_LEFTNOWORDWRAP,
        450, 290, 400, 100, hWnd, nullptr, hInst, nullptr);

    // Изначально деактивируем кнопки улучшений
    EnableWindow(hButtonShield, FALSE);
    EnableWindow(hButtonMagic, FALSE);
    EnableWindow(hButtonStrength, FALSE);
    EnableWindow(hButtonSpeed, FALSE);
    EnableWindow(hButtonReset, FALSE);
}

void UpdateCharacterInfo(HWND hWnd)
{
    if (!currentCharacter) return;

    // Обновляем характеристики
    std::wstringstream stats;
    stats << L"Текущий персонаж: " << currentCharacter->GetDescription() << L"\n\n";
    stats << L"Характеристики:\n";
    stats << L"  Здоровье: " << currentCharacter->GetHealth() << L" HP\n";
    stats << L"  Урон: " << currentCharacter->GetDamage() << L"\n";
    stats << L"  Броня: " << currentCharacter->GetArmor() << L"\n";
    stats << L"  Скорость: " << currentCharacter->GetSpeed() << L"\n\n";
    stats << L"Количество улучшений: " << currentCharacter->GetDecoratorCount();

    SetWindowTextW(hStaticStats, stats.str().c_str());

    // Обновляем список баффов
    std::wstringstream buffs;

    // Проверяем, есть ли улучшения
    bool hasUpgrades = false;

    // Добавляем каждое примененное улучшение
    if (currentCharacter->HasUpgrade(UpgradeType::SHIELD))
    {
        buffs << L"• Щит (+20 к броне)\n";
        hasUpgrades = true;
    }
    if (currentCharacter->HasUpgrade(UpgradeType::MAGIC_AURA))
    {
        buffs << L"• Магическая аура (+25 к урону, +0.3 к скорости)\n";
        hasUpgrades = true;
    }
    if (currentCharacter->HasUpgrade(UpgradeType::STRENGTH))
    {
        buffs << L"• Усиление силы (+100 HP, +35 к урону, -0.2 к скорости)\n";
        hasUpgrades = true;
    }
    if (currentCharacter->HasUpgrade(UpgradeType::SPEED))
    {
        buffs << L"• Ускорение (+0.5 к скорости, -5 к броне)\n";
        hasUpgrades = true;
    }

    // Если нет улучшений, показываем заглушку
    if (!hasUpgrades)
    {
        buffs << L"Нет активных баффов";
    }

    SetWindowTextW(hStaticBuffs, buffs.str().c_str());
}

void ResetCharacter()
{
    currentCharacter.reset();
}

LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    switch (message)
    {
    case WM_CREATE:
        CreateControls(hWnd);
        break;

    case WM_COMMAND:
    {
        int wmId = LOWORD(wParam);
        int wmEvent = HIWORD(wParam);

        if (wmId >= ID_BUTTON_CREATE && wmId <= ID_BUTTON_RESET)
        {
            // Обработка нажатий кнопок
            switch (wmId)
            {
            case ID_BUTTON_CREATE:
            {
                int selected = SendMessage(hComboCharacter, CB_GETCURSEL, 0, 0);

                switch (selected)
                {
                case 0: // Питомец
                    currentCharacter = std::make_shared<Pet>();
                    break;
                case 1: // NPC
                    currentCharacter = std::make_shared<Npc>();
                    break;
                case 2: // Босс
                    currentCharacter = std::make_shared<Boss>();
                    break;
                }

                // Активируем кнопки улучшений
                EnableWindow(hButtonShield, TRUE);
                EnableWindow(hButtonMagic, TRUE);
                EnableWindow(hButtonStrength, TRUE);
                EnableWindow(hButtonSpeed, TRUE);
                EnableWindow(hButtonReset, TRUE);

                UpdateCharacterInfo(hWnd);
                break;
            }

            case ID_BUTTON_ADD_SHIELD:
                if (currentCharacter)
                {
                    currentCharacter = std::make_shared<ShieldDecorator>(currentCharacter);
                    UpdateCharacterInfo(hWnd);
                }
                break;

            case ID_BUTTON_ADD_MAGIC:
                if (currentCharacter)
                {
                    currentCharacter = std::make_shared<MagicAuraDecorator>(currentCharacter);
                    UpdateCharacterInfo(hWnd);
                }
                break;

            case ID_BUTTON_ADD_STRENGTH:
                if (currentCharacter)
                {
                    currentCharacter = std::make_shared<StrengthDecorator>(currentCharacter);
                    UpdateCharacterInfo(hWnd);
                }
                break;

            case ID_BUTTON_ADD_SPEED:
                if (currentCharacter)
                {
                    currentCharacter = std::make_shared<SpeedDecorator>(currentCharacter);
                    UpdateCharacterInfo(hWnd);
                }
                break;

            case ID_BUTTON_RESET:
                ResetCharacter();
                SetWindowTextW(hStaticStats, L"Персонаж сброшен. Создайте нового персонажа.");
                SetWindowTextW(hStaticBuffs, L"Нет активных баффов");

                // Деактивируем кнопки улучшений
                EnableWindow(hButtonShield, FALSE);
                EnableWindow(hButtonMagic, FALSE);
                EnableWindow(hButtonStrength, FALSE);
                EnableWindow(hButtonSpeed, FALSE);
                EnableWindow(hButtonReset, FALSE);
                break;
            }
        }
        else
        {
            // Обработка меню
            switch (wmId)
            {
            case IDM_ABOUT:
                DialogBox(hInst, MAKEINTRESOURCE(IDD_ABOUTBOX), hWnd, About);
                break;
            case IDM_EXIT:
                DestroyWindow(hWnd);
                break;
            default:
                return DefWindowProc(hWnd, message, wParam, lParam);
            }
        }
        break;
    }

    case WM_PAINT:
    {
        PAINTSTRUCT ps;
        HDC hdc = BeginPaint(hWnd, &ps);

        // Получаем размеры клиентской области
        RECT rcClient;
        GetClientRect(hWnd, &rcClient);

        // Рисуем разделительную линию между левой и правой колонками
        HPEN hPen = CreatePen(PS_SOLID, 2, RGB(200, 200, 200));
        HPEN hOldPen = (HPEN)SelectObject(hdc, hPen);

        int dividerX = 430; // Позиция разделительной линии
        MoveToEx(hdc, dividerX, 20, NULL);
        LineTo(hdc, dividerX, rcClient.bottom - 20);

        SelectObject(hdc, hOldPen);
        DeleteObject(hPen);

        // Рисуем заголовки с рамками
        HFONT hTitleFont = CreateFont(18, 0, 0, 0, FW_BOLD, FALSE, FALSE, FALSE,
            DEFAULT_CHARSET, OUT_DEFAULT_PRECIS, CLIP_DEFAULT_PRECIS,
            DEFAULT_QUALITY, DEFAULT_PITCH | FF_DONTCARE, L"Arial");
        HFONT hNormalFont = CreateFont(14, 0, 0, 0, FW_NORMAL, FALSE, FALSE, FALSE,
            DEFAULT_CHARSET, OUT_DEFAULT_PRECIS, CLIP_DEFAULT_PRECIS,
            DEFAULT_QUALITY, DEFAULT_PITCH | FF_DONTCARE, L"Arial");

        // Левая колонка - заголовок "Управление"
        HFONT hOldFont = (HFONT)SelectObject(hdc, hTitleFont);
        SetTextColor(hdc, RGB(0, 0, 139)); // Темно-синий
        SetBkMode(hdc, TRANSPARENT);

        TextOutW(hdc, 20, 0, L"УПРАВЛЕНИЕ", 10);

        // Правая колонка - заголовок "Информация"
        TextOutW(hdc, 450, 0, L"ИНФОРМАЦИЯ", 11);

        // Рисуем рамки вокруг информационных блоков
        HPEN hFramePen = CreatePen(PS_SOLID, 1, RGB(100, 100, 200));
        hOldPen = (HPEN)SelectObject(hdc, hFramePen);

        // Рамка вокруг информации о характеристиках
        Rectangle(hdc, 448, 48, 852, 252);

        // Рамка вокруг списка баффов
        Rectangle(hdc, 448, 288, 852, 392);

        SelectObject(hdc, hOldPen);
        DeleteObject(hFramePen);

        // Подписи под рамками
        SelectObject(hdc, hNormalFont);
        SetTextColor(hdc, RGB(0, 100, 0)); // Темно-зеленый

        // Подпись для информации о характеристиках
        TextOutW(hdc, 450, 255, L"Характеристики персонажа", 24);

        // Подпись для списка баффов
        TextOutW(hdc, 450, 395, L"Активные баффы", 14);

        // Восстанавливаем старый шрифт
        SelectObject(hdc, hOldFont);

        // Освобождаем ресурсы
        DeleteObject(hTitleFont);
        DeleteObject(hNormalFont);

        EndPaint(hWnd, &ps);
        break;
    }

    case WM_SIZE:
    {
        // Обновляем размеры элементов при изменении размера окна
        int width = LOWORD(lParam);
        int height = HIWORD(lParam);

        // Обновляем размеры информационных блоков в правой колонке
        if (width > 500 && height > 400)
        {
            // Характеристики персонажа
            MoveWindow(hStaticStats, 450, 50, width - 470, 200, TRUE);

            // Список баффов
            MoveWindow(hStaticBuffs, 450, 290, width - 470, 100, TRUE);
        }
        break;
    }

    case WM_CTLCOLORSTATIC:
    {
        HDC hdcStatic = (HDC)wParam;
        HWND hwndStatic = (HWND)lParam;

        // Изменяем цвет фона для информационных статических элементов
        if (hwndStatic == hStaticStats || hwndStatic == hStaticBuffs)
        {
            SetTextColor(hdcStatic, RGB(0, 0, 0)); // Черный текст
            SetBkColor(hdcStatic, RGB(240, 248, 255)); // Светло-голубой фон
            return (LRESULT)CreateSolidBrush(RGB(240, 248, 255));
        }

        // Для остальных статических элементов - стандартный цвет
        SetTextColor(hdcStatic, RGB(0, 0, 0));
        SetBkColor(hdcStatic, GetSysColor(COLOR_WINDOW));
        return (LRESULT)GetSysColorBrush(COLOR_WINDOW);
    }

    case WM_CTLCOLORBTN:
    {
        HDC hdcButton = (HDC)wParam;

        // Настраиваем внешний вид кнопок
        SetTextColor(hdcButton, RGB(255, 255, 255)); // Белый текст
        SetBkColor(hdcButton, RGB(70, 130, 180)); // Steel blue фон

        static HBRUSH hButtonBrush = CreateSolidBrush(RGB(70, 130, 180));
        return (LRESULT)hButtonBrush;
    }

    case WM_CTLCOLORLISTBOX:
    case WM_CTLCOLOREDIT:
    {
        HDC hdcCombo = (HDC)wParam;

        // Настраиваем внешний вид ComboBox'ов
        SetTextColor(hdcCombo, RGB(0, 0, 139)); // Темно-синий текст
        SetBkColor(hdcCombo, RGB(240, 248, 255)); // Светло-голубой фон

        static HBRUSH hComboBrush = CreateSolidBrush(RGB(240, 248, 255));
        return (LRESULT)hComboBrush;
    }

    case WM_DESTROY:
        PostQuitMessage(0);
        break;

    default:
        return DefWindowProc(hWnd, message, wParam, lParam);
    }
    return 0;
}

// Обработчик сообщений для окна "О программе"
INT_PTR CALLBACK About(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
    UNREFERENCED_PARAMETER(lParam);
    switch (message)
    {
    case WM_INITDIALOG:
    {
        // Изменяем размер окна
        SetWindowPos(hDlg, NULL, 0, 0, 500, 400, SWP_NOMOVE | SWP_NOZORDER);

        // Центрируем окно
        RECT rcOwner, rcDlg;
        GetWindowRect(GetParent(hDlg), &rcOwner);
        GetWindowRect(hDlg, &rcDlg);

        int x = rcOwner.left + (rcOwner.right - rcOwner.left - (rcDlg.right - rcDlg.left)) / 2;
        int y = rcOwner.top + (rcOwner.bottom - rcOwner.top - (rcDlg.bottom - rcDlg.top)) / 2;
        SetWindowPos(hDlg, NULL, x, y, 0, 0, SWP_NOSIZE | SWP_NOZORDER);

        // Создаем элементы для окна "О программе" с улучшенным оформлением
        HWND hText = CreateWindowW(L"STATIC",
            L"Лабораторная работа №2: Паттерн Декоратор\n\n"
            L"Динамическое добавление характеристик персонажу\n\n"
            L"Используемые декораторы:\n"
            L"1. Щит: +20 к броне\n"
            L"2. Магическая аура: +25 к урону, +0.3 к скорости\n"
            L"3. Усиление силы: +100 HP, +35 к урону, -0.2 к скорости\n"
            L"4. Ускорение: +0.5 к скорости, -5 к броне\n\n"
            L"Интерфейс:\n"
            L"- Левая колонка: управление созданием персонажа\n"
            L"- Правая колонка: информация и список примененных улучшений",
            WS_CHILD | WS_VISIBLE | SS_LEFT | SS_NOPREFIX,
            10, 10, 470, 330, hDlg, nullptr, hInst, nullptr);

        // Создаем кнопку ОК
        HWND hOkButton = CreateWindowW(L"BUTTON", L"ОК",
            WS_CHILD | WS_VISIBLE | BS_PUSHBUTTON | WS_TABSTOP,
            200, 350, 100, 30, hDlg, (HMENU)IDOK, hInst, nullptr);

        return (INT_PTR)TRUE;
    }

    case WM_COMMAND:
        if (LOWORD(wParam) == IDOK || LOWORD(wParam) == IDCANCEL)
        {
            EndDialog(hDlg, LOWORD(wParam));
            return (INT_PTR)TRUE;
        }
        break;

    case WM_CTLCOLORSTATIC:
    {
        HDC hdcStatic = (HDC)wParam;
        SetTextColor(hdcStatic, RGB(0, 0, 139)); // Темно-синий текст
        SetBkColor(hdcStatic, RGB(240, 248, 255)); // Светло-голубой фон
        return (LRESULT)CreateSolidBrush(RGB(240, 248, 255));
    }

    case WM_CTLCOLORBTN:
    {
        HDC hdcButton = (HDC)wParam;
        SetTextColor(hdcButton, RGB(255, 255, 255));
        SetBkColor(hdcButton, RGB(70, 130, 180));
        static HBRUSH hAboutButtonBrush = CreateSolidBrush(RGB(70, 130, 180));
        return (LRESULT)hAboutButtonBrush;
    }
    }
    return (INT_PTR)FALSE;
}