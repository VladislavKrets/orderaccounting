package online.omnia.order;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;

/**
 * Created by lollipop on 22.09.2017.
 */
public class MySQLDaoImpl {
    private static Configuration masterDbConfiguration;
    private static SessionFactory masterDbSessionFactory;

    private static MySQLDaoImpl instance;

    static {
        masterDbConfiguration = new Configuration()
                .addAnnotatedClass(AdvertsEntity.class)
                .addAnnotatedClass(OrderEntity.class)
                .configure("/hibernate.cfg.xml");
        Map<String, String> properties = FileWorkingUtils.iniFileReader();

        masterDbConfiguration.setProperty("hibernate.connection.password", properties.get("master_db_password"));
        masterDbConfiguration.setProperty("hibernate.connection.username", properties.get("master_db_username"));
        masterDbConfiguration.setProperty("hibernate.connection.url", properties.get("master_db_url"));
        while (true) {
            try {
                masterDbSessionFactory = masterDbConfiguration.buildSessionFactory();
                break;
            } catch (PersistenceException e) {
                try {
                    e.printStackTrace();
                    System.out.println("Can't connect to master db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public AdvertsEntity getAdvByName(String name) {
        Session session = masterDbSessionFactory.openSession();
        AdvertsEntity advertsEntity = null;
        try {
            advertsEntity = session.createQuery("from AdvertsEntity where advshortname=:name", AdvertsEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
        }
        catch (NoResultException e) {
            advertsEntity = null;
        }
        catch (PersistenceException e) {
            try {
                e.printStackTrace();
                System.out.println("Can't connect to master db");
                System.out.println("Waiting for 30 seconds");
                Thread.sleep(30000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        session.close();
        return advertsEntity;
    }

    public synchronized void addOrderEntity(OrderEntity orderEntity) {
        Session session = masterDbSessionFactory.openSession();
        session.beginTransaction();
        session.save(orderEntity);
        session.getTransaction().commit();
        session.close();
    }

    private MySQLDaoImpl() {
    }

    public static SessionFactory getMasterDbSessionFactory() {
        return masterDbSessionFactory;
    }

    public static MySQLDaoImpl getInstance() {
        if (instance == null) instance = new MySQLDaoImpl();
        return instance;
    }
}
