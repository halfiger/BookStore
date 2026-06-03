ОСТАННЄ Hibernate mini-завдання.

📌 Міні-фінал Hibernate
= bidirectional helper methods
Тобто:

зробити красиво:

publisher.addMagazine(...)

і:

publisher.removeMagazine(...)
+ orphanRemoval
  І ти побачиш:
  Hibernate автоматично видаляє child entity
  ⚠️ Це дуже корисна ORM практика.
  Але після цього — я б уже йшов у Spring 🙂

Добре, тут уже важливо не заплутатись у lifecycle 🙂
Бо orphanRemoval працює НЕ “магією”, а через:

Hibernate tracking entity state
🔥 1. ДЕ робити removeMagazine()
✅ У класі Publisher

Бо:

Publisher керує collection magazines
У Publisher.java
Було:
public void addMagazine(Magazine magazine) {

    magazines.add(magazine);

    magazine.setPublisher(this);
}
Додати нижче:
public void removeMagazine(Magazine magazine) {

    magazines.remove(magazine);

    magazine.setPublisher(null);
}
🧠 Чому саме так
1️⃣
magazines.remove(...)
прибирає child:
з collection parent-а
2️⃣
magazine.setPublisher(null)
прибирає relation:
з child side
⚠️ Relation:
треба синхронізувати з ОБОХ сторін
🔥 2. orphanRemoval

У Publisher:

Було:
@OneToMany(
mappedBy = "publisher",
cascade = CascadeType.ALL,
fetch = FetchType.LAZY
)
Стане:
@OneToMany(
mappedBy = "publisher",
cascade = CascadeType.ALL,
fetch = FetchType.LAZY,
orphanRemoval = true
)
🔥 3. updatePublisher()
У service потрібен метод:
public void updatePublisher(Publisher publisher)
І ось тут дуже важливий момент.
Hibernate зараз:
отримав object поза session
Це:
detached entity
І Hibernate треба:
повернути під контроль session
Тому:
session.merge(publisher);
🔥 Повний метод
public void updatePublisher(Publisher publisher) {

    Session session = factory.getCurrentSession();

    session.beginTransaction();

    session.merge(publisher);

    session.getTransaction().commit();
}
⚠️ merge —

дуже важливий Hibernate метод.

🧠 Він каже:
“синхронізуй detached object з database”
🔥 4. main()
Publisher publisher =
service.findPublisherWithMagazines(1L);

Magazine magazine =
publisher.getMagazines().get(0);

publisher.removeMagazine(magazine);

service.updatePublisher(publisher);
🔥 Що станеться
1️⃣
removeMagazine()

прибере:

relation,
magazine з collection.
2️⃣
merge()

Hibernate побачить:

“цей child більше не належить parent”
3️⃣

Через:

orphanRemoval = true

Hibernate зробить:

DELETE FROM magazines
WHERE id = ?
⚠️ Без orphanRemoval

Hibernate зазвичай зробив би:

UPDATE magazines
SET publisher_id = null
А orphanRemoval каже:
“видалити child повністю”
🔥 Це дуже важлива ORM behavior тема
remove from collection

≠
delete row

orphanRemoval:

робить:

remove from collection → DELETE row
🚀 І це вже реально дуже хороше Hibernate розуміння 🙂