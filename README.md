# 🍜 **Chifa Fu — Plataforma Web Integral con IA Conversacional**

![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![IntelliJ](https://img.shields.io/badge/IntelliJIDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)

---

## 🥡 Descripción General

**Chifa Fu** es una plataforma web moderna diseñada para digitalizar la experiencia del restaurante y elevar su atención al cliente al siguiente nivel.  

Con esta plataforma los usuarios pueden:
- Explorar el menú completo  
- Realizar pedidos online  
- Pagar de manera segura  
- Hacer seguimiento del estado del pedido  
- Interactuar con un **chatbot inteligente** capaz de resolver dudas, recomendar platos y guiar al usuario

Mientras que el equipo del restaurante puede gestionar:
- Platos del menú  
- Pedidos  
- Usuarios  
- Inventario  
- Estadísticas internas  

Todo desde un **panel administrativo profesional**, construido con tecnologías robustas y escalables.

---

## 🤖 Integración de Inteligencia Artificial

El proyecto incluye un **ChatBot IA** integrado directamente en la web.

Este asistente virtual puede:
- Responder preguntas frecuentes  
- Recomendar platos según preferencias  
- Guiar en el proceso de compra  
- Explicar ingredientes  
- Indicar horarios, promociones o disponibilidad  
- Resolver dudas generales sobre el restaurante  

Está implementado usando APIs modernas y un modelo de lenguaje actualizado, brindando una experiencia cercana, fluida y natural.

---

## 🛠️ Tecnologías Usadas

### Backend
- Spring Boot  
- Maven  
- Spring Web  
- Spring Data JPA  
- Spring Security  
- Lombok  
- MySQL Driver  
- Thymeleaf  
- API externa para el ChatBot IA  

### Frontend
- HTML5  
- CSS3  
- Bootstrap  
- Thymeleaf  

### Base de Datos
- MySQL

### Otros
- Git & GitHub  
- IntelliJ IDEA  


## 🧠 Arquitectura del Sistema

El sistema está dividido en 3 capas principales:

| Capa                             | Descripción                                                           |
| -------------------------------- | --------------------------------------------------------------------- |
| **Presentación (UI)**            | Interfaces de usuario diseñadas con HTML, CSS, Bootstrap y Thymeleaf. |
| **Lógica (Service/Controllers)** | Reglas del negocio, validaciones, flujos del sistema.                 |
| **Datos (Repository + MySQL)**   | Gestión de entidades, consultas, persistencia y relaciones.           |

Incluye además:

* **Módulo de autenticación con Spring Security**
* **Módulo de pedidos**
* **Módulo de menú**
* **Módulo de usuarios**
* **Módulo de chatbot IA**
* **Panel administrativo profesional**

---

## 👥 Roles del Equipo

| Rol                                                                        | Integrante                                                                                                                                           | Función |
| -------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- | ------- |
| 👨‍💻 **Backend & Frontend Developer**                                     | **Muñoz Manosalva, Jhoan Aronith**                                                                                                                   |         |
| GitHub: [https://github.com/JhoanAronith](https://github.com/JhoanAronith) | Implementación de controladores, vistas, CRUDs, integración de Spring Boot con Thymeleaf y gestión del flujo completo del sistema.                   |         |
| 🤖 **Machine Learning Engineer & Database Architect**                      | **Sullón Lévano, Leonardo José**                                                                                                                     |         |
| GitHub: [https://github.com/Levanxx](https://github.com/Levanxx)           | Diseño de la base de datos, optimización de consultas, implementación del chatbot IA, integración con APIs externas, documentación técnica avanzada. |         |
