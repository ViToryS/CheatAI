
Собранный apk: CheatAI.apk

Особенности использования: 

- на экране с текстом книги для вызова и скрытия меню нужно сделать двойной клик в любом месте;
- для переключения между страницами: одиночный клик в левой и правой части экрана.

   
# Инструкция по сборке APK для Windows

## 1. Скачайте Command Line Tools
- Ссылка: https://developer.android.com/studio#command-line-tools-only
- Скачай файл `commandlinetools-win-xxxx_latest.zip`

## 2. Распакуйте архив
- Создайте папку `C:\Android` (или на любом другом диске)
- Внутри `C:\Android` создайте папку `cmdline-tools\latest`
- Распакуй содержимое архива в `C:\Android\cmdline-tools\latest`
- Должно получиться: `C:\Android\cmdline-tools\latest\bin\sdkmanager.bat`

## 3. Установите компоненты SDK
Укажите путь к SDK:

$env:ANDROID_HOME = "D:\Android"

Открой командную строку (Win+R → cmd) и выполни:
bash
D:\Android\cmdline-tools\bin\sdkmanager "platform-tools" "platforms;android-35" "build-tools;35.0.0"

## 4. Клонируйте проект
git clone https://github.com/ViToryS/CheatAI.git
cd CheatAI


### Сборка Debug APK

#### Win :

.\gradlew assembleDebug


### Путь к apk
   ....CheatAI\app\build\outputs\apk\debug\app-debug.apk
