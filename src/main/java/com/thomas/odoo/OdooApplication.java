package com.thomas.odoo;

import com.thomas.odoo.dao.DataDao;
import com.thomas.odoo.service.DemoConnect;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OdooApplication {
//https://www.odoo.com/documentation/18.0/developer/reference/backend/orm.html
    //https://www.odoo.com/documentation/18.0/developer/reference/external_api.html
    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext context =SpringApplication.run(OdooApplication.class, args);
        var connect = context.getBean(DemoConnect.class);

        var config = connect.connect();
        connect.checkConnection(config.getHost());
        var userId = connect.authenticate(config);
        System.out.println("User id %s".formatted(userId));

        config.setUid(userId);

        var dao = context.getBean(DataDao.class);

        var res = dao.find(config);

     //   System.out.println(dao.getRandomProduct(config));

        dao.getAllModels(config);

      //  var invoices = dao.findInvoices(config);

        dao.createInvoice(config);

      //  System.out.println(res);

    }
}
