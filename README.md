## Proxy Json Pacleholder

---

#### Endpoints for REST API:

- ##### Authentication.
    - `POST: /auth/api/register`
    - `POST: /auth/api/enter`\
Body example: `{ "login": "test", "password": "test" }`.\
Response consists of access token which is valid next 10 minutes.\
For authorization `X-AuthToken` HTTP header is used.

- ##### Adding new user roles.
  - `POST: /user-roles/api/add`\
Body example: `{ "userLogin": "test_login", "roleName:" "test_role" }`\
To use new role preferences you should authorize again.

- ##### Proxy queries on `https://jsonplaceholder.typicode.com` (`GET`, `POST`, `PUT`, `DELETE`):
    - `/api/posts/**`
    - `/api/users/**`
    - `/api/albums/**`