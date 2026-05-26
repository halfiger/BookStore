🔥 Наступне завдання (дуже важливе)
LazyInitializationException
📌 Завдання
1.

У main:

Publisher publisher =
bookstoreService.findPublisherById(2L);
2.

ПІСЛЯ завершення transaction:

System.out.println(
publisher.getMagazines()
);
⚠️ І подивись:

чи буде exception.

🧠 Якщо буде:
LazyInitializationException
То ти побачиш:
session already closed
І це:

одна з найвідоміших проблем Hibernate 🙂

🔥 Після цього ми зробимо:
JOIN FETCH
І ти побачиш:
SELECT p FROM Publisher p
JOIN FETCH p.magazines
⚠️ Це вже дуже “дорослий” Hibernate.
І ще одна порада
Зміни:
<property name="hibernate.hbm2ddl.auto">create</property>

На:
update
Бо зараз:

при кожному запуску
ти:

втрачаєш усі дані

🙂