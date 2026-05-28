🔥 Наступне завдання
№5 — JOIN FETCH
🧠 Це:
правильний Hibernate спосіб
завантажити relation одразу
⚠️ Бо зараз:
publisher.getMagazines()

може:

зробити додатковий SQL,
або кинути LazyInitializationException.
🔥 JOIN FETCH вирішує це

Hibernate:

одразу підтягує relation
📌 Твоє завдання
1.

Створити метод:

findPublisherWithMagazines(Long id)
2.

Написати JPQL:

SELECT p
FROM Publisher p
JOIN FETCH p.magazines
WHERE p.id = :id
3.

Використати:

.uniqueResult()
4.

У main:

Отримати publisher:
Publisher publisher =
service.findPublisherWithMagazines(1L);
5.

Після commit/session close:

System.out.println(
publisher.getMagazines()
);
⚠️ І тепер:

помилки вже НЕ повинно бути.

🧠 Чому?

Бо:

JOIN FETCH already loaded collection
🔥 І тут почнеться дуже важливе ORM розуміння
Без JOIN FETCH
Publisher loaded
Magazines lazy
З JOIN FETCH
Publisher + magazines loaded together
🔥 І ще дуже важливе

Подивися SQL у console.

Без JOIN FETCH

буде:

SELECT publisher

а потім окремо:

SELECT magazines
А з JOIN FETCH:

буде:

ONE BIG JOIN QUERY
⚠️ І це вже:
боротьба з N+1 problem
🚀 А після цього буде:
№6 — EAGER vs LAZY

-----------------------------------------------
? ---> task5
🚀 Правильніший Hibernate спосіб
JOIN FETCH
Оце вже дуже важлива ORM техніка.
Метод:

    public Publisher findPublisherWithMagazines(Long id)
    JPQL:
    SELECT p
    FROM Publisher p
    JOIN FETCH p.magazines
    WHERE p.id = :id

Повністю:

    public Publisher findPublisherWithMagazines(Long id) {
    Session session = factory.getCurrentSession();
    session.beginTransaction();
    Publisher publisher = session.createQuery(
            "SELECT p FROM Publisher p " +
            "JOIN FETCH p.magazines " +
            "WHERE p.id = :id",
            Publisher.class
    )
            .setParameter("id", id)
            .uniqueResult();
    session.getTransaction().commit();
    return publisher;
}
🔥 І тепер:
publisher.getMagazines()
НЕ зробить новий SQL,

бо:

relation already loaded
🧠 І це:
правильний Hibernate підхід
⚠️ Найважливіше

Getter:

НЕ треба переробляти
Service:
повинен вирішувати
що і коли завантажувати
🔥 І це одна з головних ORM ідей:
Entity:
дані + relation
Service:
робота з session/query/loading

---------------------------

# JOIN FETCH — це інструкція в JPQL/HQL,
# яка змушує Hibernate отримати пов'язані
# сутності (асоціації) в один запит до бази даних.
# Вона використовується для уникнення проблеми N+1
# і замінює ліниву (LAZY) стратегію завантаження
# на жадібну (EAGER) лише для поточного запиту.


---- пояснення
JOIN FETCH
якраз і є:

одне з головних рішень LazyInitializationException
🔥 Логіка така
❌ Без JOIN FETCH

Hibernate робить:

SELECT publisher
А magazines:
ще НЕ завантажені
Потім session закривається.
І коли ти робиш:
publisher.getMagazines()

Hibernate каже:

“треба зробити ще SQL”
Але:
session already closed
⚠️ І тоді:
LazyInitializationException
🔥 А JOIN FETCH

робить:

SELECT publisher + magazines JOIN
🧠 Тобто:

relation уже:

повністю завантажена

ще ДО закриття session.

І тому:
publisher.getMagazines()

після commit:
✅ працює нормально.

🔥 Тобто ти правий:
JOIN FETCH —

це вже і є рішення проблеми.
