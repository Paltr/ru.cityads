# ru.cityads

Небольшое Android приложение для оценки.

Текст задачи можно посмотреть внизу.

### Описание проекта
1. Для работы с сетью используется Retrofit
2. Для работы с UI, получения данных с сервера используется реактивное программирование(RxJava)
3. Комментарии - только общее описание классов, т.к. код организовывался таким образом, чтобы его можно было легко читать без комментариев - мелкие методы с говорящими названиями, минимальная вложенность и т.д.
4. Присутствует 2 локализации - английский и русский
5. Обрабатывается 3 типа ошибок:
   * ошибки в теле ответа на запрос рекламы(Status != OK)  3a
   * ошибки отображения html в WebView
   * прочие ошибки(отсутствие Интернета, ошибки соединения, ошибки парсинга и прочие)

### Структура проекта
* ru.cityads.test
   * services
      * AdResponse: объект-ответ на запрос рекламы
      * AdsService: класс для запроса рекламы, полностью абстрагирует источник рекламы.
   * activities
      * AdsActivity: Activity для отображения рекламы
   * MainActivity: главная Activity приложения, имеет кнопку Test, по нажатию которой стартует AdsActivity

### Текст задания
Design and build android activity for serving full screen html ads.
Example:

>POST /adviator/index.php HTTP/1.1
>Host: www.505.rs
>Cache-Control: no-cache
>Postman-Token: abd93bb8-2857-2fd0-7679-0b25087e1d35
>Content-Type: application/x-www-form-urlencoded
>
>id=85950205030644900
>
>{
>"status":"OK",
>"message":"display full screen ad",
>"url":"http://www.505.rs/adviator/ad.html"
>}

If status is equal "OK" use returned "url" and load ad into Activity webView.

If status is not equal "OK" show dialog with 'message' text and OK button. Clicking OK button will dismiss both dialog and activity.

While requesting for ad and loading ad html show spinner in center of screen and transparent background.

Once html ad is loaded hide spinner and show ad.

When user clicks ad link close activity and open native android browser with clicked url.

Activity should work in both portrait and landscape mode.