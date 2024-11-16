package com.thomas.odoo.dao;

import com.thomas.odoo.dto.ConfigServer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@Component
@RequiredArgsConstructor
public class DataDao {
    private final XmlRpcClient client;


    @SneakyThrows
    public List<Object> find(ConfigServer config) {
        final XmlRpcClientConfigImpl common_config = initClientConfig(config.getHost());

        return asList((Object[]) client.execute(common_config, "execute_kw", asList(
                config.getDatabase(), config.getUid(), config.getPassword(),
                "res.partner", "search",
                asList(asList(
                        asList("is_company", "=", true)))
        )));
    }

    @SneakyThrows
    public List<Object> findInvoices(ConfigServer config) {
        final XmlRpcClientConfigImpl common_config = initClientConfig(config.getHost());

        return asList((Object[]) client.execute(common_config, "execute_kw", asList(
                config.getDatabase(), config.getUid(), config.getPassword(),
                "account.move", "search",
                emptyList()
        )));
    }

    private XmlRpcClientConfigImpl initClientConfig(String host) throws MalformedURLException {
        final XmlRpcClientConfigImpl common_config = new XmlRpcClientConfigImpl();
        common_config.setServerURL(new URL(String.format("%s/xmlrpc/2/object", host)));
        return common_config;
    }

    @SneakyThrows
    public void createInvoice(ConfigServer config) {
        final XmlRpcClientConfigImpl common_config = initClientConfig(config.getHost());

        // Create an invoice (Factura)
        Map<String, Object> invoiceData = new HashMap<>();
       var partner = (Integer) find(config).get(0);
        invoiceData.put("partner_id", partner); // Partner (Customer) ID
        invoiceData.put("invoice_date", "2024-11-13"); // Invoice date

        // Invoice lines - Add product or service
        ArrayList<Object> invoiceLines = new ArrayList<>();
        Map<String, Object> line1 = new HashMap<>();
     //   line1.put("product_id", 1);  // Product ID
     //   line1.put("quantity", 2);    // Quantity
    //    line1.put("price_unit", 100); // Unit price
        invoiceLines.add(line1);

    //    invoiceData.put("invoice_line_ids", invoiceLines);


        // Call the Odoo 'account.move' model to create the invoice
        Object[] params = new Object[]{config.getDatabase(), config.getUid(), config.getPassword(), "account.move", "create", new Object[]{invoiceData}};
        Object invoiceId = client.execute(common_config, "execute_kw", params);


        // Display the result (new invoice ID)
        System.out.println("Invoice created with ID: " + invoiceId);

        // After the invoice is created, you can post it to confirm the transaction
       /* Object[] postParams = new Object[]{config.getDatabase(), config.getUid(), config.getPassword(), "account.move", "action_post", new Object[]{invoiceId}};
        Boolean isPosted = (Boolean) client.execute(common_config, "execute_kw", postParams);
        if (isPosted) {
            System.out.println("Invoice posted successfully.");
        } else {
            System.out.println("Failed to post the invoice.");
        }*/
    }


    public Integer getRandomProduct(ConfigServer config) throws Exception {
        final XmlRpcClientConfigImpl common_config = initClientConfig(config.getHost());
        Object[] productIds = (Object[]) client.execute(common_config, "execute_kw",
                asList(config.getDatabase(), config.getUid(), config.getPassword(), "product.product", "search",
                        asList(asList(asList("type", "=", "product")))));
        if (productIds.length > 0) {
            return (Integer) productIds[0]; // Return the first found product ID
        } else {
            Map<String, Object> productTemplateData = new HashMap<>();

            productTemplateData.put("name", "Sample Product");
          //  productTemplateData.put("type", "product"); // Type: 'product' or 'service'

            Object[] params = new Object[]{config.getDatabase(), config.getUid(), config.getPassword(),
                    "product", "create", new Object[]{productTemplateData}};
            Integer productTemplateId = (Integer) client.execute(common_config,"execute_kw", params);

            productIds = (Object[]) client.execute(common_config, "execute_kw",
                    asList(config.getDatabase(), config.getUid(), config.getPassword(), "product.product", "search",
                            asList(asList(asList("type", "=", "product")))));
            return (Integer) productIds[0];
        }
    }

    public void getAllModels(ConfigServer config) throws Exception {
        final XmlRpcClientConfigImpl common_config = initClientConfig(config.getHost());

        var models = (Object[]) client.execute(common_config, "execute_kw",
                asList(config.getDatabase(), config.getUid(), config.getPassword(), "ir.model", "search_read",
                        emptyList()));


        System.out.println("List of all models in Odoo:");
        for (Object model : models) {

            String modelName = (String) ((Map<String, Object>) model).get("model");
            System.out.println(modelName);
        }
    }

}
