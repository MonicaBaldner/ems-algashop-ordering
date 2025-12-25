package com.algaworks.algashop.ordering.core.application.customer.management;

public class CustomerJsonSamples {

    public static String validJsonInput() {
        return """
    {
      "firstName": "John",
      "lastName": "Doe",
      "email": "johndoe@email.com",
      "document": "12345",
      "phone": "1191234564",
      "birthDate": "1991-07-05",
      "promotionNotificationsAllowed": false,
      "address": {
        "street": "Bourbon Street",
        "number": "2000",
        "complement": "apt 122",
        "neighborhood": "North Ville",
        "city": "Yostfort",
        "state": "South Carolina",
        "zipCode": "12321"
      }
    }
    """;
    }
    public static String invalidJsonInput() {
        return """
    {
      "firstName": "",  
      "lastName": "Doe",
      "email": "invalid-email",  
      "document": "12345",
      "phone": "1191234564",
      "birthDate": "1991-07-05",
      "promotionNotificationsAllowed": false,
      "address": {
        "street": "Bourbon Street",
        "number": "2000",
        "complement": "apt 122",
        "neighborhood": "North Ville",
        "city": "Yostfort",
        "state": "South Carolina",
        "zipCode": "12321"
      }
    }
    """;
    }
}
