    ----------------------Структура----------------------
    
    src
    └── main
        └── java
            └── com.tommy.bookstore.entity
                ├── Main.java
                ├── Magazine.java
                ├── Publisher.java
                ├── MagazineService.java
                └── HibernateUtil.java
                └── readme.md

----------------------Main.java----------------------

package com.tommy.bookstore.tasks;

import com.tommy.bookstore.entity.BookstoreService;
import com.tommy.bookstore.entity.Magazine;
import com.tommy.bookstore.entity.Publisher;

import java.util.List;

public class BookstoreApplication {

package com.tommy.bookstore.tasks;

import com.tommy.bookstore.entity.BookstoreService;
import com.tommy.bookstore.entity.Magazine;
import com.tommy.bookstore.entity.Publisher;

import java.util.List;

public class BookstoreApplication {

    public static void main(String[] args) {
//		BookstoreService bookstoreService = new BookstoreService();
//		Publisher publisher = bookstoreService.findPublisherById(2L);
//		System.out.println(publisher.toString());
//		List<Magazine> magazineList = bookstoreService.getAllMagazines(2L);
//
//		for (Magazine m : magazineList) {
//			if (m != null) {
//				System.out.println(m);
//			}
//		}
//
//		System.out.println(magazineList);


//	BookstoreService bookstoreService = new BookstoreService();
//	Publisher publisher = bookstoreService.findPublisherById(2L);
//	task 4 --> transaction ended
//		System.out.println(publisher.getMagazines());
//	for (Magazine m : publisher.getMagazines()) {
//		System.out.println(m);
//	}


        BookstoreService bookstoreService = new BookstoreService();
        List<Publisher> list = bookstoreService.findAllpublisher();

        for (Publisher p : list) {
            System.out.println(p.getMagazines().toString());
        }
    }
}

    
-----------------Magazine.java------------------

package com.tommy.bookstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "magazines")
public class Magazine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;

    @ManyToOne()
    @JoinColumn(name="publisher_id")
    private Publisher publisher;

    public Magazine() {
    }

    public Magazine(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", publisher=" + publisher +
                '}';
    }
}

--------------------Publisher.java---------------------

package com.tommy.bookstore.entity;

import jakarta.persistence.*;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="publishers")
public class Publisher {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany (mappedBy = "publisher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)   ///  хто володар relation --> publisher!
    private List<Magazine> magazines;

    public Publisher() {}

    public Publisher(String name) {
        this.name = name;
    }

    public void addMagazine(Magazine magazine) {

        if (magazines == null) {
            magazines = new ArrayList<>();
        }

        magazines.add(magazine);

        magazine.setPublisher(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setMagazines (Magazine newMagazine) {
        if (magazines == null) {
            magazines = new ArrayList<>();
        }
        magazines.add(newMagazine);
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}


------------------BookstoreService.java----------------

package com.tommy.bookstore.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class BookstoreService {
private SessionFactory factory =
HibernateUtil.getFactory();

    public void createPublisher (Publisher publisher) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        session.save(publisher);
        session.getTransaction().commit();
    }

    public void createMagazine (Magazine magazine) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        session.save(magazine);
        session.getTransaction().commit();
    }

    public List<Magazine> getAllMagazines (long ID) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        List <Magazine> list = session
                .createQuery("from Magazine m where m.publisher.id = :id",
                        Magazine.class)
                .setParameter("id", ID)
                .getResultList();
        session.getTransaction().commit();
        return list;
    }
    //task4
    public Publisher findPublisherById (Long id) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        Publisher publisher = session.get(Publisher.class, id);
        publisher.getMagazines().size();//хитрість, спеціально команда підвантажує додатково ще і магазини які не завантажилися через lazy:тригерить lazy loading
        session.getTransaction().commit();
        return publisher;
    }


    //task5
    public Publisher findPublisherWithMagazinesAndFetching (Long ID) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        Publisher publisher = session.createQuery(
         "SELECT p FROM Publisher p JOIN FETCH p.magazines WHERE p.id = :id",
                        Publisher.class)
                .setParameter("id", ID)
                .uniqueResult();
        session.getTransaction().commit();
        return publisher;
    }

    //task6
    public List <Publisher> findAllpublisher () {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        List <Publisher> list = session.createQuery("FROM Publisher", Publisher.class).getResultList();
        for (Publisher p : list) {
            p.getMagazines().size(); // підвантажили журнали
        }
        session.getTransaction().commit();
        return list;
    }
}


----------------HibernateUtil.java----------------------

package com.tommy.bookstore.entity;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
private static final SessionFactory factory;

    static {
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Magazine.class)
                .addAnnotatedClass(Publisher.class)
                .buildSessionFactory();
    }

    public static SessionFactory getFactory () {
        return factory;
    }
}

