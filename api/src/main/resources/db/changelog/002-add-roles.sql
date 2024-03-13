INSERT INTO roles
(
    name,
    posts_access_level,
    users_access_level,
    albums_access_level
)
VALUES
    ('ROLE_ADMIN' , 'EDIT'   , 'EDIT'   , 'EDIT'   ),
    ('ROLE_VIEWER', 'VIEW'   , 'VIEW'   , 'VIEW'   ),
    ('ROLE_POSTS' , 'EDIT'   , 'NOTHING', 'NOTHING'),
    ('ROLE_USERS' , 'NOTHING', 'EDIT'   , 'NOTHING'),
    ('ROLE_ALBUMS', 'NOTHING', 'NOTHING', 'EDIT'   );
