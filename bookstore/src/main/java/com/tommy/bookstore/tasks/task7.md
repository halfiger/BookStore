📌 Твоє завдання 7
1️⃣ Створи новий метод: findAllPublishersWithMagazines()
2️⃣ Використай JPQL:

SELECT DISTINCT p
FROM Publisher p
JOIN FETCH p.magazines
⚠️ Дуже важливо:
DISTINCT
🧠 Чому?

Бо JOIN:

розмножує publisher rows
Наприклад:
DC + magazine1
DC + magazine2
DC + magazine3
І без DISTINCT:

Hibernate може повернути:

один і той самий publisher багато разів
📌 3️⃣

У main:

List<Publisher> list =
service.findAllPublishersWithMagazines();

for (Publisher p : list) {
System.out.println(
p.getMagazines()
);
}
📌 4️⃣

Подивись SQL logs.

🧠 І ти побачиш:

НЕ:

1 + N queries
А:
ОДИН JOIN query 🙂
🔥 І це вже:
реальний production Hibernate optimization
⚠️ Ще кілька дуже важливих зауважень по твоєму коду
1️⃣ Оцей коментар неправильний:
/// хто володар relation --> publisher!

❌ Owner:
Magazine
Бо owner side —

це сторона з:

@JoinColumn
Тобто:
@ManyToOne
@JoinColumn(name="publisher_id")
private Publisher publisher;

Publisher:
inverse side

через:

mappedBy
2️⃣ Ось це дуже добре:
publisher.getMagazines().size();

Бо ти вже:
свідомо тригериш lazy loading
І це правильне ORM мислення 🙂
3️⃣ Ось це:
<property name="hibernate.hbm2ddl.auto">create</property>

⚠️ Я б уже змінив на:
update
Бо зараз:

при кожному запуску:

ти повністю втрачаєш дані

------------------------

Так 🙂
І ти правильно зрозумів лише частину механіки, але там є ще один дуже важливий нюанс.

🔥 Що робить DISTINCT у Hibernate JOIN FETCH
Без DISTINCT

Hibernate робить SQL типу:

Publisher JOIN Magazine
І SQL повертає:
DC + Batman
DC + Flash
DC + Superman
Marvel + SpiderMan
Marvel + Thor
⚠️ Тобто SQL row-и дублюють Publisher.
🧠 Для SQL це НОРМАЛЬНО.

Бо JOIN:

створює комбінації рядків
Але Hibernate потім намагається:
перетворити rows → Java objects
І без DISTINCT Hibernate може створити:
List:
[DC, DC, DC, Marvel, Marvel]
⚠️ Хоча це той самий Publisher object.
🔥 DISTINCT каже Hibernate:
“залиш лише унікальні Publisher”
І тоді Hibernate:
об’єднує rows,
збирає magazines у collection,
повертає:
[DC, Marvel]
🧠 Тобто DISTINCT:

НЕ “вирішує N+1” напряму.

❌ N+1 вирішує:
JOIN FETCH
А DISTINCT:
прибирає дублікати root entity
🔥 Дуже важливе ORM мислення
JOIN FETCH:
завантажує relation одним SQL
DISTINCT:
нормалізує Java result list
⚠️ І це одна з тих речей,

які дуже плутають новачків Hibernate.

🔥 До речі

Твій метод:

findAllPublishersWithMagasines7()

працює правильно 🙂

Але я б перейменував:
Magasines

→

Magazines
Бо зараз typo 🙂
🔥 І ще одна дуже важлива річ

Ось тут:

publisher=Publisher{id=1, name='DC Comics'}

⚠️ У тебе зараз:

Magazine.toString()
друкує:

publisher
Це ПОКИ нормально.
Але в майбутньому це може викликати:
циклічний recursion
Наприклад:

Publisher →
magazines →
publisher →
magazines →
publisher…

І тоді:
StackOverflowError
🧠 Тому в production часто:
або прибирають relation з toString(),
або використовують Lombok exclude,
або DTO.
🚀 А тепер ти вже реально дійшов до рівня:
Hibernate performance optimization basics
