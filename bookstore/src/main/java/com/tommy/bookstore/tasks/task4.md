🔥 Наступне завдання (дуже важливе)
LazyInitializationException

📌 Завдання
1. У main:
Publisher publisher = 
bookstoreService.findPublisherById(2L);

2. ПІСЛЯ завершення transaction:

System.out.println(
publisher.getMagazines()
);

⚠️ І подивись:
- 
- чи буде exception.

🧠 Якщо буде:
LazyInitializationException
То ти побачиш:
session already closed
І це: одна з найвідоміших проблем Hibernate 🙂

🔥 Після цього ми зробимо:
JOIN FETCH
І ти побачиш:
SELECT p FROM Publisher p
JOIN FETCH p.magazines

=============================================================

Відповідь
- 
- ти зараз уперся саме в ту межу, де починається:

service layer vs entity layer

І твоє відчуття правильне:

не хочеться пхати SQL/Session всередину entity
🔥 І це ПРАВИЛЬНО

Entity:

НЕ повинна знати про Hibernate Session
❌ Погана практика:
public List<Magazine> getMagazines() {
Session session = ...
}
Бо entity тоді:
ламає separation of concerns,
стає залежною від DB,
перестає бути просто моделлю.
🧠 Тому:
ГЕТЕР залишаємо простим
public List<Magazine> getMagazines() {
return magazines;
}
⚠️ І він абсолютно правильний.
🔥 Проблема НЕ в getter
Проблема тут:
session вже закрита

коли ти викликаєш:

publisher.getMagazines()
🧠 Бо:
LAZY collection ще не завантажена
Hibernate думає:
“зараз я зроблю SQL”
Але:
session already closed
І тоді:
LazyInitializationException
🔥 Тому рішення роблять у SERVICE layer
І саме це: правильна архітектура
🚀 Варіант №1 — initialize всередині service
Наприклад:

    public Publisher findPublisherById(Long id) {
    Session session = factory.getCurrentSession();
    session.beginTransaction();
    Publisher publisher =
            session.get(Publisher.class, id);
    publisher.getMagazines().size();
    session.getTransaction().commit();
    return publisher;
}
⚠️ Оце:
publisher.getMagazines().size();
спеціально:
тригерить lazy loading
🧠 Поки session ще жива.
І тоді:

після commit
колекція вже:

завантажена в пам’ять
🔥 Але це “хитрий” спосіб

