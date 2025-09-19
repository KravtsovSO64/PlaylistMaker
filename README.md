# Playlist Maker

**Playlist Maker** — это написанное с нуля Android-приложение для тех, кто не представляет свою жизнь без музыки. Откройте для себя миллионы треков со всего мира, лайкайте их и создавайте музыкальные альбомы которыми не стыдно поделиться с друзьями.

---

## 🎶 Ключевые возможности

*   **Серверная часть**: Поиск по базе более миллиона треков из разных стран и жанров с помощью iTunes Search API.
*   **Персональные альбомы**: Создавайте и редактируйте собственные сборники из избранных треков.
*   **Делитесь музыкой**: Отправляйте ссылки на свои плейлисты и альбомы друзьям.
*   **Не переживайте**: Прослушивание музыки продолжается, даже когда вы выходите из приложения.
*   **Современный UI**: Красивый и интуитивно понятный интерфейс, написанный на XML, Jetpack Compose, а так же с использованием CustomView.
*   **Плавная работа**: Высокая отзывчивость и производительность обеспечены асинхронными операциями на Kotlin Coroutines.

---

## 🛠 Технологический стек

*   **Язык**: [Kotlin](https://kotlinlang.org/)
*   **Асинхронность**: [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) с Flow для асинхронного и реактивного программирования.
*   **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - современный декларативный toolkit для построения интерфейса.
*   **Воспроизведение аудио**: [MediaPlayer](https://developer.android.com/guide/topics/media/mediaplayer) для управления аудиопотоком.
*   **Фоновые службы**: [Service](https://developer.android.com/guide/components/services) для воспроизведения музыки в фоне.
*   **Управление системными событиями**: [BroadcastReceiver](https://developer.android.com/reference/android/content/BroadcastReceiver) уведомит Вас, если что-то пойдет не так.
*   **Архитектура**: MVVM для чистого и поддерживаемого кода.
*   **Сетевые запросы**: [Retrofit](https://square.github.io/retrofit/) с Gson для работы с REST API.

---

## 🏗️ Установка и запуск

1.  Клонируйте репозиторий:
    ```bash
    git clone https://github.com/KravtsovSO64/PlaylistMaker
    ```
2.  Откройте проект в Android Studio.
3.  Получите API-ключ на сайте [iTunes Search API](https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/index.html#//apple_ref/doc/uid/TP40017632-CH3-SW1).
4.  Не забудьте добавьте полученный ключ в проект.
5.  Приложение готово к запуску.

---

## 📐 Архитектура и основные компоненты

Приложение построено по принципам SOLID, KISS, DRY

*   **Data Layer**: Репозитории, которые объединяют данные c сервера и локальной базы данных.
*   **Domain Layer**: Содержит интеракторы для инкапсуляции бизнес-логики.
*   **UI Layer**: Jetpack Compose, использует ViewModel для подготовки данных для отображения и управления состоянием.

