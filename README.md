# Sky Rush

Аркадная игра для Android. Летите сквозь трубы, пока не врежетесь.

### Возможности

- Управление одним касанием
- Нарастающая сложность: каждые 5 очков трубы ускоряются и сближаются
- Качающиеся трубы: каждая третья пара движется вертикально
- Пять уровней сложности: Easy, Normal, Hard, Extreme, Insane
- Рекорд сохраняется между сессиями

### Стек

Java, LibGDX 1.12.1, Android Gradle Plugin 8.3.2

### Сборка

```
git clone https://github.com/username/skyrush.git
cd skyrush
set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr
gradlew assembleDebug
```

APK окажется в `android/build/outputs/apk/debug/`.

### Установка на устройство

```
adb install android/build/outputs/apk/debug/android-debug.apk
```

Требуется Android 5.1 и выше.

### Структура проекта

| Папка | Содержимое |
|-------|-----------|
| `core/src/com/flappybird/` | Игровая логика |
| `android/src/` | Android-лаунчер |
| `android/assets/` | Текстуры |