-----------------hibernate.cfg.xml---------------------

    <?xml version='1.0' encoding='utf-8'?>
    <!DOCTYPE hibernate-configuration PUBLIC
            "-//Hibernate/Hibernate Configuration DTD//EN"
            "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
    <hibernate-configuration>
        <session-factory>
            <property name="connection.url">jdbc:mysql://localhost:3306/my_db?useSSL=false&amp;serverTimezone=UTC</property>
            <property name="connection.driver_class">com.mysql.cj.jdbc.Driver</property>
            <property name="connection.username">bestuser</property>
            <property name="connection.password">bestuser</property>
            <property name="hibernate.hbm2ddl.auto">create</property>
            <property name="current_session_context_class">thread</property>
            <property name="dialect">org.hibernate.dialect.MySQLDialect</property>
            <property name="show_sql">true</property>
    
        </session-factory>
    </hibernate-configuration>


-------------------Terminal output---------------------
C:\Java\jdk-25.0.2\bin\java.exe "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2024.3.7\lib\idea_rt.jar=58651" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath D:\Bookstore\bookstore\target\classes;C:\Users\jazzm\.m2\repository\org\hibernate\orm\hibernate-core\6.4.4.Final\hibernate-core-6.4.4.Final.jar;C:\Users\jazzm\.m2\repository\jakarta\transaction\jakarta.transaction-api\2.0.1\jakarta.transaction-api-2.0.1.jar;C:\Users\jazzm\.m2\repository\org\jboss\logging\jboss-logging\3.6.3.Final\jboss-logging-3.6.3.Final.jar;C:\Users\jazzm\.m2\repository\org\hibernate\common\hibernate-commons-annotations\6.0.6.Final\hibernate-commons-annotations-6.0.6.Final.jar;C:\Users\jazzm\.m2\repository\io\smallrye\jandex\3.1.2\jandex-3.1.2.jar;C:\Users\jazzm\.m2\repository\com\fasterxml\classmate\1.7.3\classmate-1.7.3.jar;C:\Users\jazzm\.m2\repository\net\bytebuddy\byte-buddy\1.17.8\byte-buddy-1.17.8.jar;C:\Users\jazzm\.m2\repository\jakarta\xml\bind\jakarta.xml.bind-api\4.0.4\jakarta.xml.bind-api-4.0.4.jar;C:\Users\jazzm\.m2\repository\jakarta\activation\jakarta.activation-api\2.1.4\jakarta.activation-api-2.1.4.jar;C:\Users\jazzm\.m2\repository\org\glassfish\jaxb\jaxb-runtime\4.0.6\jaxb-runtime-4.0.6.jar;C:\Users\jazzm\.m2\repository\org\glassfish\jaxb\jaxb-core\4.0.6\jaxb-core-4.0.6.jar;C:\Users\jazzm\.m2\repository\org\eclipse\angus\angus-activation\2.0.3\angus-activation-2.0.3.jar;C:\Users\jazzm\.m2\repository\org\glassfish\jaxb\txw2\4.0.6\txw2-4.0.6.jar;C:\Users\jazzm\.m2\repository\com\sun\istack\istack-commons-runtime\4.1.2\istack-commons-runtime-4.1.2.jar;C:\Users\jazzm\.m2\repository\jakarta\inject\jakarta.inject-api\2.0.1\jakarta.inject-api-2.0.1.jar;C:\Users\jazzm\.m2\repository\org\antlr\antlr4-runtime\4.13.0\antlr4-runtime-4.13.0.jar;C:\Users\jazzm\.m2\repository\jakarta\persistence\jakarta.persistence-api\3.1.0\jakarta.persistence-api-3.1.0.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-context\6.1.6\spring-context-6.1.6.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-aop\7.0.7\spring-aop-7.0.7.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-beans\7.0.7\spring-beans-7.0.7.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-core\7.0.7\spring-core-7.0.7.jar;C:\Users\jazzm\.m2\repository\commons-logging\commons-logging\1.3.6\commons-logging-1.3.6.jar;C:\Users\jazzm\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-expression\7.0.7\spring-expression-7.0.7.jar;C:\Users\jazzm\.m2\repository\io\micrometer\micrometer-observation\1.16.5\micrometer-observation-1.16.5.jar;C:\Users\jazzm\.m2\repository\io\micrometer\micrometer-commons\1.16.5\micrometer-commons-1.16.5.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-orm\6.1.6\spring-orm-6.1.6.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-jdbc\7.0.7\spring-jdbc-7.0.7.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-tx\6.1.6\spring-tx-6.1.6.jar;C:\Users\jazzm\.m2\repository\com\mysql\mysql-connector-j\8.3.0\mysql-connector-j-8.3.0.jar;C:\Users\jazzm\.m2\repository\org\aspectj\aspectjrt\1.9.25.1\aspectjrt-1.9.25.1.jar;C:\Users\jazzm\.m2\repository\org\aspectj\aspectjweaver\1.9.25.1\aspectjweaver-1.9.25.1.jar;C:\Users\jazzm\.m2\repository\org\slf4j\slf4j-simple\2.0.13\slf4j-simple-2.0.13.jar;C:\Users\jazzm\.m2\repository\org\slf4j\slf4j-api\2.0.17\slf4j-api-2.0.17.jar com.tommy.bookstore.tasks.BookstoreApplication
May 28, 2026 1:58:58 PM org.hibernate.Version logVersion
INFO: HHH000412: Hibernate ORM core version 6.4.4.Final
May 28, 2026 1:58:58 PM org.hibernate.cache.internal.RegionFactoryInitiator initiateService
INFO: HHH000026: Second-level cache disabled
May 28, 2026 1:58:58 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl configure
WARN: HHH10001002: Using built-in connection pool (not intended for production use)
May 28, 2026 1:58:58 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001005: Loaded JDBC driver class: com.mysql.cj.jdbc.Driver
May 28, 2026 1:58:58 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001012: Connecting with JDBC URL [jdbc:mysql://localhost:3306/my_db?useSSL=false&serverTimezone=UTC]
May 28, 2026 1:58:58 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001001: Connection properties: {user=bestuser, password=****}
May 28, 2026 1:58:58 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001003: Autocommit mode: false
May 28, 2026 1:58:58 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl$PooledConnections <init>
INFO: HHH10001115: Connection pool size: 20 (min=1)
May 28, 2026 1:58:59 PM org.hibernate.engine.jdbc.dialect.internal.DialectFactoryImpl constructDialect
WARN: HHH90000025: MySQLDialect does not need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
May 28, 2026 1:59:00 PM org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl getIsolatedConnection
INFO: HHH10001501: Connection obtained from JdbcConnectionAccess [org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess@6f31df32] for (non-JTA) DDL execution was not in auto-commit mode; the Connection 'local transaction' will be committed and the Connection will be set into auto-commit mode.
Hibernate: select p1_0.id,p1_0.name from publishers p1_0
Hibernate: select m1_0.publisher_id,m1_0.id,m1_0.name,m1_0.price from magazines m1_0 where m1_0.publisher_id=?
Hibernate: select m1_0.publisher_id,m1_0.id,m1_0.name,m1_0.price from magazines m1_0 where m1_0.publisher_id=?
[Magazine{id=1, name='Batman: Gotham Nights #11', price=4.99, publisher=Publisher{id=1, name='DC Comics'}}, Magazine{id=2, name='Superman: Metropolis Weekly #8', price=5.49, publisher=Publisher{id=1, name='DC Comics'}}, Magazine{id=3, name='Flash Speed Force Monthly #21', price=3.95, publisher=Publisher{id=1, name='DC Comics'}}, Magazine{id=4, name='Wonder Woman Amazon Chronicles #14', price=6.25, publisher=Publisher{id=1, name='DC Comics'}}, Magazine{id=5, name='Justice League Unlimited Special #3', price=7.1, publisher=Publisher{id=1, name='DC Comics'}}, Magazine{id=6, name='Green Lantern Cosmic Patrol #17', price=4.75, publisher=Publisher{id=1, name='DC Comics'}}, Magazine{id=7, name='Aquaman Ocean Kingdom Digest #5', price=3.5, publisher=Publisher{id=1, name='DC Comics'}}, Magazine{id=8, name='Teen Titans Academy Journal #12', price=5.2, publisher=Publisher{id=1, name='DC Comics'}}]
[Magazine{id=9, name='Spider-Man: Web of Shadows #18', price=5.99, publisher=Publisher{id=2, name='Marvel Comics'}}, Magazine{id=10, name='Iron Man Tech Monthly #7', price=6.25, publisher=Publisher{id=2, name='Marvel Comics'}}, Magazine{id=11, name='Captain America Liberty Journal #12', price=4.8, publisher=Publisher{id=2, name='Marvel Comics'}}, Magazine{id=12, name='Thor: Asgard Chronicles #4', price=7.15, publisher=Publisher{id=2, name='Marvel Comics'}}, Magazine{id=13, name='Doctor Strange Mystic Arts Review #9', price=6.7, publisher=Publisher{id=2, name='Marvel Comics'}}, Magazine{id=14, name='Black Panther Wakanda Times #15', price=5.4, publisher=Publisher{id=2, name='Marvel Comics'}}, Magazine{id=15, name='X-Men Mutation Report #22', price=4.95, publisher=Publisher{id=2, name='Marvel Comics'}}, Magazine{id=16, name='Guardians of the Galaxy Space Digest #11', price=6.9, publisher=Publisher{id=2, name='Marvel Comics'}}]

Process finished with exit code 0



---------------task6.md-------------------------------

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