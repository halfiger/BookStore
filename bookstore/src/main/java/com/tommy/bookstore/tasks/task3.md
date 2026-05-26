🔥 А тепер наступне важливе завдання

Ти вже:

створив relation,
зробив cascade,
побачив generated FK.
🚀 Наступна тема:
LAZY LOADING
⚠️ Це ОДНА з найважливіших ORM тем взагалі.
🧠 Ідея

Hibernate:

не завантажує все одразу
Він може:
підвантажити relation пізніше
🔥 Твоє завдання
📌 1.

У Publisher:

явно додати:

fetch = FetchType.LAZY
Ось тут:
@OneToMany(...)
має стати:
@OneToMany(
mappedBy = "publisher",
cascade = CascadeType.ALL,
fetch = FetchType.LAZY
)
⚠️ Хоча OneToMany і так LAZY by default —

ми це пишемо спеціально,
щоб ти це побачив.

📌 2.

Створи новий метод:

findPublisherById
У BookstoreService
Метод:
public Publisher findPublisherById(Long id)
Використати:
session.get(...)
📌 3.

У main:

Отримати publisher:
Publisher publisher = bookstoreService.findPublisherById(1L);
📌 4.

Після цього:

System.out.println(publisher.getName());
І окремо:
System.out.println(publisher.getMagazines());
🔥 І ось тут почнеться магія 🙂
🧠 Ти побачиш:

спочатку:

SELECT publisher
А потім ЛИШЕ при:
getMagazines()

Hibernate зробить:

SELECT magazines
⚠️ Тобто relation:

завантажиться ЛІНИВО.

І це:
LAZY loading
🔥 Але є дуже важливий нюанс
⚠️ Якщо transaction закрита —

може бути:

LazyInitializationException
І це:

одна з найвідоміших помилок Hibernate 😄

🚀 Після цього:

ми розберемо:

чому вона виникає,
session lifecycle,
proxies,
join fetch,
N+1 problem 🙂