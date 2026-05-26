12 ++

CREATE TABLE publisher (
id BIGINT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(100)
);

CREATE TABLE magazine (
id BIGINT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(100),
price DECIMAL(10,2),
publisher_id BIGINT,

    FOREIGN KEY (publisher_id)
        REFERENCES publisher(id)
);

Якщо Hibernate налаштований правильно — 
таблиці можуть створитися автоматично 
просто після запуску main().
🔥 Що для цього потрібно
У hibernate.cfg.xml має бути:
<property name="hibernate.hbm2ddl.auto">create</property>
але всі дані кожного разу перезаписуватимуться


Зараз зробимо ПЕРШИЙ relation-проект.

📌 Тематика
Publisher ↔ Magazine
Один Publisher: може мати: багато Magazine

А кожен Magazine: має: одного Publisher

🔥 Структура
Publisher->
id
name

List<Magazine> Magazines

Magazine->
id
title
price
Publisher publisher

-------------------------------------

🔥 Твоє перше завдання
1. Створити entity Publisher
   ⚠️ Важливо

Додати:

@OneToMany
Поки без деталей —

спробуй сам.

📌 Підказка

У Author:

private List<Book> books;
І треба:
import List
import annotations
⚠️ Поки НЕ поспішай

Не треба одразу:

cascade
fetch
mappedBy
Спочатку:
просто відчути relation
🔥 Друге завдання

Створити Book.

Там буде:
@ManyToOne
І:
@JoinColumn
⚠️ Але спробуй сам спочатку
🧠 Дуже важливо зараз

Тобі треба:
НЕ копіювати,
а:

спробувати вгадати логіку ORM
І ще одна порада
НЕ роби одразу двосторонній зв’язок ідеальним

Бо relations у Hibernate:

спочатку ламають мозок майже всім

😄

🔥 Мінімальна ціль

Щоб працювало:

Author → books
Book → author
🚀 А після цього:

ми розберемо:

owning side,
mappedBy,
LAZY,
cascade,
join fetch,
чому relations часто ламаються 🙂