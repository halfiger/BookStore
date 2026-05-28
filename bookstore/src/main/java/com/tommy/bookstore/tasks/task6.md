🚀 Тому наступна нормальна тема тепер:
№6 — N+1 problem
І вона прямо пов’язана з JOIN FETCH.
🧠 Ідея

Уяви:

List<Publisher> publishers
Hibernate робить:
SELECT publishers
А потім для КОЖНОГО publisher:
SELECT magazines WHERE publisher_id=?
⚠️ Тобто:

1 запит +
N запитів.

І це:
N+1 problem
🔥 Наступне завдання
📌 1.

Створити метод:

findAllPublishers()
Просто:
FROM Publisher
📌 2.

У main:

Отримати:
List<Publisher>
І циклом:
for (...)
Викликати:
publisher.getMagazines().size()
📌 3.

Подивитися SQL logs.

🧠 І ти побачиш:
1 query publishers
+
багато query magazines
⚠️ І це одна з головних ORM performance проблем.
🚀 А потім:

ми вирішимо її через:

JOIN FETCH

для collection 🙂


--------------відповідь----------------------

Ти щойно УСПІШНО відтворив N+1 problem 🙂

І по логах це видно ідеально.

🧠 Дивись що сталося
1️⃣ Hibernate зробив:
select p1_0.id,p1_0.name from publishers p1_0

Це:
отримали ВСІХ publisher-ів
2️⃣ Потім:
select m1_0.publisher_id,m1_0.id,m1_0.name,m1_0.price
from magazines m1_0
where m1_0.publisher_id=?

І ще раз:
select m1_0.publisher_id,m1_0.id,m1_0.name,m1_0.price
from magazines m1_0
where m1_0.publisher_id=?

🧠 Чому двічі?

Бо:

1 publisher = DC
2 publisher = Marvel
Hibernate:
для кожного publisher
окремо тягне magazines
⚠️ І це:
1 + N queries
🔥 Це і є:
N+1 problem
🧠 І тепер головне

У тебе лише:

2 publisher-и
А уяви:
1000 publisher-ів
Hibernate зробить:
1 + 1000 SQL queries
⚠️ І це вже:
дуже поганий performance
🔥 Тепер наступне завдання 7
ВИРІШИТИ N+1 через JOIN FETCH