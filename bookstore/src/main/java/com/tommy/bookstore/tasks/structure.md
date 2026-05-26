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

----------------------Main6.java----------------------

package com.tommy.bookstore.tasks;

import com.tommy.bookstore.entity.BookstoreService;
import com.tommy.bookstore.entity.Magazine;
import com.tommy.bookstore.entity.Publisher;

import java.util.List;

public class BookstoreApplication {

	public static void main(String[] args) {
		BookstoreService bookstoreService = new BookstoreService();
		Publisher publisher = bookstoreService.findPublisherById(2L);
		System.out.println(publisher.toString());
		List<Magazine> magazineList = bookstoreService.getAllMagazines(2L);
		System.out.println(magazineList);
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
        List <Magazine> list = session.createQuery("from Magazine where publisher_id = :id", Magazine.class).setParameter("id", ID).getResultList();
        session.getTransaction().commit();
        return list;
    }

    public Publisher findPublisherById (Long id) {
        Session session = factory.getCurrentSession();

        session.beginTransaction();
        Publisher publisher = session.get(Publisher.class, id);
        session.getTransaction().commit();
        return publisher;
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
C:\Java\jdk-25.0.2\bin\java.exe "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2024.3.7\lib\idea_rt.jar=64344" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath D:\Bookstore\bookstore\target\classes;C:\Users\jazzm\.m2\repository\org\hibernate\orm\hibernate-core\6.4.4.Final\hibernate-core-6.4.4.Final.jar;C:\Users\jazzm\.m2\repository\jakarta\transaction\jakarta.transaction-api\2.0.1\jakarta.transaction-api-2.0.1.jar;C:\Users\jazzm\.m2\repository\org\jboss\logging\jboss-logging\3.6.3.Final\jboss-logging-3.6.3.Final.jar;C:\Users\jazzm\.m2\repository\org\hibernate\common\hibernate-commons-annotations\6.0.6.Final\hibernate-commons-annotations-6.0.6.Final.jar;C:\Users\jazzm\.m2\repository\io\smallrye\jandex\3.1.2\jandex-3.1.2.jar;C:\Users\jazzm\.m2\repository\com\fasterxml\classmate\1.7.3\classmate-1.7.3.jar;C:\Users\jazzm\.m2\repository\net\bytebuddy\byte-buddy\1.17.8\byte-buddy-1.17.8.jar;C:\Users\jazzm\.m2\repository\jakarta\xml\bind\jakarta.xml.bind-api\4.0.4\jakarta.xml.bind-api-4.0.4.jar;C:\Users\jazzm\.m2\repository\jakarta\activation\jakarta.activation-api\2.1.4\jakarta.activation-api-2.1.4.jar;C:\Users\jazzm\.m2\repository\org\glassfish\jaxb\jaxb-runtime\4.0.6\jaxb-runtime-4.0.6.jar;C:\Users\jazzm\.m2\repository\org\glassfish\jaxb\jaxb-core\4.0.6\jaxb-core-4.0.6.jar;C:\Users\jazzm\.m2\repository\org\eclipse\angus\angus-activation\2.0.3\angus-activation-2.0.3.jar;C:\Users\jazzm\.m2\repository\org\glassfish\jaxb\txw2\4.0.6\txw2-4.0.6.jar;C:\Users\jazzm\.m2\repository\com\sun\istack\istack-commons-runtime\4.1.2\istack-commons-runtime-4.1.2.jar;C:\Users\jazzm\.m2\repository\jakarta\inject\jakarta.inject-api\2.0.1\jakarta.inject-api-2.0.1.jar;C:\Users\jazzm\.m2\repository\org\antlr\antlr4-runtime\4.13.0\antlr4-runtime-4.13.0.jar;C:\Users\jazzm\.m2\repository\jakarta\persistence\jakarta.persistence-api\3.1.0\jakarta.persistence-api-3.1.0.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-context\6.1.6\spring-context-6.1.6.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-aop\7.0.7\spring-aop-7.0.7.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-beans\7.0.7\spring-beans-7.0.7.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-core\7.0.7\spring-core-7.0.7.jar;C:\Users\jazzm\.m2\repository\commons-logging\commons-logging\1.3.6\commons-logging-1.3.6.jar;C:\Users\jazzm\.m2\repository\org\jspecify\jspecify\1.0.0\jspecify-1.0.0.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-expression\7.0.7\spring-expression-7.0.7.jar;C:\Users\jazzm\.m2\repository\io\micrometer\micrometer-observation\1.16.5\micrometer-observation-1.16.5.jar;C:\Users\jazzm\.m2\repository\io\micrometer\micrometer-commons\1.16.5\micrometer-commons-1.16.5.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-orm\6.1.6\spring-orm-6.1.6.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-jdbc\7.0.7\spring-jdbc-7.0.7.jar;C:\Users\jazzm\.m2\repository\org\springframework\spring-tx\6.1.6\spring-tx-6.1.6.jar;C:\Users\jazzm\.m2\repository\com\mysql\mysql-connector-j\8.3.0\mysql-connector-j-8.3.0.jar;C:\Users\jazzm\.m2\repository\org\aspectj\aspectjrt\1.9.25.1\aspectjrt-1.9.25.1.jar;C:\Users\jazzm\.m2\repository\org\aspectj\aspectjweaver\1.9.25.1\aspectjweaver-1.9.25.1.jar;C:\Users\jazzm\.m2\repository\org\slf4j\slf4j-simple\2.0.13\slf4j-simple-2.0.13.jar;C:\Users\jazzm\.m2\repository\org\slf4j\slf4j-api\2.0.17\slf4j-api-2.0.17.jar com.tommy.bookstore.tasks.BookstoreApplication
May 26, 2026 2:23:56 PM org.hibernate.Version logVersion
INFO: HHH000412: Hibernate ORM core version 6.4.4.Final
May 26, 2026 2:23:56 PM org.hibernate.cache.internal.RegionFactoryInitiator initiateService
INFO: HHH000026: Second-level cache disabled
May 26, 2026 2:23:56 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl configure
WARN: HHH10001002: Using built-in connection pool (not intended for production use)
May 26, 2026 2:23:56 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001005: Loaded JDBC driver class: com.mysql.cj.jdbc.Driver
May 26, 2026 2:23:56 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001012: Connecting with JDBC URL [jdbc:mysql://localhost:3306/my_db?useSSL=false&serverTimezone=UTC]
May 26, 2026 2:23:56 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001001: Connection properties: {user=bestuser, password=****}
May 26, 2026 2:23:56 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001003: Autocommit mode: false
May 26, 2026 2:23:56 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl$PooledConnections <init>
INFO: HHH10001115: Connection pool size: 20 (min=1)
May 26, 2026 2:23:56 PM org.hibernate.engine.jdbc.dialect.internal.DialectFactoryImpl constructDialect
WARN: HHH90000025: MySQLDialect does not need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
Hibernate: select p1_0.id,p1_0.name from publishers p1_0 where p1_0.id=?
Publisher{id=2, name='Marvel Comics'}
Exception in thread "main" java.lang.IllegalArgumentException: org.hibernate.query.SemanticException: Could not interpret path expression 'publisher_id'
at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:143)
at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:167)
at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:173)
at org.hibernate.internal.AbstractSharedSessionContract.createQuery(AbstractSharedSessionContract.java:848)
at org.hibernate.internal.AbstractSharedSessionContract.createQuery(AbstractSharedSessionContract.java:136)
at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
at java.base/java.lang.reflect.Method.invoke(Method.java:565)
at org.hibernate.context.internal.ThreadLocalSessionContext$TransactionProtectionWrapper.invoke(ThreadLocalSessionContext.java:343)
at jdk.proxy2/jdk.proxy2.$Proxy51.createQuery(Unknown Source)
at com.tommy.bookstore.entity.BookstoreService.getAllMagazines(BookstoreService.java:29)
at com.tommy.bookstore.tasks.BookstoreApplication.main(BookstoreApplication.java:15)
Caused by: org.hibernate.query.SemanticException: Could not interpret path expression 'publisher_id'
at org.hibernate.query.hql.internal.BasicDotIdentifierConsumer$BaseLocalSequencePart.resolvePathPart(BasicDotIdentifierConsumer.java:255)
at org.hibernate.query.hql.internal.BasicDotIdentifierConsumer.consumeIdentifier(BasicDotIdentifierConsumer.java:91)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitSimplePath(SemanticQueryBuilder.java:5050)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitIndexedPathAccessFragment(SemanticQueryBuilder.java:5009)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitGeneralPathFragment(SemanticQueryBuilder.java:4984)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitGeneralPathExpression(SemanticQueryBuilder.java:1776)
at org.hibernate.grammars.hql.HqlParser$GeneralPathExpressionContext.accept(HqlParser.java:7699)
at org.antlr.v4.runtime.tree.AbstractParseTreeVisitor.visitChildren(AbstractParseTreeVisitor.java:46)
at org.hibernate.grammars.hql.HqlParserBaseVisitor.visitBarePrimaryExpression(HqlParserBaseVisitor.java:756)
at org.hibernate.grammars.hql.HqlParser$BarePrimaryExpressionContext.accept(HqlParser.java:7157)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.createComparisonPredicate(SemanticQueryBuilder.java:2429)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitComparisonPredicate(SemanticQueryBuilder.java:2392)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitComparisonPredicate(SemanticQueryBuilder.java:269)
at org.hibernate.grammars.hql.HqlParser$ComparisonPredicateContext.accept(HqlParser.java:6164)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitWhereClause(SemanticQueryBuilder.java:2244)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitWhereClause(SemanticQueryBuilder.java:269)
at org.hibernate.grammars.hql.HqlParser$WhereClauseContext.accept(HqlParser.java:5905)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitQuery(SemanticQueryBuilder.java:1159)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitQuerySpecExpression(SemanticQueryBuilder.java:941)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitQuerySpecExpression(SemanticQueryBuilder.java:269)
at org.hibernate.grammars.hql.HqlParser$QuerySpecExpressionContext.accept(HqlParser.java:1869)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitSimpleQueryGroup(SemanticQueryBuilder.java:926)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitSimpleQueryGroup(SemanticQueryBuilder.java:269)
at org.hibernate.grammars.hql.HqlParser$SimpleQueryGroupContext.accept(HqlParser.java:1740)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitSelectStatement(SemanticQueryBuilder.java:443)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.visitStatement(SemanticQueryBuilder.java:402)
at org.hibernate.query.hql.internal.SemanticQueryBuilder.buildSemanticModel(SemanticQueryBuilder.java:311)
at org.hibernate.query.hql.internal.StandardHqlTranslator.translate(StandardHqlTranslator.java:71)
at org.hibernate.query.internal.QueryInterpretationCacheStandardImpl.createHqlInterpretation(QueryInterpretationCacheStandardImpl.java:165)
at org.hibernate.query.internal.QueryInterpretationCacheStandardImpl.resolveHqlInterpretation(QueryInterpretationCacheStandardImpl.java:147)
at org.hibernate.internal.AbstractSharedSessionContract.interpretHql(AbstractSharedSessionContract.java:790)
at org.hibernate.internal.AbstractSharedSessionContract.createQuery(AbstractSharedSessionContract.java:840)
... 7 more

Process finished with exit code 1