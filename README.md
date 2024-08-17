## Project Description
The NEU Student Healthcare Management System is designed to manage health-related services for students at Northeastern University. This system will make healthcare processes easier, keep student health information secure, and improve overall student health care using modern web technologies. The system will include features for user authentication, Google OAuth, role-based access control, and efficient management of healthcare-related data and workflows.

## Problem Statement
Northeastern University students, especially international students, often face challenges in managing their health information, booking medical appointments, handling prescriptions, and keeping their health data secure. These challenges can lead to delayed or inadequate healthcare, increased stress, and overall dissatisfaction with the healthcare services provided. To address these issues and improve healthcare management for NEU students, there is a pressing need for a robust, secure, and efficient system that streamlines these processes and supports students' unique healthcare needs.

## Solution
The proposed NEU Student Healthcare Management System will tackle these issues with the following features:

• **User Login and Security:** A secure login system that allows different types of users (students, doctors, admins) to access the system based on their roles. Google OAuth is implemented for authentication, and a login interceptor ensures that only authorized users can access the system.

• **Managing Health Records:** Enabling students to Create, Read, Update, and Delete their health records, including personal details, medical history, and contact information.

• **Appointment Scheduling:** Allowing students to manage their appointments with healthcare providers efficiently.

• **Prescription Handling:** This feature enables doctors to create, manage, and update prescriptions, including information about medications and dosage instructions.

• **Notifications:** Automated email notifications to remind students of their upcoming appointments, helping them keep track of their schedule.

• **Health Resource Access:** Access to health information and resources for students to address their unique needs.

• **AJAX and Pagination:** AJAX is used for partial page refreshing to enhance user experience, and pagination is implemented to manage large data sets.

• **Redis Caching:** Utilized to decrease network traffic and improve application performance.

• **Bitly Integration:** Bitly is used for URL shortening to make sharing links easier and more efficient.

• **Swagger Documentation:** Provides comprehensive API documentation for easy understanding and integration.

## Use Cases
### 1. User Login and Security
• **Description:** A safe way for different types of users (students, doctors, admins) to log in and register with the system based on their roles.

• **Roles:** Students/Patients, Doctors, Admins

• **Steps:**
  1. **Login Form:**

     • Users enter their username and password.

     • **Login Button:** To submit the login form.

     • **Register Link:** If users don’t have an account, they can click on a Register link to navigate to the registration page.
  2. **Registration Form:**

     • Users enter their username, password, confirm password, email address, and personal details. 
     
     • They select their role from a dropdown or radio buttons (Student, Doctor).

     • **Register Button:** To submit the registration form. After clicking the Register button, the page automatically redirects to the login page, allowing users to log in with their newly created account.

### 2. User Record Management
• **Description:** Manage student health records, including personal details, medical history, contact information, and doctor’s records.

• **Roles:** Doctors, Students/Patients, Admin

• **Steps:**
  1. **Doctors:**
     • Update their own personal details and contact information.
     • Create a patient health record.
     • Reschedule or cancel appointments.
  2. **Students/Patients:**
     • Update their own personal details and contact information.
     • Schedule appointments based on a doctor’s availability.
     • View their own health record summary.
  3. **Admin:**
     • View and manage lists of students and doctors, with the ability to activate or deactivate their accounts.
     • Oversee and review health records, though not directly editing them.

### 3. Appointment Scheduling
• **Description:** Schedule, reschedule, and cancel appointments with doctors.

• **Roles:** Students/Patients, Doctors

• **Steps:**
  1. Student selects a doctor and requests an appointment.
  2. The system checks the availability of the healthcare provider.
  3. If the slot is available, the appointment is booked, and the student gets a confirmation email.
  4. The doctor can reschedule or cancel the appointment if needed.

### 4. Notifications
• **Description:** Send email reminders about appointments.

• **Roles:** Students/Patients

• **Steps:** The system sends an email confirmation when an appointment is booked, rescheduled, or canceled.

### 5. Health Resource Access
• **Description:** Provide access to health resources and information tailored for international students.

• **Roles:** Students/Patients

• **Steps:**
  1. Students use the system to access health resources and information.
  2. The system provides details on healthcare services, insurance, mental health support, and other important topics.

## Technology Stack
### Backend
• **Spring Boot:** For building the backend RESTful web services.

• **Hibernate:** For ORM (Object-Relational Mapping) to interact with the MySQL database.

• **Spring Security:** For implementing authentication and authorization.

• **Redis:** For caching to improve application performance and reduce database load.

• **SLF4J:** For logging and debugging purposes.

### Frontend
• **JSP:** For rendering views.

• **HTML, CSS:** For designing the user interface.

• **AJAX:** For partial page refreshing to improve user experience.

• **Pagination:** For handling large data sets in a user-friendly manner.

### Database
• **MySQL:** For storing student health records, appointments, and personal details.

### Additional Tools
• **Bitly:** For URL shortening to make sharing links more efficient.

• **Swagger:** For API documentation to facilitate easier integration and understanding of the system's endpoints.
