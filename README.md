**Supercard**

This application provides a set of REST APIs to create a new credit card as well to fetch all existing credit cards in the system.


| API | Method | URI                    | Request Payload                                         | Response Payload                                                                                                                                                                                                | Response                                      |
|-----|--------|------------------------|---------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------|
| add | POST   | /api/creditcard/v1/add | \{  "name":"someName", "number":"4444333322221111",  \} | NULL                                                                                                                                                                                                            | 201\-CREATED, 409\-CONFLICT, 400\-BAD REQUEST |
| get | GET    | /api/creditcard/v1/get | NULL                                                    | \[     \{         "name": "amrutaJoshi",         "number": 4444333322221111,         "limit": 0\.0     \},     \{         "name": "amruta",         "number": 5547692003986836,         "limit": 0\.0     \} \] | 200\-OK                                       |

**API - add**

This API Validates the input before creating a new credit card. Below are the validation rules imposed. Please make sure you meet these validation.

****Credit Card name : 
The card name should be valid. Strings containing null values or empty Strings will result in validation failure.

****Credit Card number : 
The card name should not be exceed 19 digits. 