package com.vibaps.merged.safetyreport;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

//import com.geotab.entity.*;



public class HibernateUtil {
   private static StandardServiceRegistry registry;
   private static SessionFactory sessionFactory;

   public static SessionFactory getSessionFactory() {
      if (sessionFactory == null) {
         try {
            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

            //Configuration properties
            Map<String, Object> settings = new HashMap<String, Object>();
            settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
            //settings.put(Environment.URL, "jdbc:mysql://vibaps.com:3306/vibapsdb?autoReconnect=true&useSSL=false");
           // settings.put(Environment.USER, "vibaps");
           //settings.put(Environment.PASS, "vibaps");
            
            settings.put(Environment.URL, "jdbc:mysql://localhost:3306/vibapsdb?autoReconnect=true&useSSL=false");
  
          settings.put(Environment.USER, "goldedu");
          settings.put(Environment.PASS, "goldedu");
            settings.put(Environment.HBM2DDL_AUTO, "validate");
            settings.put(Environment.SHOW_SQL, true);
            
            registryBuilder.applySettings(settings);
            registry = registryBuilder.build();
            
            MetadataSources sources = new MetadataSources(registry);
            //sources.addAnnotatedClass(Com_AuthenticatorEntity.class);
            Metadata metadata = sources.getMetadataBuilder().build();
            
            sessionFactory = metadata.getSessionFactoryBuilder().build();
         } catch (Exception e) {
            if (registry != null) {
               StandardServiceRegistryBuilder.destroy(registry);
            }
            e.printStackTrace();
         }
      }
      return sessionFactory;
   }

   public static void shutdown() {
      if (registry != null) {
         StandardServiceRegistryBuilder.destroy(registry);
      }
   }

public static Session getsession() {
    Session session = null;
    
    try {
	         session = HibernateUtil.getSessionFactory().openSession();
    }catch (Exception e) {
		// TODO: handle exception
	}
return session;
}
}